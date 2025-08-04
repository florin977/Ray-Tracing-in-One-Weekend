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

        static double reflectance(double cos, double refractionIndex)
        {
            double r0 = (1 - refractionIndex) / (1 + refractionIndex);
            r0 = r0 * r0;

            return r0 + (1 - r0) * Math.pow((1 - cos), 5);
        }

        @Override
        public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj)
        {
            scatterObj.attenuation = new Vector3(1.0, 1.0, 1.0);
            double ri = record.frontFace ? (1.0 / refractionIndex) : refractionIndex;

            Vector3 unitVector = rayIn.getDirection().unitVector();

                
            double cosTheta = Math.min(Vector3.dot(Vector3.mul(unitVector, -1.0), record.normal), 1.0);
            double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

            boolean cannotRefract = ri * sinTheta > 1.0;

            Vector3 direction = new Vector3(0, 0, 0);

            if (cannotRefract || reflectance(cosTheta, ri) > Math.random())
            {
                direction = Utils.reflect(unitVector, record.normal);
            }
            else 
            {
                direction = Utils.refract(unitVector, record.normal, ri);
            }

            scatterObj.scattered = new Ray(record.p, direction);

            return true;
        }
    }
}
