import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Node {

    private String prevAction;
    private int deaths;
    private int blackBoxesDamaged;
    private int curCapacitiy;
    private Node parent;
    private HashMap<Pair, Ship> ships;

    private Pair cgCoordinates;

    private HashSet<Pair> visitedCells;

    public Node(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair>visitedCells) {
        this.prevAction = prevAction;
        this.parent = parent;
        this.ships = ships;
        this.deaths = deaths;
        this.blackBoxesDamaged = blackBoxesDamaged;
        this.curCapacitiy = curCapacitiy;
        this.cgCoordinates = cgCoordinates;
        this.visitedCells= visitedCells;
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

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getBlackBoxesDamaged() {
        return blackBoxesDamaged;
    }

    public void setBlackBoxesDamaged(int blackBoxesDamaged) {
        this.blackBoxesDamaged = blackBoxesDamaged;
    }

    public int getCurCapacitiy() {
        return curCapacitiy;
    }

    public void setCurCapacitiy(int curCapacitiy) {
        this.curCapacitiy = curCapacitiy;
    }

    public Pair getCgCoordinates() {
        return cgCoordinates;
    }

    public void setCgCoordinates(Pair cgCoordinates) {
        this.cgCoordinates = cgCoordinates;
    }
    public HashSet<Pair> getVisitedCells() {
        return visitedCells;
    }
    public void setVisitedCells(HashSet<Pair> visitedCells) {
        this.visitedCells = visitedCells;
    }
}
