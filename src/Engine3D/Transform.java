package Engine3D;

import Math.*;

public class Transform
{
    public Vector3 position;
    public Vector3 rotation;

    public Transform(Vector3 position, Vector3 rotation)
    {
        this.position = position;
        this.rotation = rotation;
    }

    public Transform()
    {
        this(Vector3.zero(), Vector3.zero());
    }

}
