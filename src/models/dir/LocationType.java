package models.dir;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by mattm on 3/29/2017.
 */
public enum LocationType {
    Room(Color.BLACK),
    Service(Color.MAGENTA),
    Physician(Color.CADETBLUE),
    PointOfInterest(Color.ORANGE),
    Kiosk(Color.GREEN),
    Entrance(Color.GREEN),
    Stairs(Color.DARKBLUE),
    Elevator(Color.DARKBLUE),
    Hallway(Color.GRAY),
    Unknown(Color.GRAY);

    private Color nodeColor;

    LocationType(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    public Color getNodeColor() {
        return this.nodeColor;
    }

    public boolean isInternal() {
        return this.equals(Hallway) || this.equals(Unknown) || this.equals(Kiosk) || this.equals(Entrance);
    }

    public boolean hasNearest() {
        return this.equals(PointOfInterest) || this.equals(Stairs) || this.equals(Elevator);
    }

    public static LocationType getType(String s) {
        switch (s.toUpperCase()) {
            case "ROOM": return Room;
            case "STAIRS": return Stairs;
            case "ELEVATOR": return Elevator;
            case "POINTOFINTEREST": return PointOfInterest;
            case "SERVICE": return Service;
            case "HALLWAY": return Hallway;
            case "PHYSICIAN": return Physician;
            case "KIOSK": return Kiosk;
            case "ENTRANCE": return Entrance;
            default: return Unknown;
        }
    }

    /**
     *
     * @return The LocationTypes that a user can set a location to through the admin interface
     */
    public static LocationType[] userValues() {
        ArrayList<LocationType> values = new ArrayList<>();
        for(LocationType locType : values()) {
            if(!locType.isInternal()) {
                values.add(locType);
            }
        }
        return values.toArray(new LocationType[values.size()]);
    }
}
