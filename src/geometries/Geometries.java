package geometries;

import primitives.Point3D;
import primitives.Ray;

import javax.print.DocFlavor;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

/**
 * a class that implements Intersactable
 */
public class Geometries implements Intersectable {
    /**
     * Linked List of intersectables
     */
    private final List<Intersectable> _geometries = new LinkedList<>();

    /**
     * constructor
     *
     * @param _geometries
     */
    public Geometries(Intersectable... _geometries) {
        add(_geometries);
    }

    /**
     * method that adds the geometries to the list
     * @param geometries
     */
    public void add(Intersectable... geometries) {
        _geometries.addAll(Arrays.asList(geometries));
    }

    /**
     * find the intersections
     * @param ray the ray that intersect the geometries
     * @return list of Point3D that intersect the list
     * @author Dr Eliezer
     */
    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;

        for (Intersectable geo : _geometries) {
            List<GeoPoint> tempIntersections = geo.findIntersections(ray, maxDistance);
            if (tempIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(tempIntersections);
            }
        }
        return intersections;

    }

    /**
     * remove the some intersectables from the list
     *
     * @param intersectables
     */
    public void remove(Intersectable... intersectables) {
        for (Intersectable geo : intersectables) {
            _geometries.remove(geo);
        }
    }
//    /**
//     * remove the some intersectables from the list
//     * @param intersectables
//     */
//    public void remove(Intersectable... intersectables) {
//        for (Intersectable geo : _geometries) {
//            _geometries.remove(geo);
//        }
//    }
}