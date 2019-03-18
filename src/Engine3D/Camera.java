package Engine3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


import Math.*;

public class Camera extends JPanel
{
    /** Data **/
    //region

    // Transform : Holds the camera's position and rotation in world space
    private Transform transform;


    // Render : Buffer for our render... Essentially just an image which lets
    //          us individually change pixels
    private BufferedImage render;


    // Z-Buffer : A 2-Dimensional array, the size of 'render,' which holds the
    //            depth for pixels. Used to render only the front-most faces when
    //            two objects interfere
    private double[][] zBuffer;


    // Graphics : Lets us draw our 'render' buffer to the screen
    private Graphics g;

    //endregion


    /** Constructor :: Lets us initialize a camera **/
    //region

    public Camera()
    {
        transform = new Transform(new Vector3(0, -1, 15), new Vector3(0, Math.PI, 0));
    }

    //endregion


    /** Methods :: Functions to render out a scene, as a physical camera would **/
    //region

    public void renderFrame(Mesh[] meshes)
    {
        // Resets the render buffer every frame
        render = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);


        // Resets the Z-Buffer every frame
        zBuffer = new double[render.getWidth()][render.getHeight()];

        // Retrieves the graphic object every frame
        g = getGraphics();

        // Long list of all the vertices in screen space
        ArrayList<Vector3> output;

        long t = System.currentTimeMillis();


        // Transforms a Vector in world space to camera space
        // First, translate by -1 * camera's position
        // Then, rotate by -1 * camera's rotation.
        // This "remaps" space such that no matter where or at what angle
        // your camera is, all points will be relative to the camera.
        TransformationMatrix globalToCameraSpace = new TransformationMatrix();
        globalToCameraSpace.addTranslation(-transform.position.x, -transform.position.y, -transform.position.z);
        globalToCameraSpace.addRotation(-transform.rotation.x, -transform.rotation.y, -transform.rotation.z);


        // Applies our 'globalToCameraSpace' transformation matrix to EVERY vertex!!
        for (Mesh m : meshes)
        {

            output = new ArrayList<Vector3>();

            for (Vector3 a : m.getVertices())
            {
                // --> GOAL <-- : Project vertex 'a'(in 3D world space), onto the screen(2D pixels)


                // Vector d : Global Space (a) --> Camera Space
                Vector3 d = new Vector3(Matrix.multiply(globalToCameraSpace, a.toMatrix()));


                // Vector b : Camera Space --> Screen Space(Also stores the depth value for z-buffer)
                // Here we're just dividing the x and y components by the depth, z, which gives the
                // illusion of perspective. In the physical world, this is similar to a 90° FOV Camera
                //
                // We store the depth, z, because the computer can't magically detect whether faces are
                // in front of behind other faces. We will then order the 'pixels' by their depth and render
                // only the ones in front
                Vector3 b = new Vector3(d.x / d.z, -d.y / d.z, d.z);


                // Since the output of our World space --> Screen Space transformation outputs
                // coordinates from -1 to 1, we have to scale it up by the width and height
                // of our screen, which is just multiplication + an offset since the origin
                // is at the top-left in java.
                int screenScale = Math.max(getWidth(), getHeight());
                b.x = (b.x * screenScale) + (screenScale / 2);
                b.y = (b.y * screenScale) + (screenScale / 2);


                // Add our result to the array which contains all the other vertices in screen space
                output.add(b);
            }

            for (int i = 0; i < m.getFaces().length; i += 3)
            {
                try
                {
                    fillTriangle(output, m, i);
                }
                catch (Exception e)
                {
                    continue;
                }

            }
        }


        // Places the image we've been calculating, 'render', onto our screen!
        g.drawImage(render, 0, 0, this);


        // How long the frame took to render, in milliseconds. Generally, <16ms is EXTREMELY good,
        // it's ~60 frames per second, which is impressive even on 3d engines that make use of the
        // GPU(hardware specifically made for this type of stuff, which I don't use in this program).
        double delta = System.currentTimeMillis() - t;
        System.out.println(Math.round(1000 / (delta)) + " FPS");


        transform.rotation.x -= 0.001;
        transform.rotation.y -= 0.001;
        transform.position.y += 0.01;
        transform.position.z -= 0.01;
        transform.position.x += 0.01;
    }


    public void fillTriangle(List<Vector3> screenSpaceVertices, Mesh mesh, int index)
    {

        // Here I'm letting a, b, and c be the vertices(in screen space) of the triangle. It's
        // not creating anything new, but it's shorter to abbreviate 'screenSpaceVertices.ge...
        // ...t(mesh.getFaces()[index + v] - 1);' to 'a,' 'b,' or 'c'.
        Vector3 a = screenSpaceVertices.get(mesh.getFaces()[index + 0] - 1);
        Vector3 b = screenSpaceVertices.get(mesh.getFaces()[index + 1] - 1);
        Vector3 c = screenSpaceVertices.get(mesh.getFaces()[index + 2] - 1);


        // These few lines detect if the triangle is offscreen or behind the camera. If so, there's
        // no need to even *try* to draw the triangle, as it's simply impossible or unnecessary.
        if (    (a.z <= 0 || b.z <= 0 || c.z <= 0) ||
                (a.x < 0 && b.x < 0 && c.x < 0) ||
                (a.y < 0 && b.y < 0 && c.y < 0) ||
                (a.x >= render.getWidth() && b.x >= render.getWidth() && c.x >= render.getWidth()) ||
                (a.y >= render.getHeight() && b.y >= render.getHeight() && c.y >= render.getHeight())
            )
        {
            // 'Return' cancels this method
            return;
        }


        // Back-face culling
        //
        // Every face in our mesh has an attributed "normal," which tells the program
        // where this face is facing. If that normal is towards the camera, then the
        // computer will know to draw that face. Otherwise, if it's facing in the opposite
        // direction, then the computer should just ignore it.
        Vector3 aN = mesh.getVertexNormals()[mesh.getNormalIndices()[(index + 0)] - 1];
        Vector3 bN = mesh.getVertexNormals()[mesh.getNormalIndices()[(index + 1)] - 1];
        Vector3 cN = mesh.getVertexNormals()[mesh.getNormalIndices()[(index + 2)] - 1];


        // The dot product is enough to determine whether the face is back-facing or not.
        //
        // Remember that if the dot product of two vectors is 0, then those two vectors
        // are orthogonal. Otherwise, the dot product will be negative or positive depending
        // on whether the two vectors are pointing in the same general direction or to
        // completely opposite ends.
        //
        // The .obj format doesn't give each *face* a normal, but rather, gives each *vertices*
        // a normal. Finding the face normal is a simple average of the normals of the vertices
        // that compose it. In this scenario, I've optimized it to sum instead of average because
        // the magnitude doesn't really matter in the dot product.
        Vector3 faceNormal = aN.clone().add(bN).add(cN);
        if (faceNormal.dotProduct(mesh.getVertices()[mesh.getFaces()[index] - 1].clone().subtract(this.transform.position)) >= 0)
        {
            // Return 'cancels' this method. In this case, if the program has detected that the
            // face is back-facing, then it won't bother drawing it.
            return;
        }


        // Calculates the light factor for this face. This uses simple Lambert shading, which is
        // the dot product of the light source(its direction) and the normal of the face.
        faceNormal.normalize();
        float light = Math.max((float) faceNormal.dotProduct(new Vector3(0.2, 0.5, 0.5).normalize()), 0);



        // Bounding box of the triangle(like fitting a triangle into a tight rectangular box)
        //
        // It's a simple yet crucial optimization... Instead of looping through every single
        // pixel on the screen, we just loop through those that have a possibility of being
        // inside the triangle.
        int xMax = (int) Math.min(Math.max(a.x, Math.max(b.x, c.x)), render.getWidth() - 1);
        int xMin = (int) Math.max(Math.min(a.x, Math.min(b.x, c.x)), 0);
        int yMax = (int) Math.min(Math.max(a.y, Math.max(b.y, c.y)), render.getHeight() - 1);
        int yMin = (int) Math.max(Math.min(a.y, Math.min(b.y, c.y)), 0);


        // Here we loop through the pixels in the bounding box defined above.
        for (int x = xMin; x <= xMax; x++)
        {
            for (int y = yMin; y <= yMax; y++)
            {
                // Method to determine whether a given point is inside a triangle
                // Implementation of https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/
                double abc = Math.abs((a.x * (b.y - c.y)) + (b.x * (c.y - a.y)) + (c.x * (a.y - b.y))) / 2;
                double abp = Math.abs((a.x * (b.y - y)) + (b.x * (y - a.y)) + (x * (a.y - b.y))) / 2;
                double apc = Math.abs((a.x * (y - c.y)) + (x * (c.y - a.y)) + (c.x * (a.y - y))) / 2;
                double pbc = Math.abs((x * (b.y - c.y)) + (b.x * (c.y - y)) + (c.x * (y - b.y))) / 2;


                // According to the theorem(linked above), if the areas of the triangles which include
                // the point add up to the area of the original triangle, then that point is inside the
                // triangle.
                //
                // Note: You might notice that I'm using "<=0.1" and not "==". That's because
                // java(or computers in general) aren't precise with decimal values, so the 0.1
                // serves as a threshold.
                if (Math.abs((abp + apc + pbc) - abc) <= 0.1)
                {
                    // This line calculates the depth of the pixel. This is fairly inaccurate, but the
                    // proper method of doing this is requires barycentric coordinates and perspective
                    // interpolation which is way beyond the scope of this project.
                    //
                    // The 'illusion' this method outputs gives satisfactory results
                    double depth = (a.z + b.z + c.z) / 3;


                    // This 'if' statement compares this current pixel's depth and the one it's about to
                    // over-write's depth. Whichever one is closer to the camera gets to be rendered. This
                    // results any issues with overlapping faces.
                    if (zBuffer[x][y] == 0 || zBuffer[x][y] > depth)
                    {

                        // Draw in the pixel!
                        render.setRGB(x, y, (int) (255 * light));
                        zBuffer[x][y] = depth;

                    }
                }
            }
        }

    }

    //endregion

}
