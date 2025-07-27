public class Camera {
    double aspectRatio = 16.0 / 9.0;
    int imageWidth = 400;
    private int imageHeight1 = (int)(imageWidth / aspectRatio);
    int imageHeight = (imageHeight1 < 1) ? 1 : imageHeight1;

    double focalLength = 1.0;

    Vector3 center = new Vector3(0, 0, 0);

    // Viewport size
    double viewportHeight = 2.0;
    double viewportWidth = viewportHeight * ((double)imageWidth / imageHeight);

    // Horizontal and vertical viewport vectors
    // Here viewportV points down (negative Y), to match C++ example
    Vector3 viewportU = new Vector3(viewportWidth, 0, 0);    // right
    Vector3 viewportV = new Vector3(0, -viewportHeight, 0);  // down

    // Pixel delta vectors
    Vector3 deltaU = Vector3.divide(viewportU, imageWidth);
    Vector3 deltaV = Vector3.divide(viewportV, imageHeight);

    // Upper-left corner of the viewport (note subtracting viewportV/2 moves up)
    Vector3 viewportUpperLeft = Vector3.sub(
        Vector3.sub(
            Vector3.sub(center, Vector3.divide(viewportU, 2)),
            Vector3.divide(viewportV, 2)
        ),
        new Vector3(0, 0, focalLength)
    );

    // Pixel (0,0) center position (move half a pixel right and down)
    Vector3 upperLeftPixel = Vector3.add(
        viewportUpperLeft,
        Vector3.add(
            Vector3.mul(deltaU, 0.5),
            Vector3.mul(deltaV, 0.5)
        )
    );
}
