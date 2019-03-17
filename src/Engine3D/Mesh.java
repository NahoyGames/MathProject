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

    private MeshObject[] objects;

    //endregion


    /** Constructor :: Lets us initialize a mesh **/
    //region

    // Generates the mesh from a .obj file
    public Mesh(String objFile)
    {
        this.srcFile = objFile;
        this.objects = parseObj(objFile);
    }

    //endregion


    /** Getters & Setters :: Lets us access & modify data **/
    //region

    public MeshObject[] getObjects()
    {
        return objects;
    }

    //endregion


    /** Parsers :: Parses standard 3D Model formats to the one used for this program **/
    //region

    // Parses a .obj file. I won't explain how this works too much in depth, but if you
    // right click --> "open with text edit" an .obj file, you might get a glimpse of what
    // this code is doing.
    private MeshObject[] parseObj(String src)
    {
        List<MeshObject> objects = new ArrayList<>();

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
                            MeshObject lastObj = objects.get(objects.size() - 1);

                            lastObj.setVertices(currentVertices.toArray(new Vector3[currentVertices.size()]));
                            lastObj.setVertexNormals(currentNormals.toArray(new Vector3[currentNormals.size()]));
                            int[] temp = new int[currentFaces.size()];
                            for (int i = 0; i < temp.length; i++)
                            {
                                temp[i] = currentFaces.get(i);
                            }
                            lastObj.setFaces(temp);

                            currentFaces.clear();
                            currentNormals.clear();
                            currentVertices.clear();
                        }
                        catch (IndexOutOfBoundsException e) { }

                        objects.add(new MeshObject(this, args[1], null, null, null));
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

            MeshObject lastObj = objects.get(objects.size() - 1);

            lastObj.setVertices(currentVertices.toArray(new Vector3[currentVertices.size()]));
            lastObj.setVertexNormals(currentNormals.toArray(new Vector3[currentNormals.size()]));
            int[] temp = new int[currentFaces.size()];
            for (int i = 0; i < temp.length; i++)
            {
                temp[i] = currentFaces.get(i);
            }
            lastObj.setFaces(temp);

            currentFaces.clear();
            currentNormals.clear();
            currentVertices.clear();

        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        return objects.toArray(new MeshObject[objects.size()]);

    }

    //endregion


    /** Technical Stuff :: Makes the program not suck **/
    //region

    public String toString()
    {
        return Arrays.toString(this.objects);
    }

    //endregion
}
