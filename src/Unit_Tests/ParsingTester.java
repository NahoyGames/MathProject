package Unit_Tests;

import Engine3D.Mesh;

public class ParsingTester
{

    public static void main(String[] args)
    {
        long t = System.currentTimeMillis();

        System.out.println(Mesh.parseObj("src/Resources/xwing.obj"));

        long delta = System.currentTimeMillis() - t;

        System.out.println("Parsed in " + delta + "ms");
    }

}
