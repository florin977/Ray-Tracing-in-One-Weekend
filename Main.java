import java.util.concurrent.atomic.AtomicInteger;

public class Main
{
    public static void main(String[] args)
    {
        HittableList world = new HittableList();
        Camera cam = new Camera();
        
        generateRandomSpheres(world);
        setUpCamera(cam);
        beginRendering(world, cam);
    }

    public static void generateRandomSpheres(HittableList world)
    {
        Material groundMat = new lambertian(new Vector3(0.5, 0.5, 0.5));
        
        world.add(new Sphere(new Vector3(0, -1000, 0), 1000, groundMat));

        for (int a = -11; a < 11; a++)
        {
            for (int b = -11; b < 11; b++)
            {
                double chooseMat = Math.random();
                Vector3 center = new Vector3(a + 0.9 * Math.random(), 0.2, b + 0.9 * Math.random());

                if (Vector3.sub(center, new Vector3(4, 0.4, 0)).length() > 0.9)
                {
                    if (chooseMat <= 0.8)
                    {
                        // Diffuse
                        Vector3 albedo = Vector3.mul(Utils.randomVector(), Utils.randomVector());
                        Material lambertianMat = new lambertian(albedo);
                        
                        world.add(new Sphere(center, 0.2, lambertianMat));
                    }
                    else if (chooseMat <= 0.95)
                    {
                        // Metal
                        Vector3 albedo = Utils.randomVector(0.5, 1);
                        double fuzz = Utils.randomDouble(0, 0.5);
                        Material metalMat = new metal(albedo, fuzz);
                        
                        world.add(new Sphere(center, 0.2, metalMat));
                    }
                    else
                    {
                        // Glass
                        Material glassMat = new dielectric(1.5);

                        world.add(new Sphere(center, 0.2, glassMat));
                    }
                }
            }
        }

        Material middleSphereMat = new dielectric(1.5);
        Material leftSphereMat = new lambertian(new Vector3(0.4, 0.2, 0.1));
        Material rightSphereMat = new metal(new Vector3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vector3(0, 1, 0), 1.0, middleSphereMat));
        world.add(new Sphere(new Vector3(-4, 1, 0), 1.0, leftSphereMat));
        world.add(new Sphere(new Vector3(4, 1, 0), 1.0, rightSphereMat));

    }

    public static void setUpCamera(Camera cam)
    {
        cam.setAspectRatio(16.0 / 9.0);
        cam.setImageWidth(1920);
        cam.setSamplesPerPixel(50);
        cam.setMaximumRecursionDepth(16);

        cam.setVfov(20);
        cam.setLookFrom(new Vector3(13, 2, 3));
        cam.setLookAt(new Vector3(0, 0, 0));
        cam.setVup(new Vector3(0, 1, 0));

        cam.setDefocusAngle(0.6);
        cam.setFocusDist(10.0);

        cam.init();
    }

    public static void beginRendering(HittableList world, Camera cam)
    {
        Vector3[][] buffer = new Vector3[cam.getImageHeight()][cam.getImageWidth()];
        
        int freeThreads = 0;
        int numOfThreads = Runtime.getRuntime().availableProcessors() - freeThreads;
        RenderTask[] threads = new RenderTask[numOfThreads];

        long startTime = System.currentTimeMillis();

        AtomicInteger pixelIndex = new AtomicInteger(0);

        for (int k = 0; k < numOfThreads; k++)
        {
            threads[k] = new RenderTask(pixelIndex, cam, world, buffer, k);
            threads[k].start();
        }

        for (int k = 0; k < numOfThreads; k++)
        {
            try
            {
                threads[k].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        cam.render(buffer);
        
        long stopTime = System.currentTimeMillis();

        System.out.println("Total time: " + (stopTime - startTime) / 1000);
    }
}