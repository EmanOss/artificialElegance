import java.util.HashMap;
import java.util.HashSet;

public class NodeH2 extends Node implements Comparable<NodeH2>{
    public NodeH2(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair> visitedCells, int depth, int savedPassengers, int blackBoxesSaved) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates, visitedCells, depth,savedPassengers, blackBoxesSaved);
    }

    @Override
    public int compareTo(NodeH2 o) {
        return h2()-o.h2();
    }
}
