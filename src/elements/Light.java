package elements;

import primitives.Color;
import primitives.Point3D;

/**
 * The light abstract class
 */
public abstract class Light {
    /**
     * _intensity value, set to protected
     */
    protected Color _intensity;

    /**
     * constructor
     *
     * @param intensity
     */
    public Light(Color intensity) {
        this._intensity = intensity;
    }

    /**
     * get intensity
     *
     * @return
     */
    public Color getIntensity() {
        return new Color(_intensity);
    }
}