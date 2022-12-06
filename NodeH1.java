import java.util.HashMap;
import java.util.HashSet;

public class NodeH1 extends Node implements Comparable<NodeH1> {

    public NodeH1(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates, HashSet<Pair>visitedCells) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates,visitedCells);
    }
    private int minDistShip() {
        //dist bet coast guard and nearest ship
        int min = Integer.MAX_VALUE;
        for (Pair p : super.getShips().keySet()) {
//            if(super.getShips().get(p).getNoOfPassengers()>0 || super.getShips().get(p).isBlackBoxRetrieved())
            if(!(super.getShips().get(p).isBlackBoxRetrieved()))
                min = Math.min(min, CoastGuard.distance(super.getCgCoordinates(),p));
        }
        return min;
    }
    private int h1() {
        HashMap<Pair, Ship> ships = super.getShips();
        int count=0;
        for (Ship s:ships.values()) {
            if(!(s.isBlackBoxRetrieved()))
                count++;
        }
        if(count==0)
            return CoastGuard.minDistStation(super.getCgCoordinates());
        return count * minDistShip();
    }

    @Override
    public int compareTo(NodeH1 o) {
        //+ve -> lower priority, -ve -> higher priority
        return h1()-o.h1();
    }
}
