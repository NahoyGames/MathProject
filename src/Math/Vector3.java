package Math;

/**
 *
 * A 3-Dimensional Vector
 *
 * Assumes the x component refers to right(+) & left(-),
 * Assumes the y component refers to up(+) & down(-),
 * Assumes the z component refers to forward(+) & backwards(-)
 *
 **/
public class Vector3
{

    /** Components :: The x, y, and z components of our Vector **/
    public double x, y, z;


    /** Constructors :: Lets use create & define new Vectors **/
    //region

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Matrix m)
    {
        if (m.columnCount() != 1 || m.rowCount() != 4) { throw new IllegalArgumentException("Invalid Matrix format"); }

        this.x = m.getEntry(0, 0) / m .getEntry(3, 0);
        this.y = m.getEntry(1, 0) / m .getEntry(3, 0);
        this.z = m.getEntry(2, 0) / m .getEntry(3, 0);

    }

    public Vector3()
    {
        this(0, 0 ,0);
    }

    //endregion


    /** Constants :: Easy & convenient way to create common vectors **/
    //region

    public static Vector3 zero() { return new Vector3(0, 0, 0); }
    public static Vector3 one() { return new Vector3(1, 1, 1); }
    public static Vector3 up() { return new Vector3(0, 1, 1); }
    public static Vector3 right() { return new Vector3(1, 0, 0); }
    public static Vector3 forward() { return new Vector3(0, 0, 1); }

    //endregion


    /** Operations :: Definition for Vector operations **/
    //region

    // Adds the components of another Vector to this Vector
    public Vector3 add(Vector3 a)
    {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;

        return this;
    }

    // Multiples this Vector by a scalar
    public Vector3 multiply(double scalar)
    {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;

        return this;
    }

    // Adds this Vector to Vector 'a' * -1
    public Vector3 subtract(Vector3 a)
    {
        this.add(a.clone().multiply(-1));

        return this;
    }

    // Returns the dot product of this vector and vector 'a'
    public double dotProduct(Vector3 a)
    {
        return (this.x * a.x) + (this.y * a.y) + (this.z * a.z);
    }

    // Returns the cross product of this vector and vector 'a'
    public Vector3 crossProduct(Vector3 a)
    {
        return new Vector3((this.y * a.z) - (this.z * a.y), (this.z * a.x) - (this.x * a.z), (this.x * a.y) - (this.y * a.x));
    }

    //endregion


    /** Functions :: Basic functions used on Vectors **/
    //region

    // Returns the length of this Vector
    public double magnitude()
    {
        return Math.sqrt(this.magnitudeSquared());
    }

    // Returns this magnitude squared to avoid doing unnecessary sqrt
    public double magnitudeSquared()
    {
       return (x * x) + (y * y) + (z * z);
    }

    // Returns the angle between 2 vectors
    public static double angleBetween(Vector3 a, Vector3 b)
    {
        return Math.acos(a.dotProduct(b) / (a.magnitude() * b.magnitude()));
    }

    // Returns an array length 2 of Vectors:
    // Element 1: A vector parallel to w
    // Element 2: A vector perpendicular to w
    public Vector3[] projectOnto(Vector3 w)
    {
        Vector3 v1 = w.clone().multiply(this.dotProduct(w) / w.magnitudeSquared());
        Vector3 v2 = this.clone().subtract(v1);

        return new Vector3[] { v1, v2 };
    }

    // Converts this Vector to a homogeneous Matrix in the form:
    // | x |
    // | y |
    // | z |
    // | w |
    public Matrix toMatrix() { return new Matrix(new double[][] {{this.x}, {this.y}, {this.z}, {1}}); }


    //endregion

    /** Technical Stuff :: Makes the program not suck **/
    //region

    public Vector3 clone()
    {
        return new Vector3(this.x, this.y, this.z);
    }

    public String toString() { return "<" + this.x + ", " + this.y + ", " + this.z + ">"; }

    //endregion

}
