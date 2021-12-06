package geometries;

import org.junit.jupiter.api.Test;

import primitives.*;
import geometries.*;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.System.out;

class GeometriesTest {
    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}
     */
    @Test
    public void findIntersections() {
        Geometries forms = new Geometries(
                new Plane(new Point3D(2, 0, 0), new Vector(-1, 1, 0)),
                new Sphere(2d, new Point3D(5, 0, 0)),
                new Triangle(new Point3D(8.5, -1, 0), new Point3D(7.5, 1.5, 1), new Point3D(7.5, 1.5, -1))
        );
        Ray ray;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some geo intersect
        ray = new Ray(new Point3D(1, 0, 0), new Vector(7, 3, 0));
        assertEquals(3, forms.findIntersections(ray).size(), "wrong intersections");

        // =============== Boundary Values Tests ==================
        // TC02: Empty collection
        ray = new Ray(new Point3D(1, 0, 0), new Vector(1, 0, 0));
        assertNull(new Geometries().findIntersections(ray), "It is empty!");

        // TC03: None geo intersect
        ray = new Ray(new Point3D(1, 0, 0), new Vector(1, 3, 0));
        assertNull(forms.findIntersections(ray), "wrong intersections");

        // TC04: Single geo intersect
        ray = new Ray(new Point3D(1, 0, 0), new Vector(4, 3, 0));
        assertEquals(1, forms.findIntersections(ray).size(), "wrong intersections");

        // TC05: All geo intersect
        ray = new Ray(new Point3D(1, 0, 0), new Vector(7, 1, 0));
        assertEquals(4, forms.findIntersections(ray).size(), "wrong intersections");
    }
}