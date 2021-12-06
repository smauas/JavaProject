package elements;

import java.util.List;

import primitives.*;

/**
 * Represents an adaptive-sampled pixel on the view plane - may be divided to sub pixels
 */
public class Pixel {
    /**
     * the point of the pixel upper left corner on the view plane (saved for calculation shortcut)
     */
    public Point3D aPoint;
    /**
     * ray beam shot from the pixel upper left corner (holds list and color)
     */
    public PixelRay aCornerRays;
    /**
     * the point of the pixel upper right corner on the view plane (saved for calculation shortcut)
     */
    public Point3D bPoint;
    /**
     * ray beam shot from the pixel upper right corner (holds list and color)
     */
    public PixelRay bCornerRays;
    /**
     * the point of the pixel lower right corner on the view plane (saved for calculation shortcut)
     */
    public Point3D cPoint;
    /**
     * ray beam shot from the pixel lower right corner (holds list and color)
     */
    public PixelRay cCornerRays;
    /**
     * the point of the pixel lower left corner on the view plane (saved for calculation shortcut)
     */
    public Point3D dPoint;
    /**
     * ray beam shot from the pixel lower left corner (holds list and color)
     */
    public PixelRay dCornerRays;
    /**
     * the value of the pixel in the rest of the sub pixels (1 if its the only pixel, 4 if its one of four ect.)
     */
    int _rank;
    // ***************** Constructor ******************** //

    /**
     * construct a pixel from the 4 corners points and rayBeams
     *
     * @param pointA    - upper left Point (on view plane)
     * @param a         - upper left ray list
     * @param pointB    - upper right Point (on view plane)
     * @param b         - upper right ray list
     * @param pointC    - lower right Point (on view plane)
     * @param c         - lower right ray list
     * @param pointD    - lower left Point (on view plane)
     * @param d         - lower left ray list
     * @param PixelRank - how many fragments like this are in the whole main pixel
     */
    public Pixel(Point3D pointA, Ray a, Point3D pointB, Ray b, Point3D pointC, Ray c, Point3D pointD, Ray d, int PixelRank) {
        aPoint = pointA;
        aCornerRays = new PixelRay(a);
        bPoint = pointB;
        bCornerRays = new PixelRay(b);
        cPoint = pointC;
        cCornerRays = new PixelRay(c);
        dPoint = pointD;
        dCornerRays = new PixelRay(d);
        _rank = PixelRank;
    }
    // ***************** Getters ******************** //

    /**
     * get the rank of sub pixel - how many fragments like this are in the whole main pixel
     *
     * @return the rank of the sub pixel
     */
    public int getRank() {
        return _rank;
    }
}
