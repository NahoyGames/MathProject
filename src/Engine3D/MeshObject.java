package Engine3D;

import Math.*;

import java.util.Arrays;


/***
 * A single, solid, set of vertices and faces.
 */
public class MeshObject
{

    /** Data :: Stores the information for this mesh object**/
    //region

    private Mesh parentMesh;

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

    // Generates the mesh from a given set of vertices and faces
    public MeshObject(Mesh parentMesh, String name, Vector3[] vertices, int[] faces, Vector3[] vertexNormals)
    {
        this.parentMesh = parentMesh;
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

    public void setFaces(int[] faces)
    {
        this.faces = faces;
    }

    public void setVertexNormals(Vector3[] vNormals)
    {
        this.vNormals = vNormals;
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
