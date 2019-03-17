package Engine3D;

import javax.swing.*;
import java.awt.image.BufferedImage;


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

    //endregion


    /** Constructor :: Lets us initialize a camera **/
    //region

    public Camera()
    {
        transform = new Transform(new Vector3(0, -1, 5), new Vector3(0, Math.PI, 0));
    }

    //endregion


    /** Methods :: Functions to render out a scene, as a physical camera would **/
    //region

    public void renderFrame(Mesh[] meshes)
    {
        // Resets the render buffer every frame
        render = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

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


                // Here we're rasterizing(converting to pixels) the vertices in screen space
                try
                {
                    // Set the color to this vertex's depth, for a cool effect(no necessary)
                    float fac = (float) b.z / 10;

                    // Since the output of our World space --> Screen Space transformation outputs
                    // coordinates from -1 to 1, we have to scale it up by the width and height
                    // of our screen, which is just multiplication.
                    int screenScale = Math.max(getWidth(), getHeight());
                    render.setRGB((int) (b.x * screenScale) + (screenScale / 2), (int) (b.y * screenScale) + (screenScale / 2), (int)((1/fac) * 255 * 3));

                }
                catch (Exception e) { }
            }
        }

        // Places the image we've been calculating, 'render', onto our screen!
        getGraphics().drawImage(render, 0, 0, this);


        // How long the frame took to render, in milliseconds. Generally, <16ms is EXTREMELY good,
        // it's ~60 frames per second, which is impressive even on 3d engines that make use of the
        // GPU(hardware specifically made for this type of stuff, which I don't use in this program).
        System.out.println("Frame took " + (System.currentTimeMillis() - t) + "ms to render");


        transform.rotation.x -= 0.001;
        transform.position.y += 0.01;
        transform.position.z += 0.001;
    }

    //endregion

}
