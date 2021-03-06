package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Util;
import primitives.Vector;

/**
 * The SpotLight object specifies an attenuated light source at a fixed point in space that radiates light
 * in a specified direction from the light source. A SpotLight has the same attributes as a PointLight node,
 * with the addition of the following:
 * <p>
 * Direction - The axis of the cone of light.
 * <p>
 * Concentration exponent - Specifies how quickly the light intensity attenuates.
 * It attenuates as a function of the angle of radiation as measured from the direction of radiation.
 * The light's intensity is highest at the center of the cone and is attenuated toward the edges of the cone
 * by the cosine of the angle between the direction of the light and the direction from the light
 * to the object being lit, raised to the power of the spot concentration exponent.
 * The higher the concentration value, the more focused the light source.
 * <p>
 * A spot light contributes to diffuse and specular reflections, which depend on the orientation and position
 * of an object's surface. A spot light does not contribute to ambient reflections.
 *
 * @author Dr Eliezer
 */
public class SpotLight extends elements.PointLight {
    Vector _direction;
    double _concentration;

    /**
     * Constructor
     *
     * @param colorIntensity
     * @param position
     * @param direction
     * @param kC
     * @param kL
     * @param kQ
     * @param concentration
     */
    public SpotLight(Color colorIntensity, Point3D position, Vector direction, double kC, double kL, double kQ, double concentration) {
        super(colorIntensity, position, kC, kL, kQ);
        this._direction = new Vector(direction).normalized();
        this._concentration = concentration;
    }

    /**
     * constructor
     *
     * @param colorIntensity
     * @param position
     * @param direction
     * @param kC
     * @param kL
     * @param kQ
     */
    public SpotLight(Color colorIntensity, Point3D position, Vector direction, double kC, double kL, double kQ) {
        this(colorIntensity, position, direction, kC, kL, kQ, 1);
    }


    /**
     * get the spot light intensity
     *
     * @param p Point3D
     * @return the Color of the spot light
     */
    @Override
    public Color getIntensity(Point3D p) {
        double projection = _direction.dotProduct(getL(p));

        if (Util.isZero(projection)) {
            return Color.BLACK;
        }
        double factor = Math.max(0, projection);
        Color spotlightIntensity = super.getIntensity(p);

        if (_concentration != 1) {
            factor = Math.pow(factor, _concentration);
        }

        return (spotlightIntensity.scale(factor));
    }
}