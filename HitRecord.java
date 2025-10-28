public class HitRecord 
{
    private Vector3 p;
    private Vector3 normal;
    private double t;
    private boolean frontFace;
    private Material mat;

    public Vector3 getP()
    {
        return p;
    }

    public Vector3 getNormal()
    {
        return normal;
    }

    public Material getMat()
    {
        return mat;
    }
    
    public double getT()
    {
        return t;
    }

    public boolean getFrontFace()
    {
        return frontFace;
    }

     public void setP(Vector3 p)
    {
        this.p = p;
    }

    public void setNormal(Vector3 normal)
    {
         this.normal = normal;
    }

    public void setMat(Material mat)
    {
        this.mat = mat;
    }
    
    public void setT(double t)
    {
        this.t = t;
    }

    public void setFrontFace(boolean frontFace)
    {
        this.frontFace = frontFace;
    }
    
    public HitRecord() 
    {}

    public void setFaceNormal(Ray r, Vector3 outwardNormal) 
    {
        Vector3 rDirection = r.getDirection();
        this.frontFace = (Vector3.dot(rDirection, outwardNormal)) < 0.0;
        this.normal = frontFace ? outwardNormal : Vector3.mul(outwardNormal, -1);
    }
}
