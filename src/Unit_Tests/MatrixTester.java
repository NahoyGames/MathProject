package Unit_Tests;

import Math.*;

import java.util.Arrays;

public class MatrixTester
{

    public static void main(String[] args)
    {
        System.out.println(Matrix.identity(3));


        Matrix m1 = new Matrix(new double[][] {{ Math.cos(Math.PI/3), -Math.sin(Math.PI/3)},
                                                {Math.sin(Math.PI/3), Math.cos(Math.PI/3)},
        });
        Matrix m2 = new Matrix(new double[][] {{5}, {2}});
        System.out.println(m1 + " * \n" + m2);
        System.out.println(Matrix.multiply(m1, m2));

    }

}
