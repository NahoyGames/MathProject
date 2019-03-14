package Unit_Tests;

import Math.*;

public class MatrixTester
{

    public static void main(String[] args)
    {
        System.out.println(Matrix.identity(3));


        Matrix m1 = Matrix.identity(4);
        m1.setEntry(0, 0, 2);
        Matrix m2 = (new Vector3(2, 3, 1)).toMatrix();
        System.out.println(m1 + " * \n" + m2);
        System.out.println(new Vector3(Matrix.multiply(m1, m2)));
    }

}
