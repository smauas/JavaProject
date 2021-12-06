package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;
/**
 * Represents an infinite tube in the 3D space.
 * That is, the cylinder does not have a length.
 */

public class Tube extends RadialGeometry {

    /**
     * represents the direction and the reference point
     */
    protected final Ray _ray;

    /**
     * constructor for a new Cylinder object
     *
     * @param _radius       the radius of the tube
     * @param _ray          the direction of the tube from the referenced point
     * @param _material     the material of the tube
     * @param emissionLight the emission light of the tube
     * @throws Exception in case of negative or zero radius from RadialGeometry constructor
     */
    public Tube(Color emissionLight, Material _material, double _radius, Ray _ray) {
        super(emissionLight, _radius);
        this._material = _material;
        this._ray = new Ray(_ray);
    }

    /**
     * constructor of the Tube
     *
     * @param _radius
     * @param _ray
     */
    public Tube(double _radius, Ray _ray) {
        this(Color.BLACK, new Material(0, 0, 0), _radius, _ray);
    }

    /**
     * Constructor of the Tube
     *
     * @param emissionLight
     * @param _radius
     * @param _ray
     */
    public Tube(Color emissionLight, double _radius, Ray _ray) {
        this(emissionLight, new Material(0, 0, 0), _radius, _ray);
    }

    /**
     * Get the ray
     *
     * @return the ray
     */
    public Ray getRay() {
        return _ray;
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null || !(obj instanceof Tube))
//            return false;
//        if (this == obj)
//            return true;
//        Tube other = (Tube) obj;
//
//        //the two vectors needs to be in the same direction,
//        //but not necessary to have the same length.
//        try {
//            Vector v = _ray.getDirection().crossProduct(other._ray.getDirection());
//        } catch (IllegalArgumentException ex) {
//            return (Util.isZero(this._radius - other._radius) && _ray.getPoint().equals((_ray.getPoint())));
//        }
//        throw new IllegalArgumentException("direction cross product with parameter.direction == Vector(0,0,0)");
//    }

    @Override
    public String toString() {
        return "ray: " + _ray +
                ", radius: " + _radius;
    }

    /**
     * @param point point to calculate the normal
     * @return returns normal vector
     */
    @Override
    public Vector getNormal(Point3D point) {
        //The vector from the point of the cylinder to the given point
        Point3D o = _ray.get_origin(); // at this point o = p0
        Vector v = _ray.get_vector();

        Vector vector1 = point.subtract(o);

        //We need the projection to multiply the _direction unit vector
        double projection = vector1.dotProduct(v);
        if (!isZero(projection)) {
            // projection of P-O on the ray:
            o = o.add(v.scale(projection));
        }

        //This vector is orthogonal to the _direction vector.
        Vector check = point.subtract(o);
        return check.normalize();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray anotherray, double maxDistance) {
        Point3D P = anotherray.get_origin();
        Point3D _point = this._ray.get_origin();

        Vector V = anotherray.get_vector(),
                Va = this._ray.get_vector(),
                DeltaP = new Vector(P.subtract(_point)),
                temp_for_use1, temp_for_use2;

        double V_dot_Va = V.dotProduct(Va),
                DeltaP_dot_Va = DeltaP.dotProduct(Va);

        temp_for_use1 = V.subtract(Va.scale(V_dot_Va));
        temp_for_use2 = DeltaP.subtract(Va.scale(DeltaP_dot_Va));

        double A = temp_for_use1.dotProduct(temp_for_use1);
        double B = 2 * V.subtract(Va.scale(V_dot_Va)).dotProduct(DeltaP.subtract(Va.scale(DeltaP_dot_Va)));
        double C = temp_for_use2.dotProduct(temp_for_use2) - _radius * _radius;
        double desc = alignZero(B * B - 4 * A * C);

        if (desc < 0) {//No solution
            return null;
        }

        double t1 = (-B + Math.sqrt(desc)) / (2 * A),
                t2 = (-B - Math.sqrt(desc)) / (2 * A);

        if (desc == 0) {//One solution
            if (-B / (2 * A) < 0) {
                return null;
            } else {
                return List.of(new GeoPoint(this, P.add(V.scale(-B / (2 * A)))));
            }
        } else if (t1 < 0 && t2 < 0) {
            return null;
        } else if (t1 < 0 && t2 > 0) {
            return List.of(new GeoPoint(this, P.add(V.scale(t2))));
        } else if (t1 > 0 && t2 < 0) {
            return List.of(new GeoPoint(this, P.add(V.scale(t1))));
        } else {
            return List.of(
                    new GeoPoint(this, P.add(V.scale(t1))),
                    new GeoPoint(this, P.add(V.scale(t2)))
            );
        }
    }
}