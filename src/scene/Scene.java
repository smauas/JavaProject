package scene;

import elements.*;
import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * The class that build the scene
 */
public class Scene {
    private final String _name;
    private final Geometries _geometries = new Geometries();
    private Color _background;
    private Camera _camera;
    /**
     * the distance of the view-plane from the camera lens
     */
    private double _distance;
    private AmbientLight _ambientLight;
    private List<LightSource> _lights = null;

    /**
     * constructor
     *
     * @param _sceneName
     */
    public Scene(String _sceneName) {
        this._name = _sceneName;
    }

    /**
     * get the ambient light
     *
     * @return The Ambient Light
     */
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    /**
     * get the camera
     *
     * @return the Camera
     */
    public Camera getCamera() {
        return _camera;
    }

    /**
     * get geometries
     *
     * @return Geometries
     */
    public Geometries getGeometries() {
        return _geometries;
    }

    /**
     * get the distance
     *
     * @return double of the distance
     */
    public double getDistance() {
        return _distance;
    }

    /**
     * Get the Color of the scene background
     *
     * @return the Color of the background
     */
    public Color getBackground() {
        return this._background;
    }

    /**
     * get the light sources
     *
     * @return a list of the light sources
     */
    public List<LightSource> getLightSources() {
        return _lights;
    }

    /**
     * add geometries in the list of the geometries
     *
     * @param intersectables
     */
    public void addGeometries(Intersectable... intersectables) {
        for (Intersectable i : intersectables) {
            _geometries.add(i);
        }
    }

    /**
     * remove Geometries from the list
     *
     * @param intersectables
     */
    public void removeGeometries(Intersectable... intersectables) {
        for (Intersectable i : intersectables) {
            _geometries.remove(i);
        }
    }

    /**
     * create a linked list or add light to the linkedList of lights
     *
     * @param light
     */
    public void addLights(LightSource light) {
        if (_lights == null) {
            _lights = new LinkedList<>();
        }
        _lights.add(light);
    }

    /**
     * the scene Bilder Class
     */
    public static class SceneBuilder {
        private String name;
        private Color background;
        private Camera camera;
        private double distance;
        private AmbientLight ambientLight;

        /**
         * Constructor
         *
         * @param name
         */
        public SceneBuilder(String name) {
            this.name = name;
        }

        /**
         * add background to the scene
         *
         * @param background
         * @return the SceneBuilder
         */
        public SceneBuilder addBackground(Color background) {
            this.background = background;
            return this;
        }

        /**
         * add a camera to the scene
         *
         * @param camera
         * @return the SceneBuilder
         */
        public SceneBuilder addCamera(Camera camera) {
            this.camera = camera;
            return this;
        }

        /**
         * add a distance
         *
         * @param distance
         * @return the SceneBuilder
         */
        public SceneBuilder addDistance(double distance) {
            this.distance = distance;
            return this;
        }

        /**
         * add ambient light
         *
         * @param ambientLight
         * @return the SceneBuilder
         */
        public SceneBuilder addAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        /**
         * build the scene
         *
         * @return The scene
         */
        public Scene build() {
            Scene scene = new Scene(this.name);
            scene._camera = this.camera;
            scene._distance = this.distance;
            scene._background = this.background;
            scene._ambientLight = this.ambientLight;
            //validateUserObject(scene);
            return scene;
        }

//        /**
//         * validate user object
//         * @param scene
//         */
//        private void validateUserObject(Scene scene) {
//            //Do some basic validations to check
//            //if user object does not break any assumption of system
//        }
    }
}