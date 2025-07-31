import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Camera 
{
    double aspectRatio = 16.0 / 9.0;
    int imageWidth = 400;
    private int imageHeight1;
    int imageHeight;

    int samplesPerPixel;
    double sampleScale;

    double focalLength;
    Vector3 center;

    double viewportHeight, viewportWidth;

    Vector3 viewportU, viewportV, deltaU, deltaV, viewportUpperLeft, upperLeftPixel;

    public void render(HittableList world)
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
                    Vector3 pixelColor = new Vector3(0, 0, 0);
                    
                    for (int sample = 0; sample < samplesPerPixel; sample++)
                    {
                        Ray r = getRay(i, j);
                        pixelColor = Vector3.add(pixelColor, rayColor(r, world));
                    }

                    Vector3.printColor(myWriter, Vector3.mul(pixelColor, sampleScale));
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

    public void init()
    {
        samplesPerPixel = 100;
        sampleScale = 1.0 / samplesPerPixel;

        imageHeight1 = (int) (imageWidth / aspectRatio);
        imageHeight = (imageHeight1 < 1) ? 1 : imageHeight1;

        focalLength = 1.0;

        center = new Vector3(0, 0, 0);

        // Viewport size
        viewportHeight = 2.0;
        viewportWidth = viewportHeight * ((double) imageWidth / imageHeight);

        viewportU = new Vector3(viewportWidth, 0, 0); // right
        viewportV = new Vector3(0, -viewportHeight, 0); // down

        // Pixel delta vectors
        deltaU = Vector3.divide(viewportU, imageWidth);
        deltaV = Vector3.divide(viewportV, imageHeight);

        // Upper-left corner of the viewport (note subtracting viewportV/2 moves up)
        viewportUpperLeft = Vector3.sub(
                Vector3.sub(
                        Vector3.sub(center, Vector3.divide(viewportU, 2)),
                        Vector3.divide(viewportV, 2)),
                new Vector3(0, 0, focalLength));

        // Pixel (0,0) center position (move half a pixel right and down)
        upperLeftPixel = Vector3.add(
                viewportUpperLeft,
                Vector3.add(
                        Vector3.mul(deltaU, 0.5),
                        Vector3.mul(deltaV, 0.5)));

    }

    Ray getRay(int i, int j)
    {
        Vector3 offset = sampleSquare();
        Vector3 pixelSample = Vector3.add(upperLeftPixel, Vector3.add(Vector3.mul(deltaU, (offset.x + i)), Vector3.mul(deltaV, (offset.y + j))));

        Vector3 rayOrigin = center;
        Vector3 rayDirection = Vector3.sub(pixelSample, rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    Vector3 sampleSquare()
    {
        return new Vector3(Math.random() - 0.5, Math.random() - 0.5, 0);
    }

    public static Vector3 rayColor(Ray r, Hittable world) 
    {
        HitRecord record = new HitRecord();

        if (world.hit(r, 0, Utils.INF, record)) 
        {
            Vector3 color1 = Vector3.add(record.normal, new Vector3(1, 1, 1));
            Vector3 color = Vector3.mul(color1, 0.5);

            return color;
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
