package core;

import controllers.IControllerWithParams;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.Directory;
import models.dir.DirectoryManager;
import models.dir.Location;
import models.dir.LocationType;
import models.path.PathfindingManager;
import models.path.Node;

import java.io.*;
import java.sql.SQLException;

public class KioskMain extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;

    private static Stage stage;

    public static final boolean DEBUG = true;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.show();
        setScene("views/MainMenu.fxml");
    }

    public static void main(String[] args) {
        // setup the managers
        initDBMg();
        initPathMg();
        initDirMg();

        System.out.println(getPath().findPath(getPath().getNode(1), getPath().getNode(76)).textPath());

        // Launch the JavaFX application after initial setup
        launch(args);
    }

    public static DirectoryManager getDir() {
        return theDirManager;
    }

    public static PathfindingManager getPath() {
        return thePathManager;
    }

    public static DatabaseManager getDB() { return theDBManager; }

    public static void setScene(String path) {
        try {
            Parent root = FXMLLoader.load(KioskMain.class.getClassLoader().getResource(path));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public static void setScene(String path, Object... data) {
        try {
            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(path));
            Scene scene = new Scene(loader.load());
            loader.<IControllerWithParams>getController().initData(data);
            stage.setScene(scene);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    private static void initDBMg() {
        // create the database manager
        theDBManager = new DatabaseManager();
        try {
            // connect to the db
            getDB().connect();
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initPathMg() {
        // create the pathfinding manager with nodes from the db
        thePathManager = new PathfindingManager(getDB().getAllNodes());
        // Test code to print all nodes
        if (DEBUG) {
            for (Node n : getDB().getAllNodes().values()) {
                System.out.println(n);
            }
        }
    }

    private static void initDirMg() {
        // create the directory manager with directories from the db
        theDirManager = new DirectoryManager(getDB().getAllDirectories());
        // Test code to print all directories/locations
        if (DEBUG) {
            for (Directory d : getDB().getAllDirectories().values()) {
                System.out.println(d);
            }
        }
    }
}
