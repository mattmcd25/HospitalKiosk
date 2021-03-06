package controllers.NavigationDrawer;

import controllers.AbstractController;
import controllers.NavigationDrawer.MenuItem.EnumMenuItem;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.login.ILoginObserver;
import models.login.UserTypeAdministrator;

import java.util.ArrayList;

/**
 * Created by dylan on 4/16/17.
 *
 */
public class NavigationDrawerController extends AbstractController implements ILoginObserver {

    @FXML
    private Label drawerClose;
    @FXML
    private Pane scrim;
    @FXML
    private Label accountText;
    @FXML
    private VBox userItems;
    @FXML
    private VBox adminItems;
    @FXML
    private VBox adminHeader;
    @FXML
    private VBox userHeader;
    @FXML
    private VBox mainBox;
    @FXML
    private VBox topItems;

    private ArrayList<MenuItem> items;

    private Parent mainRoot;
    private EnumMenuItem selected;

    public NavigationDrawerController(Parent mainRoot, EnumMenuItem selected) {
        super(mainRoot, selected);
    }

    @Override
    public void initData(Object... data) {
        mainRoot = (Parent)data[0];
        selected = (EnumMenuItem)data[1];
    }

    @FXML
    private void initialize() {
        KioskMain.getUI().setNavDrawer(this);
        KioskMain.getLogin().attachObserver(this);
        items = new ArrayList<>();
        onAccountChanged();
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Pane getScrim() {
        return scrim;
    }

    @Override
    public String getURL() {
        return "resources/views/NavigationDrawer/NavigationDrawer.fxml";
    }

    private void setMenuItems() {
        if (KioskMain.getLogin().getState() instanceof UserTypeAdministrator) {
            mainBox.getChildren().setAll(adminHeader, adminItems, userHeader, userItems);
        }
        else {
            mainBox.getChildren().setAll(userHeader, userItems);
        }
        userItems.getChildren().clear();
        adminItems.getChildren().clear();
        topItems.getChildren().clear();
        items.clear();
        for (EnumMenuItem e : KioskMain.getLogin().getState().getAvailableOptions()) {
            MenuItem m = new MenuItem(e, mainRoot);
            if(e.equals(this.selected)) m.setHighlighted();
            if(m.getHeader().equals(MenuItem.MenuHeader.ADMIN))
                adminItems.getChildren().add(m.getRoot());
            else if(m.getHeader().equals(MenuItem.MenuHeader.USER))
                userItems.getChildren().add(m.getRoot());
            else if(m.getHeader().equals(MenuItem.MenuHeader.TOP))
                topItems.getChildren().add(m.getRoot());
            items.add(m);
        }
    }

    @Override
    public void onAccountChanged() {
        accountText.setText(KioskMain.getLogin().getState().getWelcomeMessage());
        setMenuItems();
    }

    public VBox getMenuItems() {
        return userItems;
    }

//    public void deselectButtons() {
//        for(MenuItem m : items) {
//            m.deselectButton();
//        }
//    }
}
