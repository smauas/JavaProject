package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.Intersectable.*;

import java.util.LinkedList;
import java.util.List;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    /**
     * Test method for {@link Plane#getNormal(Point3D)}
     */
    @Test
    void testGetNormal() {
        Plane p1 = new Plane(
                new Point3D(0.0, 1.0, 0.0),
                new Point3D(1.0, 0.0, 0.0),
                new Point3D(0.0, 0.0, 1.0));
        Vector v1 = p1.getNormal(new Point3D(0.0, 1.0, 0.0));

        Plane p2 = new Plane(
                new Point3D(0.0, 0.0, 1.0),
                new Point3D(1.0, 0.0, 0.0),
                new Point3D(0.0, 1.0, 0.0));
        Vector v2 = p2.getNormal(new Point3D(0.0, 1.0, 0.0));

        Plane p3 = new Plane(
                new Point3D(1.0, 0.0, 0.0),
                new Point3D(0.0, 0.0, 1.0),
                new Point3D(0.0, 1.0, 0.0));
        Vector v3 = p3.getNormal(new Point3D(0.0, 1.0, 0.0));

        assertNotEquals(v1, v2);
        assertEquals(v1, v3, "not same direction!");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(
                new Point3D(1.0, 0.0, 0.0),
                new Point3D(0.0, 1.0, 0.0),
                new Point3D(0.0, 0.0, 1.0));
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane (1 points)
        List<Point3D> points = new LinkedList<>();
        List<GeoPoint> results = plane.findIntersections(new Ray(new Point3D(-3, 0.0, 0), new Vector(6, 0, 0)));
        for (GeoPoint geo : results) {
            points.add(geo._point);
        }
        assertEquals(List.of(new Point3D(1, 0, 0)), points, "Ray did not intersect the plane as expected");

        // TC02: Ray doesn't intersect the plane (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(0.5, 0, 0), new Vector(-5, 0, 0))),
                "Ray intersect the Plane, not as expected! ");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray is parallel to the plane
        // TC03: Ray is include in the plane (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(0, 0, 1), new Vector(1, -1, 0))),
                "Ray intersect the Plane, not as expected! ");

        // TC04: Ray doesn't include in the plane (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(0.5, 0, -1), new Vector(1, -1, 0))),
                "Ray intersect the Plane, not as expected! ");

        // **** Group: Ray is orthogonal to the plane
        // TC05: P0 is before the Plane (1 points)
        results = plane.findIntersections(new Ray(new Point3D(-1, -1, -1),
                new Vector(0.5773502691896258, 0.5773502691896258, 0.5773502691896258)));
        points.clear();
        for (GeoPoint geo : results) {
            points.add(geo._point);
        }
        assertEquals(List.of(new Point3D(0.3333333333333335, 0.3333333333333335, 0.3333333333333335)), points,
                "Ray not intersected the plane as expected");
        // TC06: P0 is in the Plane (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(0, 0, 1),
                        new Vector(0.5773502691896258, 0.5773502691896258, 0.5773502691896258))),
                "Ray intersect the Plane, not as expected! ");
        // TC07: P0 is after the Plane (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(2, 2, 2),
                        new Vector(0.5773502691896258, 0.5773502691896258, 0.5773502691896258))),
                "Ray intersect the Plane, not as expected! ");

        // TC08: Ray is neither orthogonal nor parallel to the plane and begins at the plane
        //  P0 is in the plane, but not the ray (0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(0, 0, 1), new Vector(1, 0, 0))),
                "Ray intersect the Plane, not as expected! ");
        // TC09: Ray is neither orthogonal nor parallel to the plane and
        //  Ray begins in the same point which appears as reference point in the plane (Q)(0 points)
        assertNull(plane.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(1, 0, 0))),
                "Ray intersect the Plane, not as expected! ");
    }
}