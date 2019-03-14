package Unit_Tests;

import Math.*;

public class VectorTester
{

    public static void main(String[] args)
    {
        // Printing
        System.out.println("Testing out toString() with Vector <0, 90, 20> :: " + (new Vector3(0, 90, 20)));

        // Addition
        System.out.println("Testing out addition with Vector <2.4, 4.1, 4.9> + <1.3, 5.3, 0.1> :: " +
                (new Vector3(2.4, 4.1, 4.9)).add(new Vector3(1.3, 5.3, 0.1))
        );

        // Subtraction
        System.out.println("Testing out subtraction with Vector <2.4, 4.1, 4.9> - <1.3, 5.3, 0.1> :: " +
                (new Vector3(2.4, 4.1, 4.9)).subtract(new Vector3(1.3, 5.3, 0.1))
        );
    }

}
