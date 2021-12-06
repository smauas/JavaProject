package renderer;

import elements.*;
import geometries.*;
//import org.junit.jupiter.api.Disabled;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;

import static geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class render the scene
 */
public class Render {
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final boolean SOFT_SHADOW_ACTIVE = false;
    private static final int SOFT_SHADOW_SIZE_RAYS = 50;
    private static final double SOFT_SHADOW_RADIUS = 0.05;
    private final ImageWriter _imageWriter;
    private final Scene _scene;
    private final int SPARE_THREADS = 2;
    private double _supersamplingDensity = 0d;
    private int _rayCounter = 1;
    private int _threads = 1;
    private boolean _print = false;
    /**
     * threshold for color difference (the percentage is the value / 255)
     */
    private static final double COLOR_DIFFERENCE_THRESHOLD = 20;
    /**
     * the rendered scene background color
     */
    Color backgroundColor;
    /**
     * number of pixel rows in the picture
     */
    int nX;
    /**
     * number of pixel columns in the picture
     */
    int nY;
    /**
     * the distance of the view plane from the lens
     */
    double screenDistance;
    /**
     * height of the view plane
     */
    double screenHeight;
    /**
     * width of the view plane
     */
    double screenWidth;
//    /**the actual amount of processors in the computer */
//    int actualCores;
    /**
     * boolean flag for rendering with or without adaptive sampling
     */
    boolean _adaptiveSampling;

    /**
     * constructor that gets the image writer and the scene objects
     *
     * @param imageWriter
     * @param scene
     */
    public Render(ImageWriter imageWriter, Scene scene) {
        _imageWriter = imageWriter;
        _scene = scene;
        _adaptiveSampling = false;

        // number of rows
        nX = _imageWriter.getNx();
        // number of columns
        nY = _imageWriter.getNy();
        screenDistance = _scene.getDistance();
        screenHeight = _imageWriter.getHeight();
        screenWidth = _imageWriter.getWidth();
        backgroundColor = _scene.getBackground();
//        //get the number of cores in the running machine
//        actualCores = Runtime.getRuntime().availableProcessors();
    }

    /**
     * constructor that gets the image writer and the scene objects and flag for
     * adaptive sampling
     *
     * @param imageWriter
     * @param scene
     * @param adaptiveSampling - implementing supersampling adaptive or not
     */
    public Render(ImageWriter imageWriter, Scene scene, boolean adaptiveSampling) {

        _adaptiveSampling = adaptiveSampling;
        _imageWriter = imageWriter;
        _scene = scene;
        // number of rows
        nX = _imageWriter.getNx();
        // number of columns
        nY = _imageWriter.getNy();
        screenDistance = _scene.getDistance();
        screenHeight = _imageWriter.getHeight();
        screenWidth = _imageWriter.getWidth();
        backgroundColor = _scene.getBackground();
//        //get the number of cores in the running machine
//        actualCores = Runtime.getRuntime().availableProcessors();
    }

    /**
     * Set the SuperSampling density
     *
     * @param supersamplingDensity
     * @return Render
     * @author Dr Eliezer
     */
    public Render setSupersamplingDensity(double supersamplingDensity) {
        _supersamplingDensity = supersamplingDensity;
        return this;
    }

    /**
     * get the ray counter
     * @return int of the ray counter
     */
    public int getRayCounter() {
        return _rayCounter;
    }

    /**
     * set the ray counter
     *
     * @param rayCounter
     * @return Render
     */
    public Render setRayCounter(int rayCounter) {
        _rayCounter = rayCounter;
        return this;
    }

    /**
     * print the grid
     *
     * @param interval
     * @param color
     */
    public void printGrid(int interval, java.awt.Color color) {
        int Nx = _imageWriter.getNx();
        int Ny = _imageWriter.getNy();
        for (int i = 0; i < Ny; i++) {
            for (int j = 0; j < Nx; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    _imageWriter.writePixel(j, i, color);
                }
            }
        }
    }

    /**
     * this function write to the image
     */
    public void writeToImage() {
        _imageWriter.writeToImage();
    }

    /**
     * render the image with multi-threading
     */
    public void renderImage2() {
        Camera camera = _scene.getCamera();
        //Intersectable geometries = _scene.getGeometries();
        Color background = _scene.getBackground();
        //AmbientLight ambientLight = _scene.getAmbientLight();
        double distance = _scene.getDistance();

        int Nx = _imageWriter.getNx();
        int Ny = _imageWriter.getNy();
        double width = _imageWriter.getWidth();
        double height = _imageWriter.getHeight();

        final Pixel thePixel = new Pixel(Ny, Nx);

        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                Color resultingColor;
                while (thePixel.nextPixel(pixel)) {
                    if (_supersamplingDensity == 0d) {//         without supersampling
                        resultingColor = getPixelRayColor(camera, background, distance, Nx, Ny, width, height, pixel);
                    } else {
                        resultingColor = getPixelRaysBeamColor(camera, distance, Nx, Ny, width, height, pixel);
                    }
                    _imageWriter.writePixel(pixel.col, pixel.row, resultingColor.getColor());
                }
            });
        }
        // Start threads
        for (Thread thread : threads) thread.start();
        // Wait for all threads to finish
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception e) {
            }
        if (_print) System.out.printf("\r100%%\n");
    }

    /**
     * render the image with multi-threading
     */
    public void renderImage() {
        Camera camera = _scene.getCamera();
        //Intersectable geometries = _scene.getGeometries();
        Color background = _scene.getBackground();
        //AmbientLight ambientLight = _scene.getAmbientLight();
        double distance = _scene.getDistance();

        int Nx = _imageWriter.getNx();
        int Ny = _imageWriter.getNy();
        double width = _imageWriter.getWidth();
        double height = _imageWriter.getHeight();

        final Pixel thePixel = new Pixel(Ny, Nx);

        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                Color resultingColor;
                while (thePixel.nextPixel(pixel)) {
//                    if (_supersamplingDensity == 0d) {//         without supersampling
//                        resultingColor = getPixelRayColor(camera, background, distance, Nx, Ny, width, height, pixel);
//                    }
                    if (_adaptiveSampling == false) {
                        resultingColor = getPixelRaysBeamColor(camera, distance, Nx, Ny, width, height, pixel);
                    }
//                    if (_adaptiveSampling==false) {//         without adaptivesampling
//                        resultingColor = getPixelRayColor(camera, background, distance, Nx, Ny, width, height, pixel);
//                    }
                    else {
                        resultingColor = pixelColorByAdaptiveSampling(pixel.col, pixel.row);
                    }
                    _imageWriter.writePixel(pixel.col, pixel.row, resultingColor.getColor());
                }
            });
        }
        // Start threads
        for (Thread thread : threads) thread.start();
        // Wait for all threads to finish
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (Exception e) {
            }
        if (_print) System.out.printf("\r100%%\n");
    }

    /**
     * get pixel rays beam color
     *
     * @param camera
     * @param distance
     * @param nx
     * @param ny
     * @param width
     * @param height
     * @param pixel
     * @return the color
     * @author Dr Eliezer
     */
    private Color getPixelRaysBeamColor(Camera camera, double distance, int nx, int ny, double width, double height, Pixel pixel) {
        List<Ray> rays = camera.constructRayBeamThroughPixel(nx, ny, pixel.col, pixel.row, distance, width, height, _supersamplingDensity, _rayCounter);
        Color resultColor = calcColor(rays);
        resultColor = resultColor.add(_scene.getAmbientLight().getIntensity());
        return resultColor;
    }

    /**
     * get the pixel ray color
     * @param camera
     * @param background
     * @param distance
     * @param nx
     * @param ny
     * @param width
     * @param height
     * @param pixel
     * @return the resulting Color
     */
    private Color getPixelRayColor(Camera camera, Color background, double distance, int nx, int ny, double width, double height, Pixel pixel) {
        Ray ray = camera.constructRayThroughPixel(nx, ny, pixel.col, pixel.row, distance, width, height);
        GeoPoint closestPoint = findClosestIntersection(ray);
        Color resultingColor = background;
        if (closestPoint != null) {
            resultingColor = calcColor(closestPoint, ray);
        }
        resultingColor = resultingColor.add(_scene.getAmbientLight().getIntensity());
        return resultingColor;
    }


    /**
     * Set multithreading <br>
     * - if the parameter is 0 - number of coress less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            _threads = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     *
     * @return the Render object itself
     */
    public Render setDebugPrint() {
        _print = true;
        return this;
    }

    /**
     * Find intersections of a ray with the scene geometries and get the
     * intersection point that is closest to the ray head. If there are no
     * intersections, null will be returned.
     *
     * @param ray intersecting the scene
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {

        if (ray == null) {
            return null;
        }

        GeoPoint closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        Point3D ray_p0 = ray.get_origin();

        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(ray);
        if (intersections == null)
            return null;

        for (GeoPoint geoPoint : intersections) {
            double distance = ray_p0.distance(geoPoint.getPoint());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = geoPoint;
            }
        }
        return closestPoint;
    }

    /**
     * this calcColor method receives a list of rays and send to the other
     * CalcColor method to calculate the Color of each one
     * @param inRays List of surrounding rays
     * @return average color
     */
    private Color calcColor(List<Ray> inRays) {
        Color bkg = _scene.getBackground();
        Color color = Color.BLACK;
        for (Ray ray : inRays) {
            GeoPoint gp = findClosestIntersection(ray);
            if (gp == null) {
                color = color.add(bkg);
            } else {
                color = color.add(calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, 1d));
            }
        }
//        color = color.add(_scene.getAmbientLight().getIntensity());
        int size = inRays.size();
        return (size == 1) ? color : color.reduce(size);
    }

    /**
     * calculate the color with a geopoint and a ray
     * @param geoPoint
     * @param inRay
     * @return the calculate color
     */
    private Color calcColor(GeoPoint geoPoint, Ray inRay) {
        Color color = calcColor(geoPoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0);
//        color = color.add(_scene.getAmbientLight().getIntensity());
        return color;
    }

    /**
     * @param geoPoint
     * @param inRay
     * @param level
     * @param k
     * @return
     */
    private Color calcColor(GeoPoint geoPoint, Ray inRay, int level, double k) {
        if (level == 1) {
            return Color.BLACK;
        }

        Point3D pointGeo = geoPoint.getPoint();
        Geometry geometryGeo = geoPoint.getGeometry();
        Color color = geometryGeo.getEmissionLight();
        Material material = geometryGeo.getMaterial();
        int nShininess = material.getnShininess();
        double kd = material.getkD();
        double ks = material.getkS();

        Vector v = pointGeo.subtract(_scene.getCamera().getP0()).normalize();
        Vector n = geometryGeo.getNormal(pointGeo);

        color = getColorLightSources(geoPoint, k, color, v, n, nShininess, kd, ks);

        double kr = geometryGeo.getMaterial().getKr();
        double kkr = k * kr;

        if (kkr > MIN_CALC_COLOR_K) {
            color = getColorSecondaryRay(level, color, kr, kkr, constructReflectedRay(pointGeo, inRay, n));
        }

        double kt = geometryGeo.getMaterial().getKt();
        double kkt = k * kt;

        if (kkt > MIN_CALC_COLOR_K) {
            color = getColorSecondaryRay(level, color, kt, kkt, constructRefractedRay(pointGeo, inRay, n));
        }
        return color;
    }

    /**
     * the recursive method to calculate the next ray
     * @param level
     * @param color
     * @param krt
     * @param kkrt
     * @param secondaryRay
     * @return the calculate color
     */
    private Color getColorSecondaryRay(int level, Color color, double krt, double kkrt, Ray secondaryRay) {
        GeoPoint geoPoint = findClosestIntersection(secondaryRay);
        if (geoPoint != null) {
            color = color.add(calcColor(geoPoint, secondaryRay, level - 1, kkrt).scale(krt));
        }
        return color;
    }

    /**
     * @param geoPoint
     * @param k
     * @param color
     * @param v
     * @param n
     * @param nShininess
     * @param kd
     * @param ks
     * @return the color
     */
    private Color getColorLightSources(GeoPoint geoPoint, double k, Color color, Vector v, Vector n, int nShininess, double kd, double ks) {
        Point3D pointGeo = geoPoint.getPoint();
        if (_scene.getLightSources() != null) {
            for (LightSource lightSource : _scene.getLightSources()) {
                Vector l = lightSource.getL(pointGeo);
                double nl = n.dotProduct(l);
                double nv = n.dotProduct(v);
                double ktr;
                if (nl * nv > 0) {
//              if( nl != 0 && nv != 0 && sign(nv)==sign(nl) ) {
//                if (unshaded(lightSource, l, n, geoPoint)) {
//                    ktr = 1d;
//                } else {
//                    ktr = 0d;
//                }
                    ktr = transparency(lightSource, l, n, geoPoint);
                    if (ktr * k > MIN_CALC_COLOR_K && !isZero(nl)) {
                        Color lightIntensity = lightSource.getIntensity(pointGeo).scale(ktr);
                        color = color.add(
                                calcDiffusive(kd, nl, lightIntensity),
                                calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
                    }
                }
            }
        }
        return color;
    }

    /**
     * Construct the Refracted Ray
     *
     * @param pointGeo the point3D of the geopoint
     * @param inRay    the ray
     * @param n        normal
     * @return
     */
    private Ray constructRefractedRay(Point3D pointGeo, Ray inRay, Vector n) {
        return new Ray(pointGeo, inRay.get_vector(), n);
    }

    /**
     * this method has to construct the reflected ray
     * @param pointGeo
     * @param inRay
     * @param n
     * @return The Reflected Ray
     */
    private Ray constructReflectedRay(Point3D pointGeo, Ray inRay, Vector n) {

        Vector v = inRay.get_vector();
        double vn = v.dotProduct(n);

        if (isZero(vn)){
            return null;
        }
        // reflected = v -( 2 * (v * n )* n)
        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(pointGeo, r, n);
    }

    /**
     * Calculate Specular component of light reflection.
     *
     * @param ks         specular component coef
     * @param l          direction from light to point
     * @param n          normal to surface at the point
     * @param nl         dot-product n*l
     * @param V          direction from point of view to point
     * @param nShininess shininess level
     * @param ip         light intensity at the point
     * @return specular component light effect at the point
     * @author Dan Zilberstein ( modified by me)
     * <p>
     * Specular light is light from a point light source which will be
     * reflected specularly.
     * <p>
     * Specularly reflected light is light which is reflected in a
     * mirror-like fashion, as from a shiny surface. As shown in Figure IV.3,
     * specularly reflected light leaves a surface with its angle of reflection
     * approximately equal to its angle of incidence. This is the main part of
     * the reflected light from a polished or glossy surface. Specular reflections
     * are the cause of “specular highlights,” i.e., bright spots on curved surfaces
     * where intense specular reflection occurs.
     * <p>
     * Finally, the Phong model has a provision for a highlight, or specular, component, which reflects light in a
     * shiny way. This is defined by [rs,gs,bs](-V.R)^p, where R is the mirror reflection direction vector we discussed
     * in class (and also used for ray tracing), and where p is a specular power. The higher the value of p, the shinier
     * the surface.
     */
    private Color calcSpecular(double ks, Vector l, Vector n, double nl, Vector V, int nShininess, Color ip) {
        if (isZero(nl)) {
            throw new IllegalArgumentException("nl cannot be Zero for scaling the normal vector");
        }
        Vector R = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double VR = alignZero(V.dotProduct(R));
        if (VR >= 0) {
            return Color.BLACK; // view from direction opposite to r vector
        }
        // [rs,gs,bs]ks(-V.R)^p
        return ip.scale(ks * Math.pow(-1d * VR, nShininess));
    }

    /**
     * Calculate Diffusive component of light reflection.
     * <p>
     * Diffuse light is light from a point light source which will be
     * reflected diffusely
     *
     * @param kd diffusive component coef
     * @param nl dot-product n*l
     * @param ip light intensity at the point
     * @return diffusive component of light reflection
     * <p>
     * Diffusely reflected light is light which is reflected evenly
     * in all directions away from the surface. This is the predominant mode of
     * reflection for non-shiny surfaces
     * <p>
     * The diffuse component is that dot product n•L. It approximates light, originally from light source L,
     * reflecting from a surface which is diffuse, or non-glossy. One example of a non-glossysurface is paper.
     * In general, you'll also want this to have a non-gray color value,
     * so this term would in general be a color defined as: [rd,gd,bd](n•L)
     */
    private Color calcDiffusive(double kd, double nl, Color ip) {
        return ip.scale(Math.abs(nl) * kd);
    }

//    /**
//     * calculate the transparency of all geopoints
//     * @param light
//     * @param l
//     * @param n
//     * @param geopoint
//     * @return a double of the calculate transparency
//     */
//    private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
//        Vector lightDirection = l.scale(-1); // from point to light source
//        Point3D pointGeo = geopoint.getPoint();
//        Ray lightRay = new Ray(pointGeo, lightDirection, n);
//
//        //List<Ray> shadowsRays = lightRay.getBeamThroughPoint(pointGeo, 5, 2, 80);
//
//        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
//        if (intersections == null) {
//            return 1d;
//        }
//        double lightDistance = light.getDistance(pointGeo);
//        double ktr = 1d;
//        for (GeoPoint gp : intersections) {
//            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0) {
//                ktr *= gp.getGeometry().getMaterial().getKt();
//                if (ktr < MIN_CALC_COLOR_K) {
//                    return 0.0;
//                }
//            }
//        }
//        return ktr;
//    }

    /**
     * calculate the average transparency of the list of ray
     * we received from the transparencyGetListOfRay method
     *
     * @param l
     * @param n
     * @param gp
     * @param lightSource
     * @return a double of the transparency
     */
    private double transparency(LightSource lightSource, Vector l, Vector n, GeoPoint gp) {
        double ktr = 0.0;
        List<Ray> rayList = transparencyGetListOfRay(l, n, gp, lightSource);
        for (Ray r : rayList) {
            ktr += transparencyRay(r, gp, lightSource);
        }
        return ktr / rayList.size();
    }

    /**
     * create a list of ray in order to calculate the transparency
     * @param l
     * @param n
     * @param gp
     * @param lightSource
     * @return the List of Rays
     */
    private List<Ray> transparencyGetListOfRay(Vector l, Vector n, GeoPoint gp, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Point3D point = gp.getPoint();// get one for fast performance
        Ray lightRay = new Ray(point, lightDirection, n);
        List<Ray> list = new LinkedList(Collections.singletonList(lightRay));

        if (SOFT_SHADOW_ACTIVE && SOFT_SHADOW_SIZE_RAYS > 0 && !isZero(SOFT_SHADOW_RADIUS)) {
            int counter = SOFT_SHADOW_SIZE_RAYS;
            Vector v = lightRay.get_vector();
            Point3D p0 = lightRay.get_origin();
            double x0 = p0.get_x().get();
            double y0 = p0.get_y().get();
            double z0 = p0.get_z().get();
            // now, calculate 2 orthogonal vectors to create a circle
            // as it was explained on the chiour
            Vector vX;
            if (x0 <= y0 && x0 <= z0) {
                vX = (new Vector(0.0D, -z0, y0)).normalize();
            } else if (y0 <= x0 && y0 <= z0) {
                vX = (new Vector(z0, 0.0D, -x0)).normalize();
            } else if (y0 == 0.0D && x0 == 0.0D) {
                vX = (new Vector(1.0D, 1.0D, 0.0D)).normalize();
            } else {
                vX = (new Vector(y0, -x0, 0.0D)).normalize();
            }
            Vector vY = v.crossProduct(vX).normalize();
            //pc in on the center of our virtual circle
            Point3D pc = p0.add(v);
            while (counter > 0) {
                double xFactor = ThreadLocalRandom.current().nextDouble(-1.0D, 1.0D);//cos
                double yFactor = Math.sqrt(1.0D - xFactor * xFactor);//sin
                double scale;
                for (scale = 0.0D; isZero(scale); scale = ThreadLocalRandom.current().nextDouble(-SOFT_SHADOW_RADIUS, SOFT_SHADOW_RADIUS))
                    ;
                xFactor = scale * xFactor;
                yFactor = scale * yFactor;
                Point3D p = pc;
                if (!isZero(xFactor)) {
                    p = p.add(vX.scale(xFactor));
                }
                if (!isZero(yFactor)) {
                    p = p.add(vY.scale(yFactor));
                }
                Vector v1 = (p.subtract(p0)).normalized();
                if (v.dotProduct(n) * v1.dotProduct(n) > 0.0D) {
                    --counter;
                    list.add(new Ray(p0, v1));
                }
            }
        }
        return list;
    }

    /**
     * calculate the transparency ray
     * @param ray
     * @param gp
     * @param lightSource
     * @return
     */
    private double transparencyRay(Ray ray, GeoPoint gp, LightSource lightSource) {
        double ktr = 1.0;
        Point3D point = gp.getPoint();// get one for fast performance
        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(ray);
        if (intersections == null) return ktr;
        double lightDistance = lightSource.getDistance(point);

        for (GeoPoint geoP : intersections) {
            if (alignZero(geoP.getPoint().distance(point) - lightDistance) <= 0) {
                ktr *= geoP.getGeometry().getMaterial().getKt();
                if (ktr < MIN_CALC_COLOR_K) return 0.0;
            }
        }
        return ktr;
    }

//    private boolean sign(double val) {
//        return (val > 0d);
//    }
//
//    @Deprecated
//    @Disabled("for demonstration purposes")
//    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
//        Vector lightDirection = l.scale(-1); // from point to light source
//        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
//        Point3D pointGeo = geopoint.getPoint();
//
//        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
//        if (intersections == null) {
//            return true;
//        }
//        double lightDistance = light.getDistance(pointGeo);
//        for (GeoPoint gp : intersections) {
//            if (alignZero(gp.getPoint().distance(pointGeo) - lightDistance) <= 0
//                    && gp.getGeometry().getMaterial().getKt() == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Deprecated
//    @Disabled("for demonstration purposes")
//    private boolean unshaded_0(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
//        Vector lightDirection = l.scale(-1); // from point to light source
//        Ray lightRay = new Ray(geopoint.getPoint(), lightDirection, n);
//        Point3D pointGeo = geopoint.getPoint();
//
//        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
//        if (intersections == null) {
//            return true;
//        }
//        // Flat geometry cannot self intersect
//        if (geopoint.getGeometry() instanceof FlatGeometry) {
//            intersections.remove(geopoint);
//        }
//
//        double lightDistance = light.getDistance(pointGeo);
//        for (GeoPoint gp : intersections) {
//            double temp = gp.getPoint().distance(pointGeo) - lightDistance;
//            if (alignZero(temp) <= 0)
//                return false;
//        }
//        return true;
//    }
//
//    @Deprecated
//    @Disabled("for demonstration purposes")
//    private boolean occluded(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
//        Point3D geometryPoint = geopoint.getPoint();
//        Vector lightDirection = light.getL(geometryPoint);
//        lightDirection.scale(-1);
//
//        Vector epsVector = geopoint.getGeometry().getNormal(geometryPoint);
//        epsVector.scale(epsVector.dotProduct(lightDirection) > 0 ? 2 : -2);
//        geometryPoint.add(epsVector);
//        Ray lightRay = new Ray(geometryPoint, lightDirection);
//        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
//
//        // Flat geometry cannot self intersect
//        if (geopoint.getGeometry() instanceof FlatGeometry) {
//            intersections.remove(geopoint);
//        }
//
//        for (GeoPoint entry : intersections)
//            if (entry.getGeometry().getMaterial().getKt() == 0)
//                return true;
//        return false;
//    }

    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each thread.
     *
     * @author Dan
     */
    public class Pixel {
        public volatile int row = 0;
        public volatile int col = -1;
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         *
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Render.this._print) System.out.printf("\r %02d%%", _percents);
        }

        /**
         * Default constructor for secondary Pixel objects
         */
        public Pixel() {
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.<br/>
         * The function provides next pixel number each call.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                if (_print && _counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         *
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (_print && percents > 0)
                System.out.printf("\r %02d%%", percents);
            if (percents >= 0)
                return true;
            if (_print) System.out.printf("\r %02d%%", 100);
            return false;

        }
    }

/******************************************************************************************************************/

    /**
     * checks if corners colors of a pixel are close enough to be considered as the same color
     *
     * @param pixel
     * @return boolean same or not
     */
    private boolean isSameColor(elements.Pixel pixel) {
        // check if any corner is different dramatically from it's neighbors
        return (difference(pixel.aCornerRays.color, pixel.bCornerRays.color) < COLOR_DIFFERENCE_THRESHOLD
                && difference(pixel.aCornerRays.color, pixel.dCornerRays.color) < COLOR_DIFFERENCE_THRESHOLD
                && difference(pixel.cCornerRays.color, pixel.bCornerRays.color) < COLOR_DIFFERENCE_THRESHOLD
                && difference(pixel.cCornerRays.color, pixel.dCornerRays.color) < COLOR_DIFFERENCE_THRESHOLD);
    }

    /**
     * calculate and save the color of a rayBeam in a pixel if it is not saved already
     *
     * @param pixel
     */
    private void setPixelCornersColors(elements.Pixel pixel) {
        //a corner that has not been checked for its color is null
        if (pixel.aCornerRays.color == null)
            pixel.aCornerRays.color = (calcRayColor(pixel.aCornerRays._ray));
        if (pixel.bCornerRays.color == null)
            pixel.bCornerRays.color = (calcRayColor(pixel.bCornerRays._ray));
        if (pixel.cCornerRays.color == null)
            pixel.cCornerRays.color = (calcRayColor(pixel.cCornerRays._ray));
        if (pixel.dCornerRays.color == null)
            pixel.dCornerRays.color = (calcRayColor(pixel.dCornerRays._ray));

    }
//    /**
//     * the method writes an image of the scene, for every pixel we calculate the color and write it in a separate thread
//     * @throws InterruptedException
//     */
//    public void renderImage3() {
//        //create a thread pool sized by the number of processors
//        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(actualCores);
//        //for each [i,j] pixel
//        for (int i = 0; i < nX; ++i) {
//            for (int j = 0; j < nY; ++j) {
//                int x = i;
//                int y = j;
//                //define the calculation to do for every pixel in a single thread
//                Runnable runnable = () ->{
//                    Color resultColor = pixelColorByAdaptiveSampling(x, y);
//                    _imageWriter.writePixel(x, y, resultColor.getColor());
//                };
//                executor.execute(runnable);
//            }
//        }
//        //after all - finish the thread work
//        executor.shutdown();
//        try {
//            while(!executor.awaitTermination(1, TimeUnit.HOURS));
//        } catch (InterruptedException e) {}
//    }

    /**
     * calculate the color of [i,j] pixel in the picture - superSampling is adaptive
     *
     * @param i
     * @param j
     * @return the color to paint the pixel
     */
    private Color pixelColorByAdaptiveSampling(int i, int j) {
        Color resultColor = Color.BLACK;
        //create the pixel that is calculated by the camera
        elements.Pixel pixel = _scene.getCamera().constructPixelCorners(nX, nY, j, i, screenDistance,
                screenWidth, screenHeight);
        //save the colors of the pixel corners
        setPixelCornersColors(pixel);
        //in case the colors of the corners are close enough - the result is the average of the corners colors
        if (isSameColor(pixel)) {
            return resultColor.add(pixel.aCornerRays.color, pixel.bCornerRays.color, pixel.cCornerRays.color,
                    pixel.dCornerRays.color).reduce(4);
        }

        //in case the colors are different - divide the pixel and check the sub pixels
        List<elements.Pixel> pixels = new ArrayList<>();
        pixels.add(pixel);
        //every time sub pixels are added - the size of the list grows
        for (int k = 0; k < pixels.size(); ++k) {
            elements.Pixel subPixel = pixels.get(k);
            setPixelCornersColors(subPixel);
            //in case the colors of the sub pixel are different and it is not the last level of division - divide it
            if (!isSameColor(subPixel) && subPixel.getRank() < 64)
                pixels.addAll(_scene.getCamera().dividePixel(subPixel));
                //if colors are similar or the pixel is maximum divided - add the color part of the sub pixel
            else {
                Color tmpColor = subPixel.aCornerRays.color.add(subPixel.bCornerRays.color, subPixel.cCornerRays.color,
                        subPixel.dCornerRays.color);
                tmpColor = tmpColor.reduce(4);
                //the part contributed to the result is 1 / rank
                resultColor = resultColor.add(tmpColor.reduce(subPixel.getRank()));
            }
        }
        return resultColor;
    }

    /**
     * checks the difference between two colors and returns the difference value
     *
     * @param color1
     * @param color2
     * @return difference value (called the delta in math)
     */
    private double difference(Color color1, Color color2) {
        //difference of color is (|R1 - R2| + |G1 - G2| + |B1 - B2|) / 255
        double r = Math.abs(color1.getColor().getRed() - color2.getColor().getRed());
        double g = Math.abs(color1.getColor().getGreen() - color2.getColor().getGreen());
        double b = Math.abs(color1.getColor().getBlue() - color2.getColor().getBlue());
        // the value is still related to 255 whole value, the threshold is the fragment of 255
        return r + g + b;
    }

    /**
     * method calculates the color of the ray
     *
     * @param ray
     * @return the  color of the ray
     */
    private Color calcRayColor(Ray ray) {
        Color color = Color.BLACK;
        // result color is the intersection color or background
        GeoPoint closestPoint = findClosestIntersection(ray);
        color = color.add(closestPoint == null ? backgroundColor : calcColor(closestPoint, ray));

        return color;
    }
}

