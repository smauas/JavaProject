package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Cylinder is a finite Tube with a certain height
 */
public class Cylinder extends Tube {
    /**
     * the height of the cylinder
     */
    private final double _height;

    /**
     * Cylinder constructor
     *
     * @param _radius רradius of the Cylinder
     * @param _ray    direction and reference point  of the cylinder
     * @param _height height of the cylinder (from the referenced point)
     */
    public Cylinder(double _radius, Ray _ray, double _height) {
        super(_radius, _ray);
        this._height = _height;
    }

    /**
     * get the height of the Cylinder
     *
     * @return a double of the height
     */
    public double get_height() {
        return _height;
    }

    /**
     * @param point point to calculate the normal
     * @return normal
     */
    @Override
    public Vector getNormal(Point3D point) {
        Point3D o = _ray.get_origin();
        Vector v = _ray.get_vector();

        // projection of P-O on the ray:
        double t;
        try {
            t = alignZero(point.subtract(o).dotProduct(v));
        } catch (IllegalArgumentException e) { // P = O
            return v;
        }

        // if the point is at a base
        if (t == 0 || isZero(_height - t)) // if it's close to 0, we'll get ZERO vector exception
            return v;

        o = o.add(v.scale(t));
        return point.subtract(o).normalize();
    }

    /**
     * find the intersections
     * @param ray ray pointing toward a Geometry
     * @return a list of GeoPoints
     */
    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        List<GeoPoint> intersections = super.findIntersections(ray);
        if (intersections != null) {
            for (GeoPoint geoPoint : intersections) {
                geoPoint._geometry = this;
            }
        }
        return intersections;
    }
}