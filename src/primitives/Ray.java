package primitives;
import java.util.Random;
import java.util.Objects;
import java.util.LinkedList;
import java.util.List;

import static java.lang.StrictMath.sqrt;
import static primitives.Util.isZero;

/**
 * This class contains a 3dPoint and a vector
 */
public class Ray {

    /**
     * move the ray head point by DELTA in direction of the secondary ray but
     * over the normal’s line
     */
    private static final double DELTA = 0.1;
    /**
     * create new random number
     */
    private static final Random rnd = new Random();
    /**
     * The origin from which the ray starts.
     */
    Point3D _origin;
    /**
     * The direction of the ray.
     */
    Vector _vector;

    //********** Constructors ***********//

    /**
     * Constructor with parameters
     *
     * @param vector is initializes
     * @param point  is initialize
     */
    public Ray(Point3D point, Vector vector) {
        this._origin = new Point3D(point);
        this._vector = new Vector(vector.normalized());
    }

    /**
     * Constructor with parameters.
     * Move the ray's origin by DELTA or -DELTA in function of the sign of the dot product
     * between the ray's direction and the normal.
     *
     * @param point
     * @param direction
     * @param normal
     */
    public Ray(Point3D point, Vector direction, Vector normal) {
        _vector = new Vector(direction).normalized();
        double nv = normal.dotProduct(direction);
        Vector normalDelta = normal.scale((nv > 0 ? DELTA : -DELTA));
        _origin = point.add(normalDelta);
    }

    /**
     * copy constructor
     *
     * @param r is a Ray
     */
    public Ray(Ray r) {
        this._origin = new Point3D(r._origin);
        this._vector = r._vector.normalized();
    }

    @Override
    public String toString() {
        return "Ray: \nOrigin is: " + _origin + "\nVector is: " + _vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _origin.equals(ray._origin) &&
                _vector.equals(ray._vector);
    }

    /**
     * Scales the origin's place of the ray by a length (double)
     * @param length
     * @return Point3D of the Origin
     */
    public Point3D getTargetPoint(double length) {
        return isZero(length) ? _origin : new Point3D(_origin).add(_vector.scale(length));
    }

    /**
     * Getter for the direction of the ray that is
     * represented by this object.
     *
     * @return A new Vector that represents the
     * direction of the ray that is
     * represented by this object.
     */
    public Vector get_vector() {
        return new Vector(_vector.normalized());
    }

    /**
     * this function returns the value of the Point3D _origin
     *
     * @return _origin
     */
    public Point3D get_origin() {
        return new Point3D(_origin);
    }

    /**
     * create a beam of rays
     * @param focalPoint
     * @param ratio
     * @param radius
     * @param amount
     * @return
     */
    public List<Ray> getBeamThroughPoint(Point3D focalPoint, double ratio, double radius, int amount) {

        double distance = this._origin.distance(focalPoint);

        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        Vector v = this._vector.normalized();
        Vector normX = new Vector(v._head._y._coord * -1, v._head._x._coord, 0).normalized();
        Vector normY = v.crossProduct(normX).normalized();

        List<Ray> rays = new LinkedList<>();


        for (int counter = 0; counter < amount; counter++) {
            Point3D newPoint = new Point3D(focalPoint);
            double cosTheta = 2 * rnd.nextDouble() - 1;
            double sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

            double d = radius * (2 * rnd.nextDouble() - 1);
            double x = d * cosTheta;
            double y = d * sinTheta;

            if (!isZero(x)) {
                newPoint = newPoint.add(normX.scale(x));
            }
            if (!isZero(y)) {
                newPoint = newPoint.add(normY.scale(y));
            }
            rays.add(new Ray(this._origin, newPoint.subtract(this._origin)));
        }
        rays.add(this);
        return rays;
    }
}
