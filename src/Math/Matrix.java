package Math;

public class Matrix
{

    /** Entries :: The entries in this Matrix **/
    protected double[][] entries;


    /** Constructors :: Lets us create a Matrix, defining its size and (optional) entries **/
    //region

    public Matrix(int row, int column)
    {
        this.entries = new double[row][column];
    }

    public Matrix(double[][] entries)
    {
        // Check that the array is a valid Matrix
        int size = entries[0].length;
        for (double[] a : entries) { if (a.length != size) { throw new IllegalArgumentException("Not a valid Matrix!"); } }

        this.entries = entries.clone();
    }

    //endregion


    /** Constants :: Easy & convenient way to create common vectors **/
    //region

    public static Matrix identity(int size) // A square Matrix with diagonal '1's
    {
        Matrix m = new Matrix(size, size);

        for (int i = 0; i < size; i++)
        {
            m.setEntry(i, i, 1);
        }

        return m;
    }

    //endregion


    /** Getters & Setters :: Lets us access & modify entries in a Matrix **/
    //region

    // Returns the entry at the given index
    public double getEntry(int row, int column) { return entries[row][column]; }

    // Sets the entry at the given index
    public void setEntry(int row, int column, double value) { entries[row][column] = value; }

    public double[][] getEntries()
    {
        return entries;
    }

    //endregion


    /** Operations **/
    //region

    // Multiplies Matrix 'a' by Matrix 'b' --> [a] * [b]
    public static Matrix multiply(Matrix a, Matrix b) throws IllegalArgumentException
    {
        if (a.columnCount() != b.rowCount()) { throw new IllegalArgumentException("Matrix a's columns MUST match b's rows"); }

        Matrix product = new Matrix(a.rowCount(), b.columnCount());


        for (int r = 0; r < product.rowCount(); r++)
        {
            for (int c = 0; c < product.columnCount(); c++)
            {
                double entry = 0;

                for (int i = 0; i < a.columnCount(); i++)
                {
                    entry += a.getEntry(r, i) * b.getEntry(i, c);
                }

                product.setEntry(r, c, entry);
            }
        }


        return product;

    }

    public static Matrix add(Matrix a, Matrix b) throws IllegalArgumentException
    {
        if (a.rowCount() != b.rowCount() || a.columnCount() != b.columnCount())
        { throw new IllegalArgumentException("Matrices must be of the same size!"); }

        Matrix sum = new Matrix(a.rowCount(), a.columnCount());

        for (int r = 0; r < a.rowCount(); r++)
        {
            for (int c = 0; c < a.columnCount(); c++)
            {
                sum.setEntry(r, c, a.getEntry(r, c) + b.getEntry(r, c));
            }
        }

        return sum;
    }

    //endregion


    /** Technical Stuff :: Makes the program not suck **/
    //region

    public int rowCount() { return entries.length; }
    public int columnCount() { return entries[0].length; }

    public Matrix clone() { return new Matrix(entries); }

    public String toString()
    {

        String s = "";

        for (int r = 0; r < rowCount(); r++)
        {
            s += "\n|";
            for (int c = 0; c < columnCount(); c++)
            {
                s += " " + getEntry(r, c) + " ";
            }
            s += "|";
        }

        return s;
    }

    //endregion
}
