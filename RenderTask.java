import java.util.concurrent.atomic.AtomicInteger;

public class RenderTask extends Thread
{
    private Camera cam;
    private HittableList world;
    private AtomicInteger pixelIndex;
    private Vector3[][] buffer;

    RenderTask(AtomicInteger newPixelIndex, Camera newCam, HittableList newWorld, Vector3[][] newBuffer, int newThreadID)
    {
        this.pixelIndex = newPixelIndex;
        this.cam = newCam;
        this.world = newWorld;
        this.buffer = newBuffer;
    }

    public void run()
    {
        long startTime = System.currentTimeMillis();
        int totalPixels = cam.getImageHeight() * cam.getImageWidth();
        
        while (true)
        {
            int remainingPixels = totalPixels - pixelIndex.get();

            if (remainingPixels % 10000 == 0)
            {
                double currentTime = (System.currentTimeMillis() - startTime) / 1000.0;
                double timePerPixel = (totalPixels == remainingPixels) ? 0 : currentTime / (totalPixels - remainingPixels);

                System.out.print("\rPixels reamining: " + remainingPixels + "   Remaining time: " + (int)(remainingPixels * timePerPixel));
                System.out.print("\rPixels reamining: " + remainingPixels);
            }

            int currentPixelIndex = pixelIndex.getAndIncrement();

            if (currentPixelIndex >= totalPixels)
            {
                break;
            }

            int i = currentPixelIndex % cam.getImageWidth();
            int j = currentPixelIndex / cam.getImageWidth();

            Vector3 pixelColor = new Vector3(0, 0, 0);
                       
            for (int sample = 0; sample < cam.getSamplesPerPixel(); sample++)
            {
                Ray r = cam.getRay(i, j);
                pixelColor = Vector3.add(pixelColor, cam.rayColor(r, cam.getMaximumRecursionDepth(), world));
            }

            buffer[j][i] = pixelColor;
        }
    }
}
