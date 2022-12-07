import java.util.HashMap;
import java.util.HashSet;

public class NodeH1Star extends Node implements Comparable<NodeH1Star>{
    public NodeH1Star(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair> visitedCells, int depth,int savedPassengers, int blackBoxesSaved) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates, visitedCells, depth, savedPassengers, blackBoxesSaved);
    }

    @Override
    public int compareTo(NodeH1Star o) {
        return (h1()+super.getDeaths())-(o.h1()+o.getDeaths());
    }
}
