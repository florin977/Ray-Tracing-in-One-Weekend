import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test
{
    
    public static double hitSphere(Vector3 center, double radius, Ray r)
    {
        Vector3 oc = Vector3.sub(center, r.getOrigin());
        double a = r.getDirection().norm();
        double h = Vector3.dot(r.getDirection(), oc);
        double c = oc.norm() - radius * radius;
        double delta = h * h - a * c;

        if (delta < 0)
        {
            return -1.0;
        }

        double solution = (h - Math.sqrt(delta)) / a;
        return solution;
    }

    public static Vector3 rayColor(Ray r, Hittable world)
    {/*
        HitRecord record = new HitRecord();
        if (world.hit(r, 0, Constants.INF, record))
        {
            return Vector3.mul(Vector3.add(record.normal, new Vector3(1, 1, 1)), 0.5);
        }
 */
        Vector3 C = new Vector3(0, 0, -1.0);

        double t = hitSphere(C, 0.5, r);
        
        if (t > 0.0)
        {
            Vector3 P = r.at(t);
            Vector3 N = Vector3.sub(P, C);
            N = N.unitVector();

            return Vector3.mul(Vector3.add(N, new Vector3(1, 1, 1)), 0.5);
        }
        
        Vector3 unitVector = r.direction.unitVector();
        double a = 0.5 * (unitVector.y + 1.0);

        // basic lerp
        Vector3 color = Vector3.add(Vector3.mul(new Vector3(1.0, 1.0, 1.0), (1.0 - a)), Vector3.mul(new Vector3(0.5, 0.7, 1.0), a));
        return color;
    }

    static Camera cam = new Camera();

    public static void main(String[] args)
    {
        try 
        {
            File myFile = new File("ray.ppm");

            if (myFile.createNewFile())
            {
                System.out.println("File created: " + myFile.getName());
            }
            else 
            {
                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter("ray.ppm");

            myWriter.write("P3\n");
            myWriter.write(cam.imageWidth + " " + cam.imageHeight + "\n255\n");

            for (int i = 0; i < cam.imageHeight; i++)
            {
                for (int j = 0; j < cam.imageWidth; j++)
                {
                    Vector3 pixelCenter = Vector3.add(cam.upperLeftPixel, Vector3.add(Vector3.mul(cam.deltaU, j), Vector3.mul(cam.deltaV, i)));
                    Vector3 rayDirection = Vector3.sub(pixelCenter, cam.center);
                    Ray r = new Ray(cam.center, rayDirection);

                    Vector3 pixelColor = rayColor(r, null);

                    Vector3.printColor(myWriter, pixelColor);
                }
            }

            myWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}