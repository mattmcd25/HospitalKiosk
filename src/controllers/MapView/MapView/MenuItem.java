package controllers.MapView.MapView;

import controllers.AboutView.AboutViewController;
import controllers.AbstractController;
import controllers.LoginView.LoginViewController;
import controllers.DirectoryView.ManageDirectoryView.ManageDirectoryViewController;
import controllers.MapView.ManageMapView.ManageMapViewController;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.path.Node;

import java.util.function.BiConsumer;

/**
 * Created by mattm on 4/24/2017
 */
public class MenuItem extends AbstractController {

    public enum EnumMenuItem {
        About("About This Application", "info", EnumMenuItem::aboutPressed),
        Login("Login", "login", EnumMenuItem::loginPressed),
        Logout("Log Out", "logout", EnumMenuItem::logoutPressed),
        ManageMap("Manage Map", "map", EnumMenuItem::manageMapPressed),
        ManageDir("Manage Directory", "dir", EnumMenuItem::manageDirPressed),
        SelectAlgo("Select Path Algorithm", "settings", EnumMenuItem::selectAlgoPressed),
        SelectKiosk("Select Kiosk Location", "settings", EnumMenuItem::selectKioskPressed);

        final String path;
        final String text;
        final BiConsumer<MouseEvent, Parent> onClick;

        EnumMenuItem(String text, String path, BiConsumer<MouseEvent, Parent> onClick) {
            this.text = text;
            this.path = path;
            this.onClick = onClick;
        }

        private static void aboutPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new AboutViewController());
        }

        private static void loginPressed(MouseEvent e, Parent mainRoot) {
            LoginViewController login = new LoginViewController(mainRoot);
            login.showCentered();
        }

        private static void logoutPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getLogin().logout();
        }

        private static void manageMapPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageMapViewController());
        }

        private static void manageDirPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }

        private static void selectAlgoPressed(MouseEvent e, Parent mainRoot) {
            Utils.showDropdown(mainRoot, "Select Algorithm", "Choose which pathfinding algorithm the application should use.",
                    KioskMain.getPath().getAlgorithms(), KioskMain.getPath().getSelectedAlgorithm().getName(),
                    (s) -> KioskMain.getPath().selectAlgorithm(s));
        }

        private static void selectKioskPressed(MouseEvent e, Parent mainRoot) {
            Utils.showDropdown(mainRoot, "Select Kiosk Location", "Choose the location that the Kiosk is currently at.",
                    KioskMain.getPath().getRoomNames(), KioskMain.getDir().getTheKiosk().getNode().getRoomName(),
                    (s) -> {
                        Node n = null;
                        try {
                            n = KioskMain.getPath().getRoom(s);
                        } catch (RoomNotFoundException e1) {
                            // TODO
                        }
                        KioskMain.getDir().getTheKiosk().setNode(n);
                    });
        }
    }

    private EnumMenuItem item;
    private Parent mainRoot;

    @FXML
    private HBox root;
    @FXML
    private Label menuIcon;
    @FXML
    private Label menuLabel;

    public MenuItem(EnumMenuItem m, Parent mainRoot) {
        super(m, mainRoot);
    }

    @Override
    public void initData(Object... data) {
        item = (EnumMenuItem) data[0];
        mainRoot = (Parent) data[1];
    }

    @FXML
    private void initialize() {
        menuLabel.setText(item.text);
        menuIcon.getStyleClass().add(item.path);

//        menuLabel.styleProperty().bind(
//            Bindings.when(root.hoverProperty())
//                    .then("-fx-text-fill: royalblue;")
//                    .otherwise("-fx-text-fill: black;")
//        );
    }

    @FXML
    private void onPressed(MouseEvent e) {
        item.onClick.accept(e, mainRoot);
    }

    public String getURL() {
        return "resources/views/NavigatinDrawer/MenuItem.fxml";
    }

}
