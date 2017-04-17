package models.path;


import core.KioskMain;
import core.Utils;
import models.dir.LocationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mattm on 3/29/2017.
 */
public class Path {
    private LinkedList<Node> path;

    public Path(){
        this.path = new LinkedList<>();
    }

    /** Adds nodes to the path in order, first to last. **/
    public void addInOrder(Node n) {
        this.path.addLast(n);
    }

    /** Builds the path in reverse order, last to first. **/
    public void buildPath(Node n){
        this.path.addFirst(n);
    }

    public Path addSteps(Path p){
        for(Node n : p.getPath()){
            this.addInOrder(n);
        }
        return this;
    }

    public LinkedList<Node> getPath() {
        return (LinkedList<Node>) this.path.clone();
    }

    public Node getStep(int i) {
        return this.path.get(i);
    }

    public String textPath() {
        if(this.path.size() < 2) return "You are already at your destination!";
        // Calculate the cardinal starting direction
        String str = "1. Start by leaving " + this.path.getFirst().getRoomName() + ".\n";
        Direction cur = Direction.dirFor(this.getStep(0), this.getStep(1));

        // Initialize the array that keeps track of attempts for each turn
        HashMap<String, Integer> attempts = new HashMap<String, Integer>();
        attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
        int stepNum = 2;
        boolean waiting = false;
        for (int i = 2; i < this.path.size(); i++) {
            if(this.getStep(i-1).equals(this.getStep(i))) continue;

            if(this.getStep(i-1).isBelkin() && !this.getStep(i).isBelkin()) {
                // leaving belkin
                str += stepNum + ". Leave the Belkin House and walk across the parking lot.\n";
                stepNum ++;
                waiting = true;
            }
            else if(this.getStep(i-1).isMain() && !this.getStep(i).isMain()) {
                // leaving main
                str += stepNum + ". Leave the main building and walk across the parking lot.\n";
                stepNum ++;
                waiting = true;
            }
            else if(!this.getStep(i-1).isBelkin() && this.getStep(i).isBelkin()) {
                // entering belkin
                str += stepNum + ". Enter the Belkin House.\n";
                stepNum ++;
                waiting = false;
                continue;
            }
            else if(!this.getStep(i-1).isMain() && this.getStep(i).isMain()) {
                // entering main
                str += stepNum + ". Enter the main building.\n";
                stepNum ++;
                waiting = false;
                continue;
            }

            if(waiting) continue;

            if(this.getStep(i).getPrimaryLocType().equals(LocationType.Elevator) && this.getStep(i-1).getPrimaryLocType().equals(LocationType.Elevator)) {
                str += stepNum + ". Ride the elevator to the " + Utils.strForNum(this.getStep(i).getFloor()) + " floor and exit.\n";
                stepNum ++;
            }
            // Calculate the next cardinal turning direction
            Direction next = Direction.dirFor(this.getStep(i-1), this.getStep(i));

            // Turn the new direction into a relative direction (left, right, or straight)
            String result = cur.turnFor(next);
            if(result.equals("straight")) {
                // If just continuing straight, record any possible turns that weren't taken.
                // This is used to keep track for the "4th left" or "3rd right" message.
                int hallways = 0;
                for (Node conn : this.getStep(i-1).getConnections()) {
                    Direction connect = Direction.dirFor(this.getStep(i-1), conn);
                    String turn = cur.turnFor(connect);
                    if(conn.getRoomName().equals("NONE")) { // if it's a hallway
                        attempts.put(turn, attempts.get(turn) + 1);
                        hallways ++;
                    }
                }
                if(hallways > 2) {
                    str += stepNum + ". Go " + result + " through the intersection.\n";
                    stepNum ++;
                }
            }
            else {
                // If actually making a turn, add a message about it to the directions
                if(i+1 == this.path.size()) {
                    str += stepNum + ". Your destination (" + this.getEnd().getRoomName() + ") will be on your " + result + ".\n";
                    stepNum ++;
                }
                else {
                    if(!this.getStep(i-1).getPrimaryLocType().equals(LocationType.Elevator)) {
                        str += stepNum + ". Make a " + result;
                        if (this.getStep(i).getPrimaryLocType().equals(LocationType.Elevator))
                            str += " into the " + this.getStep(i).getRoomName() + ".\n";
                        else str += ".\n";
                        stepNum ++;
                    }
                }
                // Reset attempt counters every time a turn is made
                attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
            }

            // Update current cardinal direction to the new one
            cur = next;
        }

        return str;
    }

    public String toString() {
        //System.out.println(this.textPath());
        String str = "Path: ";
        for (Node n : this.path) {
            str += n.getID() + ", ";
        }
        return str;
    }

    public Node getStart() {
        return this.path.getFirst();
    }

    public Node getEnd() {
        return this.path.getLast();
    }

    public ArrayList<String> getFloorsSpanning() {
        ArrayList<String> results = new ArrayList<>();

        for (Node n : this.path) {
            String floor = Utils.strForNum(n.getFloor()) + " Floor";
            if(!results.contains(floor)) results.add(floor);
        }

        return results;
    }

    @Override
    public boolean equals(Object o) {
        Path p = (Path)o;
        if(this.path.size() != p.path.size()) return false;
        for (int i = 0; i < this.path.size(); i++) {
            if(this.path.get(i).getID() != p.path.get(i).getID()) return false;
        }
        return true;
    }
}