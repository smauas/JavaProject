package primitives;

import java.util.Objects;

import static java.lang.StrictMath.sqrt;

public class Vector {
    Point3D _head;

    /**
     * constructor with a Point3D
     *
     * @param head
     */
    public Vector(Point3D head) {
        if (head.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector Zero is not valid for head");
        else this._head = head;
    }

    /**
     * constructor with three coordinates
     *
     * @param c1
     * @param c2
     * @param c3
     */
    public Vector(Coordinate c1, Coordinate c2, Coordinate c3) {
        Point3D head = new Point3D(c1, c2, c3);
        if (head.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector Zero is not valid for head");
        else {
            this._head._x = c1;
            this._head._y = c2;
            this._head._z = c3;
        }
    }

    /**
     * constructor with two points
     *
     * @param p1
     * @param p2
     */
    public Vector(Point3D p1, Point3D p2) {
        this(p1.subtract(p2));
    }

    /**
     * constructor with three doubles
     *
     * @param d1
     * @param d2
     * @param d3
     */
    public Vector(double d1, double d2, double d3) {
        Point3D head = new Point3D(d1, d2, d3);
        if (head.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector Zero is not valid for head");
        else
            this._head = new Point3D(d1, d2, d3);
    }

    /**
     * constructor with another vector
     *
     * @param vector
     */
    public Vector(Vector vector) {
        this._head = vector._head;
    }

    public Point3D get_head() {
        return new Point3D(_head._x, _head._y, _head._z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return _head.equals(vector._head);
    }

    /**
     * this function multiplies two vectors and give a new vector perpendicular to both vectors
     *
     * @param vector is multiplied with the currant vector
     * @return a new vector perpendicular to both vectors
     */
    public Vector crossProduct(Vector vector) {

        double x = (this._head.get_y().get() * vector._head.get_z().get() - this._head.get_z().get() * vector._head.get_y().get());
        double y = (this._head.get_z().get() * vector._head.get_x().get() - this._head.get_x().get() * vector._head.get_z().get());
        double z = (this._head.get_x().get() * vector._head.get_y().get() - this._head.get_y().get() * vector._head.get_x().get());
        return new Vector(new Point3D(x, y, z));
    }

    /**
     * @param other
     * @return double number of the dot product between two vectors
     */
    public double dotProduct(Vector other) {

        return _head._x.get() * other._head._x.get() +
                _head._y.get() * other._head._y.get() +
                _head._z.get() * other._head._z.get();
    }

    /**
     * this function subtracts two vectors
     *
     * @param vector
     * @return
     */
    public Vector subtract(Vector vector) {
        return new Vector(this._head.subtract(vector._head));
    }

    /**
     * this function adds two vectors
     *
     * @param vector
     * @return
     */
    public Vector add(Vector vector) {
        return new Vector(this._head.add(vector));
    }

    /**
     * this function scales a vector with a number(scalar)
     *
     * @param scalar is double
     * @return a vector
     */
    public Vector scale(double scalar) {
        return new Vector(this._head.get_x().get() * scalar,
                this._head.get_y().get() * scalar,
                this._head.get_z().get() * scalar
        );
    }

    /**
     * function that calculates the length Squared of a vector
     *
     * @return double
     */
    public double lengthSquared() {
        return (this._head._x.get() * this._head._x.get()
                + this._head._y.get() * this._head._y.get()
                + this._head._z.get() * this._head._z.get());
    }

    /**
     * function that calculates the length of a vector
     *
     * @return double
     */
    public double length() {
        return sqrt(lengthSquared());
    }

    /**
     * function that normalizes a vector
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        this._head = new Point3D(this._head._x.get() / length(),
                this._head._y.get() / length(),
                this._head._z.get() / length())
        ;
        return this;
    }

    /**
     * this function creates a new normalized vector
     *
     * @return the new normalized vector
     */
    public Vector normalized() {
        return new Vector(normalize());
    }

    @Override
    public String toString() {
        return "head=" + _head +
                '}';
    }
}
