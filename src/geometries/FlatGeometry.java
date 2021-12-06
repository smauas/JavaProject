package geometries;

import primitives.*;

/**
 * Flat Geometry is a Marker abstract class extending Geometry
 * to differentiate it from RadialGeometry
 * we did not declare it as an interface
 *
 * @author Dr Eliezer
 */
public abstract class FlatGeometry extends Geometry {
    /**
     * Associated plane in which the flat geometry lays
     */
    protected Plane _plane;

    /**
     * constructor
     *
     * @param _emission
     * @param _material
     */
    public FlatGeometry(Color _emission, Material _material) {
        super(_emission, _material);
    }

    /**
     * constructor
     * @param _emission
     */
    public FlatGeometry(Color _emission) {
        super(_emission);
    }

    /**
     * default constructor
     */
    public FlatGeometry() {
        super();
    }
}