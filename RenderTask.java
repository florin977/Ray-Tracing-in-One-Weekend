public class RenderTask extends Thread
{
    int numOfThreads;
    Camera cam;
    HittableList world;
    int startY, endY, threadID;
    Vector3[][] buffer;

    RenderTask(int newNumOfThreads, int newStartY, int newEndY, Camera newCam, HittableList newWorld, Vector3[][] newBuffer, int newThreadID)
    {
        this.numOfThreads = newNumOfThreads;
        this.startY = newStartY;
        this.endY = newEndY;
        this.cam = newCam;
        this.world = newWorld;
        this.buffer = newBuffer;
        this.threadID = newThreadID;
    }

    public void run()
    {
        int printDebug = 0;

        for (int j = startY; j < endY; j++)
        {
            if (printDebug % 10 == 0)
            {
                System.err.println("Thread " + threadID + ": Scanlines remaining: " + (endY - j) + " ");
                System.err.flush();
            }

            printDebug++;

            for (int i = 0; i < cam.imageWidth; i++)
            {
                Vector3 pixelColor = new Vector3(0, 0, 0);
                        
                for (int sample = 0; sample < cam.samplesPerPixel; sample++)
                {
                    Ray r = cam.getRay(i, j);
                    pixelColor = Vector3.add(pixelColor, cam.rayColor(r, cam.maximumRecursionDepth, world));
                }

                buffer[j][i] = pixelColor;
            }
        }
        
        System.err.println("\rDone.                   ");
    }
}
