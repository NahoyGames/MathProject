package Engine3D;

import Math.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * A container which encapsulates mesh objects and offers numerous other functions
 * such as .obj parsing
 */
public class Mesh
{

    /** Data :: Stores the information for this mesh**/
    //region

    private String srcFile;

    private String name;

    // An array of position vectors representing the mesh's vertices, in **local space**
    private Vector3[] vertices;

    // Stores the indices of the vertices that make up a triangular face on a mesh
    private int[] faces;

    // Indicates in which direction each vector is facing
    private Vector3[] vNormals;

    //endregion


    /** Constructor :: Lets us initialize a mesh **/
    //region

    // Generates the mesh from a .obj file
    public Mesh(String objFile, String name, Vector3[] vertices, int[] faces, Vector3[] vertexNormals)
    {
        this.srcFile = objFile;
        this.name = name;
        this.vertices = vertices;
        this.faces = faces;
        this.vNormals = vertexNormals;
    }

    //endregion


    /** Getters & Setters :: Lets us access & modify data **/
    //region

    public void setVertices(Vector3[] vertices)
    {
        this.vertices = vertices;
    }

    public Vector3[] getVertices()
    {
        return vertices;
    }

    public int[] getFaces()
    {
        return faces;
    }

    public Vector3[] getVertexNormals()
    {
        return vNormals;
    }

    public void setFaces(int[] faces)
    {
        this.faces = faces;
    }

    public void setVertexNormals(Vector3[] vNormals)
    {
        this.vNormals = vNormals;
    }

    //endregion


    /** Parsers :: Parses standard 3D Model formats to the one used for this program **/
    //region

    // Parses a .obj file. I won't explain how this works too much in depth, but if you
    // right click --> "open with text edit" an .obj file, you might get a glimpse of what
    // this code is doing.
    public static Mesh[] parseObj(String src)
    {
        List<Mesh> meshes = new ArrayList<>();

        List<Vector3> currentVertices = new ArrayList<>();
        List<Integer> currentFaces = new ArrayList<>();
        List<Vector3> currentNormals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(src)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] args = line.split("\\s+"); // Splits the line by spaces

                // Declaration : Defines what this line is, ie. 'v' = vertex or 'f' face
                switch (args[0])
                {
                    case "v": // Vertex
                        currentVertices.add(new Vector3(
                                Double.parseDouble(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3])
                        ));
                        break;

                    case "o": // Mesh Object
                        try
                        {
                            Mesh lastMesh = meshes.get(meshes.size() - 1);

                            lastMesh.setVertices(currentVertices.toArray(new Vector3[currentVertices.size()]));
                            lastMesh.setVertexNormals(currentNormals.toArray(new Vector3[currentNormals.size()]));
                            int[] temp = new int[currentFaces.size()];
                            for (int i = 0; i < temp.length; i++)
                            {
                                temp[i] = currentFaces.get(i);
                            }
                            lastMesh.setFaces(temp);

                            currentFaces.clear();
                            currentNormals.clear();
                            currentVertices.clear();
                        }
                        catch (IndexOutOfBoundsException e) { }

                        meshes.add(new Mesh(src, args[1], null, null, null));
                        break;

                    case "f": // Face

                        for (int i = 1; i < args.length; i++)
                        {
                            String[] f = args[i].replaceAll("//", "/").split("/");

                            for (String s : f)
                            {
                                currentFaces.add(Integer.parseInt(s));
                            }
                        }
                        break;

                    case "vn": // Vertex Normal
                        currentNormals.add(new Vector3(
                                Double.parseDouble(args[1]),
                                Double.parseDouble(args[2]),
                                Double.parseDouble(args[3])
                        ));
                        break;

                    default: // Unimplemented
                        break;
                }
            }

            Mesh lastMesh = meshes.get(meshes.size() - 1);

            lastMesh.setVertices(currentVertices.toArray(new Vector3[currentVertices.size()]));
            lastMesh.setVertexNormals(currentNormals.toArray(new Vector3[currentNormals.size()]));
            int[] temp = new int[currentFaces.size()];
            for (int i = 0; i < temp.length; i++)
            {
                temp[i] = currentFaces.get(i);
            }
            lastMesh.setFaces(temp);

            currentFaces.clear();
            currentNormals.clear();
            currentVertices.clear();

        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        return meshes.toArray(new Mesh[meshes.size()]);

    }

    //endregion


    /** Technical Stuff :: Makes the program not suck **/
    //region

    public String toString()
    {
        return "\n\n\"" + name + "\" ::: \nVertices : \n" + Arrays.toString(vertices) +
                "\n Vertex Normals : \n" + Arrays.toString(vNormals) +
                "\n Faces : \n" + Arrays.toString(faces);
    }

    //endregion
}
