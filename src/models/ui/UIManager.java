package models.ui;

import controllers.NavigationDrawer.NavigationDrawerController;
import controllers.PopupView.IPopup;
import controllers.IController;
import core.KioskMain;
import javafx.scene.image.Image;
import models.timeout.TimeoutManager;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Created by dylan on 4/11/17.
 */
public class UIManager {

    private final Stage stage;
    private Parent root;
    private Scene scene;
    private IController controller;

    private IPopup popup;
    private NavigationDrawerController nav;
    private static DoubleProperty fontSize = new SimpleDoubleProperty(15);

    public UIManager(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Brigham and Women's Faulkner Hospital Kiosk");
        this.stage.getIcons().add(new Image("/resources/icon.png"));
        this.stage.show();
    }

    public void setScene(IController controller) {
        if (scene == null){
            scene = new Scene(controller.getRoot());
        } else {
            scene = new Scene(controller.getRoot(), scene.getWidth(), scene.getHeight());
        }
        this.controller = controller;


        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                KioskMain.getTimeout().resetTimer();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KioskMain.getTimeout().resetTimer();
            }
        });

//        fontSize.bind(scene.widthProperty().add(scene.heightProperty()).divide(100));
//        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//
//        controller.getRoot().styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"
//                ,"-fx-base: rgb(135, 138, 150);"));
//
//        stage.setX(bounds.getMinX());
//        stage.setY(bounds.getMinY());
//        stage.setWidth(bounds.getWidth());
//        stage.setHeight(bounds.getHeight());

        stage.setScene(scene);
    }

    public Node lookup(String query) {
        return scene.lookup(query);
    }

    public Scene getScene() {
        return this.scene;
    }

    public IController getController() {
        return this.controller;
    }

    public Stage getStage() {
        return stage;
    }

    public Parent getRoot() {
        return root;
    }

    public IPopup getPopup() {
        return popup;
    }

    public void setPopup(IPopup popup) {
        this.popup = popup;

        if(popup != null && popup.getInstance() != null) {
            popup.getInstance().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    KioskMain.getTimeout().resetTimer();
                }
            });

            popup.getInstance().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    KioskMain.getTimeout().resetTimer();
                }
            });
        }
    }

    public void setNavDrawer(NavigationDrawerController nav) {
        this.nav = nav;
    }

    public NavigationDrawerController getNavDrawer() {
        return this.nav;
    }
}
