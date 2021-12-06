package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {
    /**
     * Test method for {@link Cylinder#getNormal(Point3D)}
     */
    @Test
    void getNormal() {

        Point3D point = new Point3D(0.0, 0.0, 0.0);
        Vector vector = new Vector(1.0, 0.0, 0.0);
        Ray ray = new Ray(point, vector);
        double radius = 2;
        double height = 2;
        Cylinder cylinder = new Cylinder(radius, ray, height);

        // ============ Equivalence Partitions Tests ==============

        // point is on the side
        Point3D p1 = new Point3D(1.0, 2.0, 0.0);
        Vector normal1 = cylinder.getNormal(p1);
        Vector ExpResult1 = new Vector(0.0, 1.0, 0.0);
        assertEquals(normal1, ExpResult1);

        // point is on the base beside the origin
        Point3D p2 = new Point3D(0.0, 1.5, 0.5);
        Vector normal2 = cylinder.getNormal(p2);
        Vector ExpResult2 = new Vector(1.0, 0.0, 0.0);
        assertEquals(normal2, ExpResult2);

        // point is on the other base of the Cylinder
        Point3D p3 = new Point3D(2.0, 0.5, 1.5);
        Vector normal3 = cylinder.getNormal(p3);
        Vector ExpResult3 = new Vector(1.0, 0.0, 0.0);
        assertEquals(normal3, ExpResult3);

        // =============== Boundary Values Tests ==================

        // point is on the base beside the origin on the limit with the side of the Cylinder
        Point3D p4 = new Point3D(0.0, 2.0, 0.0);
        Vector normal4 = cylinder.getNormal(p4);
        Vector ExpResult4 = new Vector(1.0, 0.0, 0.0);
        assertEquals(normal4, ExpResult4);

        // point is on the other base of the Cylinder on the limit with the side of the Cylinder
        Point3D p5 = new Point3D(2.0, 2.0, 0.0);
        Vector normal5 = cylinder.getNormal(p5);
        Vector ExpResult5 = new Vector(1.0, 0.0, 0.0);
        assertEquals(normal5, ExpResult5);

    }
}