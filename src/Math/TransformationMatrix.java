package Math;

/***
 * An extension of the Matrix class which allows to 'stack'
 * pre-defined transformations on top of each other
 *
 * Uses homogeneous coordinates
 */
public class TransformationMatrix extends Matrix
{

    public TransformationMatrix()
    {
        super(4, 4);
        entries = Matrix.identity(4).entries;
    }


    /**
     * Adds a rotation transformation to the stack
     * Inspired by this: https://wikimedia.org/api/rest_v1/media/math/render/svg/41412701e4dd3dd0d85ad01077b00a882469687b
     * @param x Euler angle
     * @param y Euler angle
     * @param z Euler angle
     */
    public void addRotation(double x, double y, double z)
    {
        // Store these so we don't compute them every time
        double xCos = Math.cos(x), xSin = Math.sin(x);
        double yCos = Math.cos(y), ySin = Math.sin(y);
        double zCos = Math.cos(z), zSin = Math.sin(z);

        Matrix xRot = new Matrix(new double[][] {{ 1, 0, 0, 0 }, { 0, xCos, xSin, 0 }, { 0, -xSin, xCos, 0 }, { 0, 0, 0, 1 }});
        Matrix yRot = new Matrix(new double[][] {{ yCos, 0, -ySin, 0 }, { 0, 1, 0, 0 }, { ySin, 0, yCos, 0 }, { 0, 0, 0, 1 }});
        Matrix zRot = new Matrix(new double[][] {{ zCos, zSin, 0, 0 }, { -zSin, zCos,0 ,0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 }});

        entries = multiply(this, multiply(xRot, multiply(yRot, zRot))).entries;
    }


    public void addTranslation(double x, double y, double z)
    {
        Matrix trans = new Matrix(new double[][] {{ 1, 0, 0, x }, { 0, 1, 0, y}, { 0, 0, 1, z }, { 0, 0 ,0, 1 }});

        entries = multiply(this, trans).entries;
    }



}
