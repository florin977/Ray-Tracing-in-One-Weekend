public class Material {
    private Vector3 albedo;

    public Vector3 getAlbedo() {
        return albedo;
    }

    public void setAlbedo(Vector3 albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj) {
        return false;
    }

}

class scatterResult {
    private Vector3 attenuation;
    private Ray scattered;

    public Ray getScattered() {
        return scattered;
    }

    public Vector3 getAttenuation() {
        return attenuation;
    }

    public void setScattered(Ray scattered) {
        this.scattered = scattered;
    }

    public void setAttenuation(Vector3 attentuation) {
        this.attenuation = attentuation;
    }

    scatterResult() {
    }
}

class lambertian extends Material {
    lambertian(Vector3 newAlbedo) {
        setAlbedo(newAlbedo);
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj) {
        Vector3 scatterDirection = Vector3.add(record.getNormal(), Utils.randomUnitVector());

        if (Utils.nearZero(scatterDirection)) {
            scatterDirection = record.getNormal();
        }

        scatterObj.setScattered(new Ray(record.getP(), scatterDirection));
        scatterObj.setAttenuation(getAlbedo());

        return true;
    }
}

class metal extends Material {
    private double fuzz;

    metal(Vector3 newAlbedo, double newFuzz) {
        setAlbedo(newAlbedo);
        this.fuzz = newFuzz;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj) {
        Vector3 reflected = Utils.reflect(rayIn.getDirection(), record.getNormal());
        reflected = Vector3.add(reflected.unitVector(), Vector3.mul(Utils.randomUnitVector(), fuzz));

        scatterObj.setScattered(new Ray(record.getP(), reflected));
        scatterObj.setAttenuation(getAlbedo());

        return (Vector3.dot(scatterObj.getScattered().getDirection(), record.getNormal()) > 0);
    }
}

class dielectric extends Material {
    private double refractionIndex;

    dielectric(double newRefractionIndex) {
        this.refractionIndex = newRefractionIndex;
    }

    static double reflectance(double cos, double refractionIndex) {
        double r0 = (1 - refractionIndex) / (1 + refractionIndex);
        r0 = r0 * r0;

        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord record, scatterResult scatterObj) {
        scatterObj.setAttenuation(new Vector3(1.0, 1.0, 1.0));
        double ri = record.getFrontFace() ? (1.0 / refractionIndex) : refractionIndex;

        Vector3 unitVector = rayIn.getDirection().unitVector();

        double cosTheta = Math.min(Vector3.dot(Vector3.mul(unitVector, -1.0), record.getNormal()), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

        boolean cannotRefract = ri * sinTheta > 1.0;

        Vector3 direction = new Vector3(0, 0, 0);

        if (cannotRefract || reflectance(cosTheta, ri) > Math.random()) {
            direction = Utils.reflect(unitVector, record.getNormal());
        } else {
            direction = Utils.refract(unitVector, record.getNormal(), ri);
        }

        scatterObj.setScattered(new Ray(record.getP(), direction));

        return true;
    }
}