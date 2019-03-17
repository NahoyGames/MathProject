package Engine3D;

import javax.swing.*;
import java.awt.*;
import Math.*;

public class Camera extends JPanel
{

    private Transform transform;


    public Camera()
    {
        transform = new Transform(new Vector3(0, -1, -5), Vector3.zero());
    }


    public Transform getTransform() { return transform; }


    private class RenderingUnit extends Thread
    {
        @Override
        public void run()
        {
            super.run();


        }
    }


    public void renderFrame(Mesh[] meshes)
    {

        Graphics g = super.getGraphics();
        long t = System.currentTimeMillis();

        TransformationMatrix globalToCameraSpace = new TransformationMatrix();
        globalToCameraSpace.addTranslation(-transform.position.x, -transform.position.y, -transform.position.z);
        globalToCameraSpace.addRotation(-transform.rotation.x, -transform.rotation.y, -transform.rotation.z);

        for (Mesh m : meshes)
        {
            for (MeshObject o : m.getObjects())
            {
                for (Vector3 a : o.getVertices())
                {
                    /** GOAL :: Project vertex, a, onto the screen **/

                    // Vector d : Global Space (a) --> Camera Space
                    Vector3 d = new Vector3(Matrix.multiply(globalToCameraSpace, a.toMatrix()));

                    // Vector b : Camera Space --> Screen Space(Also stores the depth value for z-buffer)
                    Vector3 b = new Vector3(d.x / d.z, d.y / d.z, d.z);

                    float fac = (float)b.z / 10;
                    g.setColor(new Color(fac, fac, fac));
                    g.fillOval((int)(b.x * 400) + 200, (int)(b.y * 400) + 200, 3, 3);
                }
            }
        }

        System.out.println("Frame took " + (System.currentTimeMillis() - t) + "ms to render");
    }

}
