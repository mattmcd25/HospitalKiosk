package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.path.Node;

/**
 * Created by mattm on 3/29/2017.
 */

public class DirectoryViewController extends AbstractDirectoryViewController {

    private Node startNode; // The selected starting node for pathfinding

    @FXML
    private Label title;
    @FXML
    private Button goToFinalSel; //button user will press to get path to selected
    @FXML
    private Label directions; //label to give user instructions
    @FXML
    private Button modifyEntry;
    @FXML
    private Button addEntry;
    @FXML
    private Button removeEntry;
    @FXML
    private Button kiosk;


    DirectoryViewController() {}

    @Override
    public String getURL() {
        return "views/DirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeDropdown();
        initializeFilter();
        // choose direction mode
        directionMode();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                goToFinalSel.setDisable(false); //enable -> button once selection has been made
            }
        });
    }

    private void directionMode() {
        addEntry.setVisible(false);
        modifyEntry.setVisible(false);
        removeEntry.setVisible(false);
        title.setText("Select Starting Location");
        directions.setText("Select a starting location from the table above. Once a location is selected, click the '->' button " +
                "to next choose a final destination.");
        //disable the -> button so user cannot move on until they have selected an entry
        goToFinalSel.setDisable(true);
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }

    @FXML  //when user clicks -> button, they will be brought to new page and asked to pick final destination
    private void clickGoToFinalSel(ActionEvent event) {
        getDirections();
    }

    private void getDirections() {
        if (startNode == null) {
            title.setText("Select Ending Location");
            directions.setText("Select an ending location from the table above. Once a location is selected, click the 'Get Path' button " +
                    "to view a path connecting the  selected starting and ending locations.");
            goToFinalSel.setText("Get Path");
            startNode = selectedLocation.getNode();
        } else {
            Node endNode = selectedLocation.getNode();
            KioskMain.setScene(new DirectionsViewController(this.startNode, endNode));
        }
    }

    @FXML
    private void clickKiosk(ActionEvent event) {
        // TODO make this not hard-coded
        selectedLocation = KioskMain.getDir().getTheKiosk();
        System.out.println(selectedLocation);
        getDirections();
        //KioskMain.setScene("views/FinalDestSelectionView.fxml", kiosk);
    }


}

