package elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.*;
import geometries.*;
import java.util.List;

import geometries.Intersectable.GeoPoint;
class CameraIntegrationTest {
    /**
     * Test method for intersections between camera and geometries
     */
    Camera cam1 = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
    Camera cam2 = new Camera(new Point3D(0, 0, -0.5), new Vector(0, 0, 1), new Vector(0, -1, 0));

    /*********************************
     * test intersections Ray/Sphere *
     ********************************/
    @Test
    void constructRayThroughPixelWithSphere1() {
        Sphere sph = new Sphere(1, new Point3D(0, 0, 3));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = sph.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }

        assertEquals(2, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithSphere2() {
        Sphere sph = new Sphere(2.5, new Point3D(0, 0, 2.5));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = sph.findIntersections(cam2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(18, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithSphere3() {
        Sphere sph = new Sphere(2, new Point3D(0, 0, 2));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = sph.findIntersections(cam2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(10, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithSphere4() {
        Sphere sph = new Sphere(4, new Point3D(0, 0, 1));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = sph.findIntersections(cam2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(9, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithSphere5() {
        Sphere sph = new Sphere(0.5, new Point3D(0, 0, -1));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = sph.findIntersections(cam2.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(0, count, "too bad");
    }

    /*********************************
     * test intersections Ray/Plane *
     ********************************/
    @Test
    void constructRayThroughPixelWithPlane1() {
        Plane plane = new Plane(new Point3D(0, 0, 3), new Vector(0, 0, 1));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = plane.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(9, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithPlane2() {
        Plane plane = new Plane(new Point3D(0, 0, 2), new Vector(0, -0.5, 1));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = plane.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(9, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithPlane3() {
        Plane plane = new Plane(new Point3D(0, 0, 2), new Vector(0, -1, 1));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = plane.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(6, count, "too bad");
    }

    /***********************************
     * test intersections Ray/Triangle *
     ***********************************/
    @Test
    void constructRayThroughPixelWithTriangle1() {
        Triangle triangle = new Triangle(new Point3D(0, -1, 2),
                new Point3D(1, 1, 2),
                new Point3D(-1, 1, 2));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = triangle.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(1, count, "too bad");
    }

    @Test
    void constructRayThroughPixelWithTriangle2() {
        Triangle triangle = new Triangle(new Point3D(0, -20, 2),
                new Point3D(1, 1, 2),
                new Point3D(-1, 1, 2));
        List<GeoPoint> results;
        int count = 0;
        int Nx = 3;
        int Ny = 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                results = triangle.findIntersections(cam1.constructRayThroughPixel(Nx, Ny, j, i, 1, 3, 3));
                if (results != null)
                    count += results.size();
            }
        }
        assertEquals(2, count, "too bad");
    }
}