public class Ray 
{
    public Vector3 origin;
    public Vector3 direction;

    public Ray(Vector3 newOrigin, Vector3 newDirection)
    {
        this.origin = newOrigin;
        this.direction = newDirection;
    }

    public Vector3 getOrigin()
    {
        return this.origin;
    }

    public Vector3 getDirection()
    {
        return this.direction;
    }

    public Vector3 at(double t)
    {
        return Vector3.add(origin, Vector3.mul(direction, t));
    }
}
