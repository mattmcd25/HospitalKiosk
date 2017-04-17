package core.exception;

import models.dir.LocationType;

/**
 * Created by mattm on 4/13/2017.
 */
public class NearestNotFoundException extends Exception {
    private LocationType loc;
    private int floor;

    public NearestNotFoundException(LocationType type, int floor) {
        this.loc = type;
        this.floor = floor;
    }

    public void printStackTrace() {
        System.out.println("There are no " + this.loc.name() + "s on floor " + floor + "!");
        super.printStackTrace();
    }
}