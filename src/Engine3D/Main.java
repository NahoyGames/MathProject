package Engine3D;

import java.awt.*;
import java.util.Arrays;

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

        Mesh[] meshes = Mesh.parseObj("src/Resources/arrows.obj");

        while (true)
        {
            cam.renderFrame(meshes);


            try { Thread.sleep(16); }
            catch (Exception e) { }
        }

    }
}
