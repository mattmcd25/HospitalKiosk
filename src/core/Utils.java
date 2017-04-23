package core;

import controllers.AlertController;
import controllers.OptionAlertController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by mattm on 4/14/2017.
 */
public class Utils {

    public static String strForNum(int i) {
        // Assumes there will never be more than 20 turns options.
        switch(i) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            default: return i + "th";
        }
    }

    public static void showAlert(Parent root, String title, String body) {
        AlertController alert = new AlertController(root, title, body);
        alert.showCentered();
    }

    public static void showOption(Parent root, String title, String body, String btn1text, EventHandler<ActionEvent> btn1, String btn2text, EventHandler<ActionEvent> btn2) {
        OptionAlertController option = new OptionAlertController(root, title, body, btn1text, btn1, btn2text, btn2);
        option.showCentered();
    }

    public static void hidePopup() {
        if(KioskMain.getUI().getPopup() != null) {
            KioskMain.getUI().getPopup().hide();
        }
    }

    /* Returns a instance of InputStream for the resource */
    public static InputStream getResourceAsStream(String resource) {
        try {
            return getResource(resource).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourceAsExternal(String resource) {
        return getResource(resource).toExternalForm();
    }

    public static URL getResource(String resource) {
        String stripped = resource.startsWith("/")?resource.substring(1):resource;
        URL path = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            path = classLoader.getResource(stripped);
        }
        if (path == null) {
            path = Utils.class.getResource(resource);
        }
        if (path == null) {
            path = Utils.class.getClassLoader().getResource(stripped);
        }
        if (path == null) {
            throw new RuntimeException("Resource not found: " + resource);
        }
        return path;
    }
}
