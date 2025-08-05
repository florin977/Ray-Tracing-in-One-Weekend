import java.util.concurrent.atomic.AtomicInteger;

public class Test
{
    public static void main(String[] args)
    {
        HittableList world = new HittableList();
        Camera cam = new Camera();
        Material mat = new Material();

        Material groundMat = mat.new lambertian(new Vector3(0.5, 0.5, 0.5));
        
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
                        Material lambertianMat = mat.new lambertian(albedo);
                        
                        world.add(new Sphere(center, 0.2, lambertianMat));
                    }
                    else if (chooseMat <= 0.95)
                    {
                        // Metal
                        Vector3 albedo = Utils.randomVector(0.5, 1);
                        double fuzz = Utils.randomDouble(0, 0.5);
                        Material metalMat = mat.new metal(albedo, fuzz);
                        
                        world.add(new Sphere(center, 0.2, metalMat));
                    }
                    else
                    {
                        // Glass
                        Material glassMat = mat.new dielectric(1.5);

                        world.add(new Sphere(center, 0.2, glassMat));
                    }
                }
            }
        }

        Material middleSphereMat = mat.new dielectric(1.5);
        Material leftSphereMat = mat.new lambertian(new Vector3(0.4, 0.2, 0.1));
        Material rightSphereMat = mat.new metal(new Vector3(0.7, 0.6, 0.5), 0.0);

        world.add(new Sphere(new Vector3(0, 1, 0), 1.0, middleSphereMat));
        world.add(new Sphere(new Vector3(-4, 1, 0), 1.0, leftSphereMat));
        world.add(new Sphere(new Vector3(4, 1, 0), 1.0, rightSphereMat));

        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 1920;
        cam.samplesPerPixel = 200;
        cam.maximumRecursionDepth = 16;

        cam.vfov = 20;
        cam.lookFrom = new Vector3(13, 2, 3);
        cam.lookAt = new Vector3(0, 0, 0);
        cam.vUp = new Vector3(0, 1, 0);

        cam.defocusAngle = 0.6;
        cam.focusDist = 10.0;

    /* 
        // Camera debug
        Material leftMat = mat.new lambertian(new Vector3(1, 0, 0));
        Material rightMat = mat.new lambertian(new Vector3(0, 0, 1));
        
        double R = Math.cos(Utils.PI / 4.0);

        world.add(new Sphere(new Vector3(-R, 0, -1.0), R, leftMat));
        world.add(new Sphere(new Vector3(R, 0, -1.0), R, rightMat));
    */

        cam.init();

        Vector3[][] buffer = new Vector3[cam.imageHeight][cam.imageWidth];
        
        int freeThreads = 0;
        int numOfThreads = Runtime.getRuntime().availableProcessors() - freeThreads;
        RenderTask[] threads = new RenderTask[numOfThreads];

        long startTime = System.currentTimeMillis();

        AtomicInteger pixelIndex = new AtomicInteger(0);

        for (int k = 0; k < numOfThreads; k++)
        {
            threads[k] = new RenderTask(numOfThreads, pixelIndex, cam, world, buffer, k);
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