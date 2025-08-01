public class Test
{
    public static void main(String[] args)
    {
        HittableList world = new HittableList();
        Camera cam = new Camera();
        Material mat = new Material();

        Material ground = mat.new lambertian(new Vector3(0.8, 0.8, 0.0));
        Material centerSphereMat = mat.new lambertian(new Vector3(0.1, 0.2, 0.5));
        Material leftSphereMat = mat.new dielectric(1.50);
        Material rightSphereMat = mat.new metal(new Vector3(0.8, 0.6, 0.2), 1.0);
        
        world.add(new Sphere(new Vector3(0, 0, -1.2), 0.5, centerSphereMat));
        world.add(new Sphere(new Vector3(-1.0, 0, -1.0), 0.5, leftSphereMat));
        world.add(new Sphere(new Vector3(1.0, 0, -1.0), 0.5, rightSphereMat));
        world.add(new Sphere(new Vector3(0, -100.5, -1.0), 100, ground));
        
        cam.init();

        cam.render(world);
    }
}