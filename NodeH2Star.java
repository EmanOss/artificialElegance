import java.util.HashMap;
import java.util.HashSet;

public class NodeH2Star extends Node implements Comparable<NodeH2Star>{
    public NodeH2Star(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair> visitedCells, int depth,int savedPassengers, int blackBoxesSaved) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates, visitedCells, depth, savedPassengers, blackBoxesSaved);
    }

    @Override
    public int compareTo(NodeH2Star o) {
        return (h2()+super.getBlackBoxesDamaged())-(o.h2()+o.getBlackBoxesDamaged());
    }
}
