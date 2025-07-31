public class Test
{
    public static void main(String[] args)
    {
        HittableList world = new HittableList();
        Camera cam = new Camera();
        
        world.add(new Sphere(new Vector3(0, 0, -1.0), 0.5));
        world.add(new Sphere(new Vector3(0, -100.5, -1.0), 100));
        
        cam.init();

        cam.render(world);
    }
}