package models.db;

import core.KioskMain;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DatabaseManager {

    private Connection conn;

    public DatabaseManager() {}

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        System.out.println("Successfully located database drivers.");
        this.conn = DriverManager.getConnection("jdbc:derby:hospitalDB;create=false");
        System.out.println("Successfully connected to database.");
    }

    /* LOCATIONS AND DIRECTORIES */

    public HashMap<LocationType, Directory> getAllDirectories() throws SQLException {
        // Run SQL query to get all LOCATIONS from the database
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT * FROM LOCATION ORDER BY ID ASC");

        HashMap<LocationType, Directory> allDirectories = new HashMap<LocationType, Directory>();
        int id = 0, nodeid;
        String name;
        LocationType locType;
        Location theloc;
        Node thenode;

        for (LocationType l : LocationType.values()) {
            allDirectories.put(l, new Directory(l.name()));
        }

        // Go through each entry and create a new Location object
        while (rset.next()) {
            id = rset.getInt("ID");
            nodeid = rset.getInt("NODEID");
            name = rset.getString("NAME");
            locType = LocationType.getType(rset.getString("LOCTYPE"));
            thenode = KioskMain.getPath().getNode(nodeid);
            theloc = new Location(id, name, locType, thenode);
            allDirectories.get(locType).addLocation(theloc);
        }

        Location.setNextLocID(id+1);

        // Return all the Directories
        return allDirectories;
    }

    public void addLocation(Location l) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Location VALUES (" + l.getID() + ", '" + l.getName() + "', '" + l.getLocType().name() + "', " + l.getNode().getID() + ")");
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + l.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeLocation(Location l) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM LOCATION WHERE ID=" + l.getID());
        }
        catch (SQLException e) {
            System.out.println("Failed to remove " + l.toString() + " from the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /* NODES */

    public HashMap<Integer, Node> getAllNodes() throws SQLException {
        // Run SQL query to get all NODEs from the database
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT * FROM NODE");

        HashMap<Integer, Node> allNodes = new HashMap<Integer, Node>();
        int x, y, id = 0;

        // Go through each entry one at a time and make a new Node object
        while (rset.next()) {
            id = rset.getInt("ID");
            x = rset.getInt("X");
            y = rset.getInt("Y");
            allNodes.put(id, new Node(id, x, y));
        }

        Node.setNextNodeID(id+1);

        // Run SQL query to get all EDGES from the database
        rset = stmt.executeQuery("SELECT * FROM EDGE");

        Node n1, n2;

        // Go through each entry and connect the two nodes mentioned
        while (rset.next()) {
            n1 = allNodes.get(rset.getInt("ANODEID"));
            n2 = allNodes.get(rset.getInt("BNODEID"));

            n1.addConnection(n2);
            n2.addConnection(n1);
        }

        rset.close();
        stmt.close();

        // Return the completed list of all nodes
        return allNodes;
    }

    public void addNode(Node n) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Node VALUES (" + n.getID() + ", " + n.getX() + ", " + n.getY() + ")");
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + n.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
