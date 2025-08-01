public class Utils 
{
    final static double INF = 1.79769313486231570e+308d;
    final static double PI = 3.1415926535897932385;

    double degreesToRadians(double degrees)
    {
        return degrees * PI / 180.0;
    }

    static double randomDouble(double min, double max)
    {
        double uniformRandom = Math.random();

        return min + (max - min) * uniformRandom;
    }

    static Vector3 randomVector()
    {
        return new Vector3(Math.random(), Math.random(), Math.random());
    }

    static Vector3 randomVector(double min, double max)
    {
        return new Vector3(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max));
    }

    static Vector3 randomUnitVector()
    {
        while(true)
        {
            Vector3 unitVector = randomVector(-1, 1);
            double lenSq = unitVector.lengthSquared();

            if (1e-160 < lenSq && lenSq <= 1)
            {
                return Vector3.divide(unitVector, Math.sqrt(lenSq));
            }
        }
    }

    static Vector3 randomOnHemisphere(Vector3 normal)
    {
        Vector3 unitVector = randomUnitVector();

        if (Vector3.dot(unitVector, normal) > 0.0)
        {
            return unitVector;
        }

        return Vector3.mul(unitVector, -1);
    }

    static boolean nearZero(Vector3 v)
    {
        double zero = 1e-8;

        return ((Math.abs(v.x) < zero) && (Math.abs(v.y) < zero) && (Math.abs(v.z) < zero));
    }

    static Vector3 reflect(Vector3 v, Vector3 normal)
    {
        double dotProduct = 2 * Vector3.dot(v, normal);

        return Vector3.sub(v, Vector3.mul(normal, dotProduct));
    }

    static Vector3 refract(Vector3 v, Vector3 normal, double refractionIndex)
    {
        double cosTheta = Math.min(1.0, Vector3.dot(Vector3.mul(v, -1), normal));
        Vector3 rOutPerpendicular = Vector3.mul(Vector3.add(v, Vector3.mul(normal, cosTheta)), refractionIndex);
        Vector3 rOutParallel = Vector3.mul(normal, -Math.sqrt(Math.abs(1.0 - rOutPerpendicular.lengthSquared())));

        return Vector3.add(rOutPerpendicular, rOutParallel);
    }

    static double clamp(double x, double min, double max)
    {
        if (x < min)
        {
            return min;
        }

        if (x > max)
        {
            return max;
        }

        return x;
    }

    static double linearToGamma(double component)
    {
        if (component > 0)
        {
            return Math.sqrt(component);
        }

        return 0;
    }
}
