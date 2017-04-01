package models.dir;

import core.KioskMain;
import models.db.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class Directory {
    private String name;
    private HashMap<Integer, Location> entries;

    public Directory(String name) {
        this.name = name;
        this.entries = new HashMap<Integer, Location>();
    }

    public void addEntry(Location l) {
        if (l.isNewLoc()) {
            try {
                KioskMain.getDB().addLocation(l);
            }
            catch (SQLException e) {
                System.out.println("Failed to add " + l.toString() + " to the database.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        this.entries.put(l.getID(), l);
    }

    public String toString() {
        String str = this.name + " Directory\n";
        for (Location l : this.entries.values()) {
            str += l.toString() + "\n";
        }
        return str;
    }
}
