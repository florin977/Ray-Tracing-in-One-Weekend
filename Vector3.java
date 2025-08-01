import java.io.FileWriter;
import java.io.IOException;

public class Vector3 
{
    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 add(Vector3 v, Vector3 adder)
    {
        return new Vector3(v.x + adder.x, v.y + adder.y, v.z + adder.z);
    }

    public static Vector3 sub(Vector3 v, Vector3 subtract)
    {
        return new Vector3(v.x - subtract.x, v.y - subtract.y, v.z - subtract.z);
    }
    
    public static Vector3 mul(Vector3 v, double op)
    {
        return new Vector3(v.x * op, v.y * op, v.z * op);
    }

    public static Vector3 mul(Vector3 v, Vector3 u)
    {
        return new Vector3(v.x * u.x, v.y * u.y, v.z * u.z);
    }

    public static Vector3 divide(Vector3 v, double div)
    {
        return new Vector3(v.x * (1.0 / div), v.y * (1.0 / div), v.z * (1.0 / div));
    }

    public double length()
    {
        return Math.sqrt(lengthSquared());
    }
    
    public double lengthSquared()
    {
        Vector3 aux = new Vector3(this.x * this.x, this.y * this.y, this.z * this.z);

        return aux.x + aux.y + aux.z;
    }

    public static double dot(Vector3 a, Vector3 b)
    {
        return (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    public static Vector3 cross(Vector3 a, Vector3 b)
    {
        return new Vector3(a.y * b.z - a.z * b.y,
                           a.z * b.x - a.x * b.z,
                           a.x * b.y - a.y * b.x);
    }

    public Vector3 unitVector()
    {
        return divide(this, this.length());
    }

    public static void print(FileWriter writer, Vector3 v)
    {
        try
        {
            writer.write(v.x + " " + v.y + " " + v.z + '\n');
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void printColor(FileWriter writer, Vector3 v)
    {
        try
        {
            double r = Utils.clamp(v.x, 0, 0.999);
            double g = Utils.clamp(v.y, 0, 0.999);
            double b = Utils.clamp(v.z, 0, 0.999);

            r = Utils.linearToGamma(r);
            g = Utils.linearToGamma(g);
            b = Utils.linearToGamma(b);
            
            writer.write((int)(r * 256) + " " + (int)(g * 256) + " " + (int)(b * 256) + '\n');
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}