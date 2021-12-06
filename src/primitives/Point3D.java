package primitives;

import static java.lang.StrictMath.sqrt;

/**
 * Point3D basic point with coordinates in X, Y, Z axes
 */

public class Point3D {

    /**
     * Class Point 3D
     */
    Coordinate _x;
    Coordinate _y;
    Coordinate _z;

    public final static Point3D ZERO = new Point3D(0.0, 0.0, 0.0);

    /**
     * constructor with 3 coordinates
     *
     * @param _x x coordinate
     * @param _y y coordinate
     * @param _z z coordinate
     */
    public Point3D(Coordinate _x, Coordinate _y, Coordinate _z) {
        this._x = _x;
        this._y = _y;
        this._z = _z;
    }

    /**
     * constructor with Point3D
     *
     * @param point
     */
    public Point3D(Point3D point) {
        this._x = point._x;
        this._y = point._y;
        this._z = point._z;
    }

    /**
     * constructor with 3 doubles
     *
     * @param _x
     * @param _y
     * @param _z
     */
    public Point3D(double _x, double _y, double _z) {
        this(new Coordinate(_x), new Coordinate(_y), new Coordinate(_z));
    }

    /**
     * @return new coordinate based on _x
     */
    public Coordinate get_x() {
        return new Coordinate(_x);
    }

    /**
     * @return new coordinate based on _y
     */
    public Coordinate get_y() {
        return new Coordinate(_y);
    }

    /**
     * @return new coordinate based on _z
     */
    public Coordinate get_z() {
        return new Coordinate(_z);
    }

    /**
     * @param o
     * @return if the object is equals to the one we compare
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return _x.equals(point3D._x) &&
                _y.equals(point3D._y) &&
                _z.equals(point3D._z);
    }

    @Override
    public String toString() {
        return "(" + _x + ", " + _y + ", " + _z + ')';
    }

    /**
     * @param v is a vector
     * @return Point3D
     */
    public Point3D subtract(Vector v) {
        return new Point3D(this._x._coord - v._head._x._coord,
                this._y._coord - v._head._y._coord,
                this._z._coord - v._head._z._coord);
    }

    /**
     * Create a Vector from a subtract between two points
     *
     * @param p is a Point3D
     * @return new vector : thisPoint - paramPoint
     */
    public Vector subtract(Point3D p) {
        return new Vector(new Point3D(
                this._x.get() - p._x.get(),
                this._y.get() - p._y.get(),
                this._z.get() - p._z.get()
        ));
    }

    /**
     * Gives a Point from the addition of a Vector and this object
     *
     * @param vector
     * @return a Point3D
     */
    public Point3D add(Vector vector) {
        return new Point3D(_x.get() + vector._head._x.get(),
                _y.get() + vector._head._y.get(),
                _z.get() + vector._head._z.get()
        );
    }

    /**
     * Calculates the square of the distance of 2 points
     *
     * @param p Point3D
     * @return double
     */
    public double distanceSquared(Point3D p) {
        return (p._x.get() - this._x.get()) * (p._x.get() - this._x.get())
                + (p._y.get() - this._y.get()) * (p._y.get() - this._y.get())
                + (p._z.get() - this._z.get()) * (p._z.get() - this._z.get());
    }

    /**
     * Calculates the the distance of 2 points from the distanceSquared function
     * @param p Point3D
     * @return double
     */
    public double distance(Point3D p) {
        return sqrt(distanceSquared(p));
    }
}
