public class Material 
{
    
    Vector3 albedo;

    public static class scatterResult
    {
        Vector3 attenuation;
        Ray scattered;

        scatterResult()
        {}
    }

    public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj)
    {
        return false;
    }   
    
    class lambertian extends Material
    {
        lambertian(Vector3 newAlbedo)
        {
            this.albedo = newAlbedo;
        }

        @Override
        public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj)
        {
            Vector3 scatterDirection = Vector3.add(record.normal, Utils.randomUnitVector());

            if (Utils.nearZero(scatterDirection))
            {
                scatterDirection = record.normal;
            }

            scatterObj.scattered = new Ray(record.p, scatterDirection);
            scatterObj.attenuation = albedo;

            return true;
        }
    }

    class metal extends Material
    {
        double fuzz;

        metal(Vector3 newAlbedo, double newFuzz)
        {
            this.albedo = newAlbedo;
            this.fuzz = newFuzz;
        }

        @Override
        public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj)
        {
            Vector3 reflected = Utils.reflect(rayIn.getDirection(), record.normal);
            reflected = Vector3.add(reflected.unitVector(), Vector3.mul(Utils.randomUnitVector(), fuzz));

            scatterObj.scattered = new Ray(record.p, reflected);
            scatterObj.attenuation = albedo;

            return (Vector3.dot(scatterObj.scattered.getDirection(), record.normal) > 0);
        }
    }

    class dielectric extends Material
    {
        double refractionIndex;
        
        dielectric(double newRefractionIndex)
        {
            this.refractionIndex = newRefractionIndex;
        }

        @Override
        public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj)
        {
            scatterObj.attenuation = new Vector3(1.0, 1.0, 1.0);
            double ri = record.frontFace ? (1.0 / refractionIndex) : refractionIndex;

            Vector3 unitVector = rayIn.getDirection().unitVector();
            Vector3 refracted = Utils.refract(unitVector, record.normal, ri);

            scatterObj.scattered = new Ray(record.p, refracted);

            return true;
        }
    }
}
