package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {
    /**
     * Test method for {@link Tube#getNormal(Point3D)}
     */
    @Test
    void getNormal() {
        Point3D point1 = new Point3D(0.0, 0.0, 0.0);
        Vector vec1 = new Vector(1.0, 0.0, 0.0);
        Ray ray1 = new Ray(point1, vec1);
        double radius1 = 2;
        Tube tube1 = new Tube(radius1, ray1);
        Point3D p1 = new Point3D(10.0, 2.0, 0.0);
        Vector normal1 = tube1.getNormal(p1);

        Vector ExpResult1 = new Vector(0.0, 1.0, 0.0);
        assertEquals(normal1, ExpResult1);

        //orthogonal
        Point3D p2 = new Point3D(0.0, 2.0, 0.0);
        Vector normal2 = tube1.getNormal(p2);

        Vector ExpResult2 = new Vector(0.0, 1.0, 0.0);
        assertEquals(normal1, ExpResult1);


    }
}