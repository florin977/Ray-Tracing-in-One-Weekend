public class Utils 
{
    final static double INF = 1.79769313486231570e+308d;
    final static double PI = 3.1415926535897932385;

    double degreesToRadians(double degrees)
    {
        return degrees * PI / 180.0;
    }

    double randomDouble(double min, double max)
    {
        double uniformRandom = Math.random();

        return min + (max - min) * uniformRandom;
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
}
