public class HitRecord 
{
    Vector3 p;
    Vector3 normal;
    double t;
    boolean frontFace;

    public HitRecord() 
    {}

    public void setFaceNormal(Ray r, Vector3 outwardNormal) 
    {
        frontFace = (Vector3.dot(r.getDirection(), outwardNormal)) < 0;
        normal = frontFace ? normal : Vector3.mul(normal, -1);
    }
}
