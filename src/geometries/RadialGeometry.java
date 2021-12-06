package geometries;

import primitives.*;

import static primitives.Util.isZero;

/**
 * RadialGeometry is an abstract class that defines
 * all radial geometries.
 */
public abstract class RadialGeometry extends Geometry {
    double _radius;

    /**
     * constructor for a new extended RadialGeometry object.
     *
     * @param radius   the radius of the RadialGeometry
     * @param material the material of the RadialGeometry
     * @throws Exception in case of negative or zero radius
     */
    public RadialGeometry(Color emissionLight, double radius, Material material) {
        super(emissionLight, material);
        _radius = radius;
    }

    /**
     * constructor
     *
     * @param emissionLight
     * @param radius
     */
    public RadialGeometry(Color emissionLight, double radius) {
        super(emissionLight);
        _radius = radius;
    }

    /**
     * constructor
     * @param radius
     */
    public RadialGeometry(double radius) {
        super();
        _radius = radius;
    }

    /**
     * constructor
     * @param other
     */
    public RadialGeometry(RadialGeometry other) {
        super(other._emission, other._material);
        _radius = other._radius;
    }

    /**
     * get the radius
     *
     * @return
     */
    public double get_radius() {
        return _radius;
    }

    @Override
    public Vector getNormal(Point3D p) {
        return null;
    }

    @Override
    public String toString() {
        return "The radius is: " + _radius + '.';
    }
}
