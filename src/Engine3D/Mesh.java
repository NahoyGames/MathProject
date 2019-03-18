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

    // Stores the indices of the vertex normals used in a triangular face on a mesh
    private int[] normalIndices;

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

    public int[] getNormalIndices()
    {
        return normalIndices;
    }

    public void setNormalIndices(int[] normalIndices)
    {
        this.normalIndices = normalIndices;
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
        List<Integer> currentNormalIndices = new ArrayList<>();
        List<Vector3> currentNormals = new ArrayList<>();

        int vertexOffset = 0;
        int normalOffset = 0;

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
                            int[] temp2 = new int[currentNormalIndices.size()];
                            for (int i = 0; i < temp.length; i++)
                            {
                                temp[i] = currentFaces.get(i);
                                temp2[i] = currentNormalIndices.get(i);
                            }
                            lastMesh.setFaces(temp);
                            lastMesh.setNormalIndices(temp2);

                            vertexOffset += currentVertices.size();
                            normalOffset += currentNormals.size();

                            currentFaces.clear();
                            currentNormalIndices.clear();
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

                            /*for (int s = 0; s < f.length; s += 2)
                            {
                                currentFaces.add(Integer.parseInt(f[s]));
                                currentNormalIndices.add(Integer.parseInt(f[s + ((f.length / 3) - 1)]));
                            }*/

                            if (f.length == 1) // v1 v2 v3
                            {
                                currentFaces.add(Integer.parseInt(f[0]) - vertexOffset);
                            }
                            else if (f.length == 2) // v1//vn1 v2//vn2 v3//vn3
                            {
                                currentFaces.add(Integer.parseInt(f[0]) - vertexOffset);
                                currentNormalIndices.add(Integer.parseInt(f[1]) - normalOffset);
                            }
                            else if (f.length == 3) // v1/vt1/vn1 ...
                            {
                                currentFaces.add(Integer.parseInt(f[0]) - vertexOffset);
                                currentNormalIndices.add(Integer.parseInt(f[2]) - normalOffset);
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
            int[] temp2 = new int[currentNormalIndices.size()];
            for (int i = 0; i < temp.length; i++)
            {
                temp[i] = currentFaces.get(i);
                temp2[i] = currentNormalIndices.get(i);
            }
            lastMesh.setFaces(temp);
            lastMesh.setNormalIndices(temp2);

            currentFaces.clear();
            currentNormalIndices.clear();
            currentNormals.clear();
            currentVertices.clear();

        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(meshes.toArray(new Mesh[meshes.size()])));


        return meshes.toArray(new Mesh[meshes.size()]);

    }

    //endregion


    /** Technical Stuff :: Makes the program not suck **/
    //region

    public String toString()
    {
        return "\n\n\"" + name + "\" ::: \nVertices : \n" + Arrays.toString(vertices) +
                "\n Vertex Normals : \n" + Arrays.toString(vNormals) +
                "\n Faces : \n" + Arrays.toString(faces) +
                "\n Normal Indices : \n" + Arrays.toString(normalIndices);
    }

    //endregion
}
