# Ray Tracing in One Weekend (Java)

This is a Java implementation of the **Ray Tracing in One Weekend** series by Peter Shirley.  
The project recreates the fundamental concepts of ray tracing in a clean, object-oriented, and **multithreaded** way using Java.

## Features

- **Basic Ray Tracing**: Implements primary rays and simple Lambertian diffuse surfaces.
- **Spheres and Materials**: Supports multiple spheres with different materials (diffuse, metal, dielectric).
- **Camera**: Perspective camera with configurable viewport, field of view, and aspect ratio.
- **Anti-Aliasing**: Simple stochastic sampling to reduce jagged edges.
- **Recursive Reflections**: Handles reflections and refractions recursively for realistic effects.
- **Multithreading**: Uses multiple threads (`RenderTask`) to divide the image into segments and render in parallel, significantly improving performance on multi-core CPUs.
- **PPM Output**: Generates rendered images as `.ppm` files.

## Getting Started

### Prerequisites

- Java JDK 17 or newer
- A terminal or IDE capable of running Java applications

### Running the Project

1. Clone the repository:
    ```bash
    git clone https://github.com/florin977/Ray-Tracing-in-One-Weekend.git
    cd Ray-Tracing-in-One-Weekend
    ```

2. Compile the Java files:
    ```bash
    javac *.java
    ```

3. Run the main program:
    ```bash
    java Main
    ```

4. The rendered image will be output as `ray.ppm` in the project folder.  
   You can view `.ppm` files with many image viewers or convert them to `.png` using tools like [ImageMagick](https://imagemagick.org/):
    ```bash
    convert ray.ppm ray.png
    ```

---

## Project Structure

Ray-Tracing-in-One-Weekend/
│
├─ Main.java # Entry point of the program; sets up the scene, camera, and triggers the multithreaded render
├─ Camera.java # Defines the virtual camera, ray generation, depth of field, and image coordinate mapping
├─ Hittable.java # Interface for any object that can be intersected ("hit") by a ray
├─ Sphere.java # Implements the Hittable interface for rendering spheres with various materials
├─ Material.java # Abstract base class defining how materials interact with light (diffuse, reflective, refractive)
├─ Ray.java # Represents a ray with an origin and direction, and computes points along its path
├─ Vector3.java # 3D vector math utilities (addition, subtraction, dot/cross product, normalization, etc.)
├─ HitRecord.java # Stores details about ray-object intersections: position, normal, material, and t-value
├─ HittableList.java # A collection of Hittable objects; tests ray intersections across all scene objects
├─ RenderTask.java # Handles a portion of the image in a separate thread to enable parallel rendering
└─ Utils.java # Helper functions for random sampling, color clamping, and numerical utilities

## Multithreading

Rendering high-quality images is computationally expensive.  
To speed up the process, this implementation divides the image into **independent segments** handled by multiple `RenderTask` threads.  

Each thread computes its assigned pixels in parallel, and when all threads finish, the results are combined into the final image.  
This approach allows near-linear scaling with the number of available CPU cores.

## Example ray.ppm for 50 samplesPerPixel and 16 maximumRecursionDepth

![Ray.ppm output example](ray.ppm)

## References

- [Ray Tracing in One Weekend](https://raytracing.github.io/books/RayTracingInOneWeekend.html) by Peter Shirley  
- [PPM File Format](https://netpbm.sourceforge.net/doc/ppm.html)


*Built for learning and experimentation in computer graphics, OOP, parallel programming, and physically based rendering.*
