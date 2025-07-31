public class Sphere extends Hittable 
{
    Vector3 center;
    double radius;

    public Sphere(Vector3 newCenter, double newRadius)
    {
        this.center = newCenter;
        this.radius = newRadius;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord record)
    {
        Vector3 rOrigin = r.getOrigin();
        Vector3 rDirection = r.getDirection();

        Vector3 oc = Vector3.sub(center, rOrigin);
        double a = rDirection.lengthSquared();
        double h = Vector3.dot(rDirection, oc);
        double c = oc.lengthSquared() - radius * radius;
        double delta = h * h - a * c;

        if (delta < 0)
        {
            return false;
        }

        double sqrtd = Math.sqrt(delta);
        double solution = (h - sqrtd) / a;

        if (solution <= tMin || solution >= tMax)
        {
            solution = (h + sqrtd) / a;

            if (solution <= tMin || solution >= tMax)
            {
                return false;
            }
        }

        record.t = solution;
        record.p = r.at(solution);
        Vector3 nonUnitNormal = Vector3.sub(record.p, center);
        Vector3 outwardNormal = Vector3.divide(nonUnitNormal, radius);
        record.setFaceNormal(r, outwardNormal);

        return true;
    }
}
