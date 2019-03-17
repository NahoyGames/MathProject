package Engine3D;

import java.awt.*;
import Math.*;

import javax.swing.*;

public class Main
{

    public static void main(String[] args)
    {
        JFrame window = new JFrame("3D Engine");

        window.setSize(400, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Camera cam = new Camera();

        window.setContentPane(cam);

        window.setVisible(true);

        cam.renderFrame(new Mesh[] {new Mesh("src/Resources/xwing.obj")});

    }


    /*public static void renderFrame(Graphics g, Mesh[] meshes, Transform cam)
    {

        long t = System.currentTimeMillis();

        TransformationMatrix globalToCameraSpace = new TransformationMatrix();
        globalToCameraSpace.addTranslation(-cam.position.x, -cam.position.y, -cam.position.z);
        globalToCameraSpace.addRotation(-cam.rotation.x, -cam.rotation.y, -cam.rotation.z);

        for (Mesh m : meshes)
        {
            for (MeshObject o : m.getObjects())
            {
                for (Vector3 a : o.getVertices())
                {
                    // GOAL :: Project vertex, a, onto the screen

                    // Vector d : Global Space (a) --> Camera Space
                    Vector3 d = new Vector3(Matrix.multiply(globalToCameraSpace, a.toMatrix()));

                    // Vector b : Camera Space --> Screen Space(Also stores the depth value for z-buffer)
                    Vector3 b = new Vector3(d.x / d.z, d.y / d.z, d.z);

                    float fac = (float)b.z / 8;
                    g.setColor(new Color(fac, fac, fac));
                    g.fillOval((int)(b.x * 400) + 200, (int)(b.y * 400) + 200, 3, 3);
                }
            }
        }

        System.out.println("Frame took " + (System.currentTimeMillis() - t) + "ms to render");
    }*/
}