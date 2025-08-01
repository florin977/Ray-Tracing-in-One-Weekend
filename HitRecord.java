public class HitRecord 
{
    Vector3 p;
    Vector3 normal;
    double t;
    boolean frontFace;
    Material mat;

    public HitRecord() 
    {}

    public void setFaceNormal(Ray r, Vector3 outwardNormal) 
    {
        Vector3 rDirection = r.getDirection();
        this.frontFace = (Vector3.dot(rDirection, outwardNormal)) < 0.0;
        this.normal = frontFace ? outwardNormal : Vector3.mul(outwardNormal, -1);
    }
}
