package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import geometries.Intersectable.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

class TriangleTest {

    /**
     * Test method for {@link Triangle#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        Point3D p1 = new Point3D(1, 2, 3);
        Point3D p2 = new Point3D(1, 3, 2);
        Point3D p3 = new Point3D(2, 1, 3);
        Triangle triangle = new Triangle(p1, p2, p3);

        Vector vec = new Vector(-0.5773502691896258, -0.5773502691896258, -0.5773502691896258);
        Vector ExpResult = new Vector(triangle.getNormal(p1));
        assertEquals(ExpResult, vec);

    }

    @Test
    void findIntersections() {
        Triangle triangle = new Triangle(
                new Point3D(1.0, 1.0, 0.0),
                new Point3D(0.0, 1.0, 0.0),
                new Point3D(0.0, 1.0, 1.0));
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects inside the triangle (1 points)
        List<Point3D> points = new LinkedList<>();
        List<GeoPoint> results = triangle.findIntersections(new Ray(new Point3D(0.1, -3, 0.1), new Vector(0.0, 1.0, 0.0)));
        for (GeoPoint geo : results) {
            points.add(geo._point);
        }
        assertEquals(List.of(new Point3D(0.1, 1.0, 0.1)), points,
                "Ray not intersected the triangle as expected");

        // TC02: Ray intersects outside against edge of the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(-0.5, 1, -0.5))),
                "Ray intersect the triangle, not as expected! ");

        // TC03: Ray intersects outside against vertex of the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(1, 1, -0.1))),
                "Ray intersect the triangle, not as expected! ");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray intersects the bound of the triangle

        // TC04: Ray intersects an edge of the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point3D(1, 0, 1), new Vector(-0.5, 1, -0.5))),
                "Ray intersect the triangle, not as expected! ");

        // TC05: Ray intersects a vertex of the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(-1, 1, 0))),
                "Ray intersect the triangle, not as expected! ");

        // TC06: Ray intersects an  edge's continuation of the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(1, 1, 0))),
                "Ray intersect the triangle, not as expected! ");

    }
}