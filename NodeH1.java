import java.util.HashMap;
import java.util.HashSet;

public class NodeH1 extends Node implements Comparable<NodeH1> {

    public NodeH1(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair>visitedCells, int depth, int savedPassengers, int blackBoxesSaved) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates,visitedCells, depth, savedPassengers, blackBoxesSaved);
    }

    @Override
    public int compareTo(NodeH1 o) {
        //+ve -> lower priority, -ve -> higher priority
        return h1()-o.h1();
    }
}
