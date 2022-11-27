import java.util.HashMap;

public class Node {
    private String prevAction; //one of 7 & specify how many picked-up/dropped-off
//    private int costSoFar; //int wla pair?? //calculated wla fl constructor - todo
    private Node parent;
    private HashMap<Pair,Ship> ships;

    public Node(String prevAction, HashMap<Pair, Ship> ships, Node parent) {
        this.prevAction = prevAction;
        this.parent = parent;
//        this.costSoFar = costSoFar;
        this.ships = ships;
    }

    public String getPrevAction() {
        return prevAction;
    }

    public void setPrevAction(String prevAction) {
        this.prevAction = prevAction;
    }

//    public int getCostSoFar() {
//        return costSoFar;
//    }
//
//    public void setCostSoFar(int costSoFar) {
//        this.costSoFar = costSoFar;
//    }

    public HashMap<Pair, Ship> getShips() {
        return ships;
    }

    public void setShips(HashMap<Pair, Ship> ships) {
        this.ships = ships;
    }
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

}
