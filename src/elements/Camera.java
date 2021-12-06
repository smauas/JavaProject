package elements;

import geometries.Intersectable;
import geometries.Plane;
import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

/**
 * The Camera class
 */
public class Camera {
    private static final Random rnd = new Random();
    /**
     * The origin of the camera
     */
    Point3D _p0;
    /**
     * 3 vectors to define the position of the camera
     */
    Vector _vTo;
    Vector _vUp;
    Vector _vRight;

    /**
     * constructor of the camera
     *
     * @param _p0
     * @param _vTo
     * @param _vUp
     */
    public Camera(Point3D _p0, Vector _vTo, Vector _vUp) {

        //if the the vectors are not orthogonal, throw exception.
        if (_vUp.dotProduct(_vTo) != 0)
            throw new IllegalArgumentException("the vectors must be orthogonal");

        this._p0 = new Point3D(_p0);
        this._vTo = _vTo.normalized();
        this._vUp = _vUp.normalized();
        // _vRight is the normal of the _vTo and _vUp vectors
        _vRight = this._vTo.crossProduct(this._vUp).normalize();

    }

    /**
     * get the origin of the camera
     *
     * @return Point3D of the origin
     */
    public Point3D getP0() {
        return new Point3D(_p0);
    }

    /**
     * get vTo vector
     *
     * @return Vector vTo
     */
    public Vector getVTo() {
        return new Vector(_vTo);
    }

    /**
     * get vUp vector
     *
     * @return Vector vUp
     */
    public Vector getVUp() {
        return new Vector(_vUp);
    }

    /**
     * get vRight vector
     *
     * @return Vector vRight
     */
    public Vector getVRight() {
        return new Vector(_vRight);
    }

    /**
     * construct a Ray through Pixel
     *
     * @param nX             number of pixels on x axis
     * @param nY             number of pixels on y axis
     * @param j              index of the pixel on the view plane on the x axis
     * @param i              index of the pixel on the view plane on the y axis
     * @param screenDistance the distance
     * @param screenWidth
     * @param screenHeight
     * @return the Ray through Pixel
     */
    public Ray constructRayThroughPixel(int nX, int nY,
                                        int j, int i, double screenDistance,
                                        double screenWidth, double screenHeight) {
        if (isZero(screenDistance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }
        // Image center
        Point3D Pc = _p0.add(_vTo.scale(screenDistance));

        // Ratio (pixel width & height)
        double Ry = screenHeight / nY;
        double Rx = screenWidth / nX;

        // Pixel[i,j] center
        double yi = ((i - nY / 2d) * Ry + Ry / 2d);
        double xj = ((j - nX / 2d) * Rx + Rx / 2d);

        Point3D Pij = Pc;

        if (!isZero(xj)) {
            Pij = Pij.add(_vRight.scale(xj));
        }
        if (!isZero(yi)) {
            Pij = Pij.subtract(_vUp.scale(yi)); // Pij.add(_vUp.scale(-yi))
        }

        Vector Vij = Pij.subtract(_p0);

        return new Ray(_p0, Vij);
    }

    /**
     * creating beam of rays for supersampling
     *
     * @param density factor for the radius
     * @param amount  number of random rays
     * @return
     * @author Dr Eliezer
     */
    public List<Ray> constructRayBeamThroughPixel(int nX, int nY, int j, int i,
                                                  double screenDistance, double screenWidth, double screenHeight,
                                                  double density, int amount) {
        if (isZero(screenDistance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        List<Ray> rays = constructNineRaysThroughPixel(nX, nY, i, j, screenDistance, screenWidth, screenHeight);

        double Ry = screenHeight / nY;
        double Rx = screenWidth / nX;

        Point3D Pij = rays.get(rays.size() - 1).get_origin();

        //antialiasing density >= 1
        double radius = (Rx + Ry) / 2d * density;


        for (int counter = 0; counter < amount; counter++) {
            Point3D point = new Point3D(Pij);
            double cosTheta = 2 * rnd.nextDouble() - 1;
            double sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

            double d = radius * (2 * rnd.nextDouble() - 1);
            double x = d * cosTheta;
            double y = d * sinTheta;

            if (!isZero(x)) {
                point = point.add(_vRight.scale(x));
            }
            if (!isZero(y)) {
                point = point.add(_vUp.scale(y));
            }
            rays.add(new Ray(_p0, point.subtract(_p0)));
        }
        return rays;
    }

    /**
     * @param nX
     * @param nY
     * @param i
     * @param j
     * @param screenDist
     * @param screenWidth
     * @param screenHeight
     * @return
     * @author Dr Eliezer
     */
    public List<Ray> constructNineRaysThroughPixel(int nX, int nY, double i, double j, double screenDist,
                                                   double screenWidth, double screenHeight) {

        double Rx = screenWidth / nX;//the length of pixel in X axis
        double Ry = screenHeight / nY;//the length of pixel in Y axis

        double yi = ((i - nY / 2d) * Ry + Ry / 2d);
        double xj = ((j - nX / 2d) * Rx + Rx / 2d);


        Point3D Pc = new Point3D(_p0.add(_vTo.scale(screenDist)));//new point from the camera to the screen

        //pc is the center of the view plane
        Point3D P = Pc.add(_vRight.scale(xj).subtract(_vUp.scale(yi)));

        //finding the intersection point with the view plane according the formula in the moodle

        //-----SuperSampling-----
        List<Ray> rays = new LinkedList<>();//the return list, construct Rays Through Pixels

        rays.add(new Ray(P, _p0.subtract(P)));//the first ray(we already find it)
        //the next 8 rays we gonna add is the same thing, but there's difference in the variation on
        // x and y arguments, some times we will change one of them and some times both of them

        // x coord middle of pixel/2 downwards
        Point3D tmp = new Point3D(P.get_x().get() - Rx / 2, P.get_y().get(), P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // y coord middle of pixel/2 downward
        tmp = new Point3D(P.get_x().get(), P.get_y().get() - Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // x coord middle of pixel/2 upwards
        tmp = new Point3D(P.get_x().get() + Rx / 2, P.get_y().get(), P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // y coord middle of pixel/2 upward
        tmp = new Point3D(P.get_x().get(), P.get_y().get() + Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // x coord middle of pixel/2 downwards  y coord middle of pixel/2 downward
        tmp = new Point3D(P.get_x().get() - Rx / 2, P.get_y().get() - Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // x coord middle of pixel/2 upwards  y coord middle of pixel/2 downward
        tmp = new Point3D(P.get_x().get() + Rx / 2, P.get_y().get() - Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // x coord middle of pixel/2 downwards    y coord middle of pixel/2 upward
        tmp = new Point3D(P.get_x().get() - Ry / 2, P.get_y().get() + Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        // x coord middle of pixel/2 upwards   y coord middle of pixel/2 upward
        tmp = new Point3D(P.get_x().get() + Ry / 2, P.get_y().get() + Ry / 2, P.get_z().get());
        rays.add(new Ray(tmp, new Vector(_p0.subtract(tmp)).normalized()));

        return rays;

    }
// ***************** Operations ******************** //

    /**
     * create a pixel on the view plane - made of four corners rayBeams
     *
     * @param nx             the number of columns
     * @param ny             the number of rows
     * @param i              the place of the pixel in rows
     * @param j              the pace of the pixel in columns
     * @param screenDistance
     * @param screenWidth
     * @param screenHeight
     * @return a Pixel object uniting the corner rays
     */
    public Pixel constructPixelCorners(int nx, int ny, int j, int i, double screenDistance,
                                       double screenWidth, double screenHeight) {
        // Pc is the center point of the view plane: P0 + d*vTo
        Point3D pc = _p0.add(_vTo.scale(screenDistance));
        // ratio factors: rx is the width of each pixel
        double rx = screenWidth / nx;
        double ry = screenHeight / ny;
        // Xi and Yj are the coefficients that would take us to the asked point from the Pc point
        // Xi for moving in the X axis direction (right / left)
        double xi = ((i - (nx / 2d)) * rx);
        // Yj for moving in the Y axis direction (down / up)
        double yj = ((j - (ny / 2d)) * ry);
        // in case the both coefficients are zero, the asked point is the Pc point
        Point3D upperLeftPoint = pc;
        if (!Util.isZero(xi)) {
            upperLeftPoint = upperLeftPoint.add(_vRight.scale(xi));
        }
        if (!Util.isZero(yj)) {
            upperLeftPoint = upperLeftPoint.add(_vUp.scale(-yj));
        }
        // find 3 more points for corners of the pixel
        Point3D upperRightPoint = upperLeftPoint.add(_vRight.scale(rx));
        Point3D lowerRightPoint = upperRightPoint.add(_vUp.scale(ry));
        Point3D lowerLeftPoint = upperLeftPoint.add(_vUp.scale(ry));
        // build four rays in the corners of the pixel A,B,C,D
        Ray aCorner = new Ray(_p0, upperLeftPoint.subtract(_p0));
        Ray bCorner = new Ray(_p0, upperRightPoint.subtract(_p0));
        Ray cCorner = new Ray(_p0, lowerRightPoint.subtract(_p0));
        Ray dCorner = new Ray(_p0, lowerLeftPoint.subtract(_p0));
        //create the desired pixel - Hallelujah!
        Pixel pixel = new Pixel(upperLeftPoint, aCorner, upperRightPoint, bCorner, lowerRightPoint, cCorner,
                lowerLeftPoint, dCorner, 1);
        return pixel;
    }

    /**
     * divide a pixel on the view plane into four subpixels made of the corners
     *
     * @param mainPixel - the pixel to divide
     *                  //     * @param focusLength
     *                  //     * @param apertureSize
     *                  //     * @param dofRayBeamSize
     * @return a list of the 4 new subPixels
     */
    public List<Pixel> dividePixel(Pixel mainPixel) {
        //calculate the width and height of the pixel
        double pixWidth = mainPixel.aPoint.distance(mainPixel.bPoint);
        double pixHeight = mainPixel.aPoint.distance(mainPixel.dPoint);
        //save the vectors that shift a point half way to right and to down
        Vector halfWidthRightShifter = _vRight.scale(pixWidth / 2d);
        Vector halfHeightDownShifter = _vUp.scale(pixHeight / 2d);
        //find the 5 new points on the pixel we will shoot rays from
        Point3D abMiddlePoint = mainPixel.aPoint.add(halfWidthRightShifter);
        Point3D dcMiddlePoint = mainPixel.dPoint.add(halfWidthRightShifter);
        Point3D adMiddlePoint = mainPixel.aPoint.add(halfHeightDownShifter);
        Point3D bcMiddlePoint = mainPixel.bPoint.add(halfHeightDownShifter);
        Point3D pixCenterPoint = abMiddlePoint.add(halfHeightDownShifter);

        //make 5 rays by the points found
        Ray abMiddle = new Ray(_p0, abMiddlePoint.subtract(_p0));
        Ray dcMiddle = new Ray(_p0, dcMiddlePoint.subtract(_p0));
        Ray adMiddle = new Ray(_p0, adMiddlePoint.subtract(_p0));
        Ray bcMiddle = new Ray(_p0, bcMiddlePoint.subtract(_p0));
        Ray pixCenter = new Ray(_p0, pixCenterPoint.subtract(_p0));

        //the rank grows times 4
        int subdivisionRank = mainPixel.getRank() * 4;
        List<Pixel> subPixels = new ArrayList<>();
        subPixels.add(new Pixel(mainPixel.aPoint, mainPixel.aCornerRays._ray, abMiddlePoint, abMiddle, pixCenterPoint, pixCenter, adMiddlePoint, adMiddle, subdivisionRank));
        subPixels.add(new Pixel(abMiddlePoint, abMiddle, mainPixel.bPoint, mainPixel.bCornerRays._ray, bcMiddlePoint, bcMiddle, pixCenterPoint, pixCenter, subdivisionRank));
        subPixels.add(new Pixel(adMiddlePoint, adMiddle, pixCenterPoint, pixCenter, dcMiddlePoint, dcMiddle, mainPixel.dPoint, mainPixel.dCornerRays._ray, subdivisionRank));
        subPixels.add(new Pixel(pixCenterPoint, pixCenter, bcMiddlePoint, bcMiddle, mainPixel.cPoint, mainPixel.cCornerRays._ray, dcMiddlePoint, dcMiddle, subdivisionRank));
        return subPixels;
    }

//    /**
//     * help method for calculating the raybeam of the DOF model for a specific original ray
//     * @param originalRay - the ray to be replaced by a beam of intesecting rays
//     * @param focusLength
//     * @param apertureSize
//     * @param dofRayBeamSize
//     * @return a list of the rays for the focus calculation
//     */
//    private List<Ray> calcDOFRays(Ray originalRay, double focusLength, double apertureSize, int dofRayBeamSize) {
//        List<Ray> raysBeam = new ArrayList<Ray>();
//        //in case the aperture is zero - no focus or unfocus is needed
//        if (Util.isZero(apertureSize) || dofRayBeamSize == 0) {
//            raysBeam.add(originalRay);
//            return raysBeam;
//        }
//
//        Point3D basePoint = originalRay.get_origin();
//        // calculate focalPoint by building a plane for focus distance
//        Point3D focalPlaneCenter = basePoint.add(_vTo.scale(focusLength));
//        Plane focalPlane = new Plane(focalPlaneCenter, _vTo);
//        List<Intersectable.GeoPoint> intersections = focalPlane.findIntersections(originalRay);
//        Point3D focalPoint = intersections.get(0).getPoint();
//
//        // create rays randomly within the range of the aperture size, directed to the
//        // focal point
//        double halfAperture = apertureSize / 2d;
//        for (int count = 0; count < dofRayBeamSize; count++) {
//            //shift the point of the ray stat randomly within the range of the aperture size
//            Point3D shiftedPoint = basePoint.add(_vRight.scale(Util.getNotZeroRandom() * halfAperture));
//            shiftedPoint = shiftedPoint.add(_vUp.scale(Util.getNotZeroRandom() * halfAperture));
//            Ray ray = new Ray(shiftedPoint, focalPoint.subtract(shiftedPoint));
//            raysBeam.add(ray);
//        }
//        return raysBeam;
//    }
}
