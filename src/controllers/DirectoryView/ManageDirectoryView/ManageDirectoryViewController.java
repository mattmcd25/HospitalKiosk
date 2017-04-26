package controllers.DirectoryView.ManageDirectoryView;

import com.jfoenix.controls.JFXButton;
import controllers.DirectoryView.AbstractDirectoryViewController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

/**
 * Created by dylan on 4/9/17.
 */
public class ManageDirectoryViewController extends AbstractDirectoryViewController {

    private Node startNode; // The selected starting node for pathfinding
    private Node endNode;

    @FXML
    private Button backButton;
    @FXML
    private Label title;
    @FXML
    private Button goToFinalSel; //button user will press to get path to selected
    @FXML
    private Label directions; //label to give user instructions
    @FXML
    private JFXButton addEntry;
    @FXML
    private JFXButton removeEntry;
    @FXML
    private Button kiosk;
    @FXML
    private VBox locationTypes;


    public ManageDirectoryViewController() {}

    @Override
    public String getURL() {
        return "resources/views/DirectoryView/ManageDirectoryView/ManageDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        backButton.setOnAction(this::clickBack);
        addEntry.setOnAction(this::clickAdd);
        removeEntry.setOnAction(this::clickRemove);
        // setup table and search
        initializeTable();
        initializeFilter();
        setFullDirectory();
        // choose admin mode
        adminMode();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                removeEntry.setDisable(false);
            } else {
                removeEntry.setDisable(true);
            }
        });
    }

    private void adminMode() {
        setTableEdit();
        removeEntry.setDisable(true);
        title.setText("Manage Directory");
        directions.setText("");
        addLocationBtns();

    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
    }

    @FXML
    private void clickAdd(ActionEvent event) {
        String defaultRoomName = KioskMain.getPath().getRoomNames().iterator().next();
        Node defaultNode = null;
        try {
            defaultNode = KioskMain.getPath().getRoom(defaultRoomName);
        } catch (RoomNotFoundException e) {
            // TODO
        }
        LocationType newType = LocationType.userValues()[0];
        if(dirType != null) {
            newType = dirType;
        }
        Location newLoc = new Location(newType, defaultNode);
        locationsTable.getItems().add(0, newLoc);
        KioskMain.getDir().addLocation(newLoc);
    }

    @FXML
    private void clickRemove(ActionEvent event) {
        if (selectedLocation != null) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }
    }

    @FXML
    private void addLocationBtns() {

        JFXButton fulldir = new JFXButton();
        fulldir.setText("Full Directory");
        fulldir.setOnAction(event -> setFullDirectory());
        fulldir.setPrefWidth(150);
        fulldir.getStylesheets().add(Utils.getResourceAsExternal("resources/styles/Main.css"));
        fulldir.getStyleClass().add("content-button");
        locationTypes.getChildren().add(fulldir);

        for (LocationType locType : LocationType.userValues()) {
            JFXButton loc = new JFXButton();
            loc.setText(locType.friendlyName());
            loc.setOnAction(event -> selectDirectory(locType));
            loc.setPrefWidth(150);
            loc.getStylesheets().add(Utils.getResourceAsExternal("resources/styles/Main.css"));
            loc.getStyleClass().add("content-button");
            locationTypes.getChildren().add(loc);
        }

    }


}