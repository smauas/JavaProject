//Salomon Mauas  - smauas25@gmail.com

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.*;



public final class Main {

    /**
     * Mini Project 1
     *
     * @param args
     */
    public static void main(String[] args) {

        Scene scene;
        scene = new Scene.SceneBuilder("Mini Project-soft Shadow")
                .addAmbientLight(new AmbientLight(Color.BLACK, 0))
                .addCamera(
                        new Camera(
                                new Point3D(0, -200, 0),
                                new Vector(0, 0, 1),
                                new Vector(0, -1, 0)))
                .addDistance(100)
                .addBackground(new Color(java.awt.Color.gray))
                .build();


        for (long i = -2000; i < 2001; i += 500) {
            for (long j = 0; j < 1001; j += 500) {
                scene.addGeometries(
                        new Triangle(
                                new Color(120, 120, 120),
                                new Material(0.5, 0.5, 60, 0, 0),
                                new Point3D(i, 0, j),
                                new Point3D(i + 500, 0, j),
                                new Point3D(i, 0, j + 500)),

                        new Triangle(
                                new Color(0, 0, 0),
                                new Material(0.5, 0.5, 60, 0, 0.7),
                                new Point3D(i, 0, j + 500),
                                new Point3D(i + 500, 0, j + 500),
                                new Point3D(i + 500, 0, j))
                );
            }
        }

        scene.addGeometries(
//                new Plane(
//                        new Color(40, 0, 50),
//                        new Material(0.5, 0.5, 80, 0.1, 0.3),
//                        new Point3D(-2000, 0, -2000),
//                        new Vector(1, 0, 0)),
//
//                new Plane(
//                        new Color(50, 0, 50),
//                        new Material(0.5, 0.5, 80, 0.1, 0.8),
//                        new Point3D(0, 0, 2000),
//                        new Vector(0, 0, 1)),
//
//                new Plane(
//                        new Color(40, 0, 50),
//                        new Material(0.5, 0.5, 80, 0.1, 0.3),
//                        new Point3D(2000, 0, 0),
//                        new Vector(1, 0, 0)),

                new Sphere(
                        new Color(java.awt.Color.BLUE),
                        new Material(0.5, 0.5, 80, 0, 0.7), 100,
                        new Point3D(-160, -100, 500)),

                new Sphere(new Color(java.awt.Color.green),
                        new Material(0.5, 0.5, 50, 0.7, 0.2), 100,
                        new Point3D(-500, -100, 400)),

//                new Sphere(new Color(java.awt.Color.YELLOW),
//                        new Material(0.5, 0.5, 60, 0.1, 0.9), 100,
//                        new Point3D(1000, -100, 500)),
//
                new Sphere(new Color(java.awt.Color.magenta),
                        new Material(0.5, 0.5, 70, 0, 0), 250,
                        new Point3D(-600, -250, 800)),

                new Sphere(new Color(java.awt.Color.RED),
                        new Material(0.5, 0.5, 90, 0, 0.2), 150,
                        new Point3D(450, -150, 350))

        );


//        scene.addLights(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, 1)));
//        scene.addLights(new PointLight(new Color(50, 150, 55),
//                new Point3D(100, -100, 200),1, 1E-5, 1.5E-7));
        scene.addLights(
                new SpotLight(
                        new Color(100, 50, 100),
                        new Point3D(-2000, -2000, 0),
                        new Vector(1, 1, 1), 1, 1E-5, 1.5E-7));
        scene.addLights(
                new PointLight(
                        new Color(50, 100, 100),
                        new Point3D(0, -1000, 1000), 1, 1E-5, 1.5E-7)
        );
        scene.addLights(
                new DirectionalLight(
                        new Color(50, 100, 100),
                        new Vector(0, 1, -1))
        );
        scene.addLights(
                new SpotLight(
                        new Color(100, 100, 50),
                        new Point3D(500, -500, 0),
                        new Vector(-1, 1, 1), 1, 1E-5, 1.5E-7));

        ImageWriter imageWriter = new ImageWriter("MiniProject1WithoutSS", 400, 200, 800, 400);
        Render render = new Render(imageWriter, scene, false)
//                .setSupersamplingDensity(0.5)
//                .setRayCounter(25)
                .setMultithreading(4)
                .setDebugPrint();

        render.renderImage();
        render.writeToImage();
    }
}
