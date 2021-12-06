package geometries;

import primitives.*;


import java.util.List;

/**
 * Intersectable is a common interface for all geometries that are able
 * to intersect from a ray to their entity (Shape)
 */
public interface Intersectable {

    /**
     * find the intersections
     *
     * @param ray pointing toward a Geometry
     * @return List<GeoPoint> return values
     */
    default List<GeoPoint> findIntersections(Ray ray) {
        return findIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * find the intersections
     * @param ray pointing toward a Geometry
     * @param maxDistance the max distance between the ray and the geometrie
     * @return List<GeoPoint> return values
     */
    List<GeoPoint> findIntersections(Ray ray, double maxDistance);

    /**
     * GeoPoint is just a tuple holding
     * references to a specific point ain a specific geometry
     */
    class GeoPoint {

        protected Geometry _geometry;
        protected Point3D _point;

        public GeoPoint(Geometry geometry, Point3D pt) {
            this._geometry = geometry;
            this._point = pt;
        }

        /**
         * get the point of the GeoPoint
         *
         * @return the point
         */
        public Point3D getPoint() {
            return _point;
        }

        /**
         * get the Geometry of the GeoPoint
         *
         * @return the Geometry
         */
        public Geometry getGeometry() {
            return _geometry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GeoPoint geoPoint = (GeoPoint) o;

            return ((_geometry.equals(geoPoint._geometry)) && (_point.equals(geoPoint._point)));
        }
    }
    //end of GeoPoint

}
//end of Intersectable