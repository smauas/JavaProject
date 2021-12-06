package elements;

import primitives.Color;

/**
 * class that defines the Ambient Light
 */
public class AmbientLight extends Light {
    /**
     * constructor
     *
     * @param _intensity
     * @param kA
     */
    public AmbientLight(Color _intensity, double kA) {
        super(_intensity.scale(kA));
    }
}