import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable
{
    List<Hittable> objects = new ArrayList<>();

    public HittableList()
    {}

    public HittableList(Hittable object)
    {
        objects.add(object);
    }

    public void clear()
    {
        objects.clear();
    }

    public void add(Hittable object)
    {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord record)
    {
        HitRecord tempRecord = new HitRecord();
        boolean hitAnything = false;
        double closest = tMax;

        for (Hittable object : objects)
        {
            if (object.hit(r, tMin, closest, tempRecord))
            {
                hitAnything = true;
                closest = tempRecord.getT();
                record.setP(tempRecord.getP());
                record.setT(tempRecord.getT());
                record.setNormal(tempRecord.getNormal());
                record.setFrontFace(tempRecord.getFrontFace());
                record.setMat(tempRecord.getMat());
            }
        }

        return hitAnything;
    }

}
