public class Sphere extends Hittable 
{
    Vector3 center;
    double radius;

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord record)
    {
        Vector3 oc = Vector3.sub(center, r.getOrigin());
        double a = r.getDirection().norm();
        double h = Vector3.dot(r.getDirection(), oc);
        double c = oc.norm() - radius * radius;
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

            if (solution <= tMin || tMax >= solution)
            {
                return false;
            }
        }

        record.t = solution;
        record.p = r.at(record.t);
        Vector3 outwardNormal = Vector3.divide(Vector3.sub(record.p, center), radius);
        record.setFaceNormal(r, outwardNormal);

        return true;
    }
}
