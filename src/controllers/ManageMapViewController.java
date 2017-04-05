package controllers;

import core.KioskMain;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;
import models.path.Node;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ManageMapViewController {

    private static final String MAP_URL = "views/Map.fxml";

    private MapController mapController;
    private DraggableNode selectedNode;

    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField room;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private Button nodeAction;
    @FXML
    private Button saveAction;
    @FXML
    private TableView<Node> tableNeighbors;
    @FXML
    private TableColumn<Node, Integer> idColumn;
    @FXML
    private Button deleteNeighbor;
    @FXML
    private Button addNeighbor;
    @FXML
    private TextField newNeighbor;
    @FXML
    private Label nodeID;


    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("nodeID"));

        try {
            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(MAP_URL));
            mapContainer.getChildren().add(loader.load());
            mapController = loader.<MapController>getController();
            mapController.adminMode();
            mapController.initData(this);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        // format numeric text fields
        TextFormatter<Integer> numericX = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        TextFormatter<Integer> numericY = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        x.setTextFormatter(numericX);
        y.setTextFormatter(numericY);

        deleteNeighbor.setDisable(true);

        TextFormatter<Integer> numericNeighbor = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );

        newNeighbor.setTextFormatter(numericNeighbor);

        tableNeighbors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (tableNeighbors.getSelectionModel().getSelectedItem() != null) {
                deleteNeighbor.setDisable(false);
            }
        });
    }

    public void selectNode(DraggableNode draggableNode) {
        // set selected node
        selectedNode = draggableNode;
        Node node = draggableNode.getNode();
        // update edit view
        x.setText(new Integer(node.getX()).toString());
        y.setText(new Integer(node.getY()).toString());
        nodeID.setText(new Integer(node.getID()).toString());
        room.setText(node.getRoomName());
        // show save button
        saveAction.setVisible(true);
        // show delete node button
        nodeAction.setText("Delete node");
        tableNeighbors.getItems().setAll(selectedNode.getNode().getConnections());
    }

    public void unselectNode() {
        // unset selected node
        selectedNode = null;
        // update edit view
        x.setText("0");
        y.setText("0");
        room.setText(null);
        // remove save button
        saveAction.setVisible(false);
        // show add node button
        nodeAction.setText("Add node");
        tableNeighbors.getItems().clear();
        nodeID.setText(null);
    }

    private int getX() {
        return Integer.parseInt(x.getText());
    }

    private int getY() {
        return Integer.parseInt(y.getText());
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/AdminMenu.fxml");
    }

    @FXML
    private void clickSave(ActionEvent event) {
        System.out.println("save node");
        selectedNode.previewX(getX());
        selectedNode.previewY(getY());
        selectedNode.previewRoomName(room.getText());
        selectedNode.previewConnections(selectedNode.getNode().getConnections());
        selectedNode.save();
    }

    @FXML
    private void clickNodeAction(ActionEvent event) {
        if (mapController.nodeIsSelected()) {
            clickDelete();
        } else {
            clickAdd();
        }
    }

    private void clickDelete() {
        // delete the node
        mapController.deleteSelectedNode();
    }

    private void clickAdd() {
        // add the node
        mapController.addNode(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), room.getText());
    }

    @FXML
    private void clickDeleteNeighbor(ActionEvent event) {
        Node nodeToDelete = tableNeighbors.getSelectionModel().getSelectedItem();
        selectedNode.getNode().removeConnection(nodeToDelete);
        tableNeighbors.getItems().setAll(selectedNode.getNode().getConnections());
    }

    @FXML
    private void xAction(ActionEvent event) {
        System.out.println(event);
        selectedNode.previewX(getX());
    }

    @FXML
    private void clickAddNeighbor(ActionEvent event) {
        int neighborID = Integer.parseInt(newNeighbor.getText());
        selectedNode.getNode().addConnection(KioskMain.getPath().getNode(neighborID));
    }


    @FXML
    private void yAction(ActionEvent event) {
        System.out.println(event);
        selectedNode.previewY(getY());
    }

    @FXML
    private void roomAction(ActionEvent event) {
        System.out.println(event);
        selectedNode.previewRoomName(room.getText());
    }

}
