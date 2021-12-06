package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import geometries.Intersectable.*;

import static primitives.Util.isZero;

/**
 * The triangle class
 */
public class Triangle extends Polygon {

    /**
     * constructor
     *
     * @param emissionLight
     * @param material
     * @param p1
     * @param p2
     * @param p3
     */
    public Triangle(Color emissionLight, Material material, Point3D p1, Point3D p2, Point3D p3) {
        super(emissionLight, material, p1, p2, p3);
    }

    /**
     * constructor
     *
     * @param emissionLight
     * @param p1
     * @param p2
     * @param p3
     */
    public Triangle(Color emissionLight, Point3D p1, Point3D p2, Point3D p3) {
        super(emissionLight, p1, p2, p3);
    }

    /**
     * constructor
     *
     * @param p1
     * @param p2
     * @param p3
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }


    @Override
    public String toString() {
        return "Triangle vertices:" + _vertices + ", plane" + _plane + '.';
    }

    @Override
    public Vector getNormal(Point3D point) {
        return super.getNormal(point);
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        List<GeoPoint> intersections = _plane.findIntersections(ray);
        if (intersections == null) return null;

        Point3D p0 = ray.get_origin();
        Vector v = ray.get_vector();

        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);

        double s1 = v.dotProduct(v1.crossProduct(v2));
        if (isZero(s1)) return null;
        double s2 = v.dotProduct(v2.crossProduct(v3));
        if (isZero(s2)) return null;
        double s3 = v.dotProduct(v3.crossProduct(v1));
        if (isZero(s3)) return null;

        return ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) ? intersections : null;

    }
}
