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

    public static Vector3 divide(Vector3 v, double div)
    {
        return new Vector3(v.x / div, v.y / div, v.z / div);
    }

    public static double dot(Vector3 a, Vector3 b)
    {
        return (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    public double norm()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 unitVector()
    {
        return divide(this, this.norm());
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
            writer.write((int)(v.x * 255.99) + " " + (int)(v.y * 255.99) + " " + (int)(v.z * 255.99) + '\n');
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}