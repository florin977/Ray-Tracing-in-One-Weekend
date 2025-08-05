import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Camera 
{
    double aspectRatio = 16.0 / 9.0;
    int imageWidth = 400;
    private int imageHeight1;
    int imageHeight;

    int samplesPerPixel = 100;
    double sampleScale = 1.0 / samplesPerPixel;

    //double focalLength;

    double viewportHeight, viewportWidth;

    Vector3 viewportU, viewportV, deltaU, deltaV, viewportUpperLeft, upperLeftPixel;

    int maximumRecursionDepth = 50;
    
    Vector3 lookFrom = new Vector3(-2, 2, 1);
    Vector3 lookAt = new Vector3(0, 0, -1.0);
    Vector3 vUp = new Vector3(0, 1, 0);

    Vector3 center = lookFrom;

    double vfov = 20;
    Vector3 u, v, w;

    double defocusAngle = 10.0, focusDist = 3.4;
    Vector3 defocusDiskU, defocusDiskV;

    public void render(Vector3[][] buffer)
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
            myWriter.write(imageWidth + " " + imageHeight + "\n255\n");

            for (int j = 0; j < imageHeight; j++)
            {
                for (int i = 0; i < imageWidth; i++)
                {
                    Vector3.printColor(myWriter, Vector3.mul(buffer[j][i], sampleScale));
                }
            }
            
            System.out.println("\rDone.                   ");

            myWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void init()
    {
        sampleScale = 1.0 / samplesPerPixel;

        imageHeight1 = (int) (imageWidth / aspectRatio);
        imageHeight = (imageHeight1 < 1) ? 1 : imageHeight1;

        center = lookFrom;

        // Viewport size
        double theta = Utils.degreesToRadians(vfov);
        double h = Math.tan(theta / 2.0);

        viewportHeight = 2.0 * h  * focusDist;
        viewportWidth = viewportHeight * ((double) imageWidth / imageHeight);

        w = Vector3.sub(lookFrom, lookAt).unitVector();
        u = Vector3.cross(vUp, w).unitVector();
        v = Vector3.cross(w, u);

        viewportU = Vector3.mul(u, viewportWidth); // right
        viewportV = Vector3.mul(Vector3.mul(v, -1), viewportHeight); // down

        // Pixel delta vectors
        deltaU = Vector3.divide(viewportU, imageWidth);
        deltaV = Vector3.divide(viewportV, imageHeight);

        // Upper-left corner of the viewport (note subtracting viewportV/2 moves up)
        viewportUpperLeft = Vector3.sub(
                Vector3.sub(
                        Vector3.sub(center, Vector3.divide(viewportU, 2)),
                        Vector3.divide(viewportV, 2)),
                Vector3.mul(w, focusDist));

        // Pixel (0,0) center position (move half a pixel right and down)
        upperLeftPixel = Vector3.add(
                viewportUpperLeft,
                Vector3.add(
                        Vector3.mul(deltaU, 0.5),
                        Vector3.mul(deltaV, 0.5)));

        double defocusRadius = focusDist * Math.tan(Utils.degreesToRadians(defocusAngle / 2.0));

        defocusDiskU = Vector3.mul(u, defocusRadius);
        defocusDiskV = Vector3.mul(v, defocusRadius);
    }

    Ray getRay(int i, int j)
    {
        Vector3 offset = sampleSquare();
        Vector3 pixelSample = Vector3.add(upperLeftPixel, Vector3.add(Vector3.mul(deltaU, (offset.x + i)), Vector3.mul(deltaV, (offset.y + j))));

        Vector3 rayOrigin = (defocusAngle <= 0) ? center : defocusDiskSample();
        Vector3 rayDirection = Vector3.sub(pixelSample, rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    Vector3 sampleSquare()
    {
        return new Vector3(Math.random() - 0.5, Math.random() - 0.5, 0);
    }

    Vector3 defocusDiskSample()
    {
        Vector3 p = Utils.randomUnitInDisk();

        return Vector3.add(center, Vector3.add(Vector3.mul(defocusDiskU, p.x), Vector3.mul(defocusDiskV, p.y)));
    }

    public Vector3 rayColor(Ray r, int maximumRecursionDepth, Hittable world) 
    {
        if (maximumRecursionDepth <= 0)
        {
            return new Vector3(0, 0, 0);
        }

        HitRecord record = new HitRecord();

        if (world.hit(r, 0.001, Utils.INF, record)) 
        {
            Material.scatterResult scatterObj = new Material.scatterResult();

            if (record.mat.scatter(r, record, scatterObj))
            {
                return Vector3.mul(rayColor(scatterObj.scattered, maximumRecursionDepth - 1, world), scatterObj.attenuation);
            }

            return new Vector3(0, 0, 0);
        }

        Vector3 rayDir = r.getDirection();
        Vector3 unitVector = rayDir.unitVector();
        double a = 0.5 * (unitVector.y + 1.0);

        // basic lerp
        Vector3 minColor = Vector3.mul(new Vector3(1.0, 1.0, 1.0), (1.0 - a));
        Vector3 maxColor = Vector3.mul(new Vector3(0.5, 0.7, 1.0), a);
        
        Vector3 color = Vector3.add(minColor, maxColor);
        
        return color;
    }
}
