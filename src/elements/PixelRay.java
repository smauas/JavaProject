package elements;

import java.util.List;

import primitives.Color;
import primitives.Ray;

/**
 * Represents a ray in adaptive-sampled pixel with the ray color
 */
public class PixelRay {
    /**
     * Ray in one point on the pixel
     */
    public Ray _ray;
    /**
     * the color of the point on the pixel - null until colored by the renderer
     */
    public Color color;

    // ***************** Constructor ******************** //

    /**
     * construct a point in the pixel with its rays beam
     *
     * @param ray - list of rays
     */
    public PixelRay(Ray ray) {
        _ray = ray;
    }
}
