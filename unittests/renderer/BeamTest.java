package renderer;

import elements.AmbientLight;
import elements.*;
import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

public class BeamTest {
    @Test
    public void trianglesSphereBeam() {
        Scene scene;
        scene = new Scene.SceneBuilder("Test scene")
                .addAmbientLight(new AmbientLight(Color.BLACK, 0))
                .addCamera(
                        new Camera(
                                new Point3D(0, 0, -1000),
                                new Vector(0, 0, 1),
                                new Vector(0, -1, 0)))
                .addDistance(1000)
                .addBackground(Color.BLACK)
                .build();

        scene.addGeometries( //
                new Triangle(
                        Color.BLACK,
                        new Material(0, 0.8, 60), //
                        new Point3D(-150, 150, 115),
                        new Point3D(150, 150, 135),
                        new Point3D(75, -75, 150)), //
                new Triangle(
                        Color.BLACK, new Material(0, 0.8, 60), //
                        new Point3D(-150, 150, 115),
                        new Point3D(-70, -70, 140),
                        new Point3D(75, -75, 150)), //
                new Sphere(
                        new Color(java.awt.Color.BLUE),
                        new Material(0.5, 0.5, 30), // )
                        30,
                        new Point3D(0, 0, 115)));

        scene.addLights(
                new SpotLight(
                        new Color(700, 400, 400), //
                        new Point3D(40, -40, -115),
                        new Vector(-1, 1, 4),
                        1, 4E-4, 2E-5));

        ImageWriter imageWriter = new ImageWriter("trianglesSphereBeam", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene)
                .setSupersamplingDensity(0.5)
                .setRayCounter(2)
                .setMultithreading(3)
                .setDebugPrint();

        render.renderImage();
        render.writeToImage();
    }

}
