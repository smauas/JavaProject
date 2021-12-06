package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * the directional light class that holds a directional vector
 */
public class DirectionalLight extends Light implements LightSource {
    private Vector _direction;

    /**
     * Initialize directional light with it's intensity and direction, direction
     * vector will be normalized.
     *
     * @param colorIntensity intensity of the light
     * @param direction      direction vector
     */
    public DirectionalLight(Color colorIntensity, Vector direction) {

        super(colorIntensity);
        _direction = direction.normalized();
    }

    /**
     * @param p the lighted point is not used and is mentioned
     *          only for compatibility with LightSource
     * @return fixed intensity of the directionLight
     */
    @Override
    public Color getIntensity(Point3D p) {
        return super.getIntensity();
        //       return _intensity;
    }

    /**
     * instead of getDirection()
     *
     * @param p the lighted point
     * @return Vector of the direction light
     */
    @Override
    public Vector getL(Point3D p) {
        return new Vector(_direction);
    }

    /**
     * the distance of the directional light in the infinity
     *
     * @param point is not used and is mentioned
     *              only for compatibility with LightSource
     * @return the distance
     */
    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }
}