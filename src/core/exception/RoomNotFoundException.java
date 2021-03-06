package core.exception;

/**
 * Created by mattm on 4/1/2017.
 */
public class RoomNotFoundException extends Exception {
    private final String s;

    public RoomNotFoundException(String s) {
        this.s = s;
    }

    public void printStackTrace() {
        System.out.println("The room " + s + " was not found!");
        super.printStackTrace();
    }
}
