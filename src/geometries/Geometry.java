package geometries;

import primitives.*;

/**
 * interface Geometry is the basic interface for all geometric objects
 * who are implementing getNormal method.
 */
public abstract class Geometry implements Intersectable {

    /**
     * the color of the object
     */
    protected Color _emission;
    /**
     * the material of the object
     */
    protected Material _material;

    /**
     * constructor
     *
     * @param emission
     * @param material
     */
    public Geometry(Color emission, Material material) {
        this._emission = new Color(emission);
        this._material = new Material(material);
    }

    /**
     * constructor
     *
     * @param _emission
     */
    public Geometry(Color _emission) {
        this(_emission, new Material(0d, 0d, 0));
    }

    /**
     * default constructor
     */
    public Geometry() {
        this(Color.BLACK);
    }

    /**
     * get the emission light
     * @return the color of the object
     */
    public Color getEmissionLight() {
        return (_emission);
    }

    /**
     * get material
     * @return the material of the object
     */
    public Material getMaterial() {
        return _material;
    }

    /**
     * abstract method to get the normal
     * @param p
     * @return the normal vector
     */
    abstract public Vector getNormal(Point3D p);
}