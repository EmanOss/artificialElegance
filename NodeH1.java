import java.util.HashMap;

public class NodeH1 extends Node implements Comparable {

    public NodeH1(String prevAction, HashMap<Pair, Ship> ships, Node parent, int deaths, int blackBoxesDamaged, int curCapacitiy, Pair cgCoordinates) {
        super(prevAction, ships, parent, deaths, blackBoxesDamaged, curCapacitiy, cgCoordinates);
    }
    private int minDistShip() {
        //dist bet coast guard and nearest ship
        int min = Integer.MAX_VALUE;
        for (Pair p : super.getShips().keySet()) {
            min = Math.min(min, CoastGuard.distance(super.getCgCoordinates(),p));
        }
        return min;
    }
    private int h1() {
        HashMap<Pair, Ship> ships = super.getShips();
        int count=0;
        for (Ship s:ships.values()) {
            if(s.getNoOfPassengers()>0)
                count++;
        }
        if(count==0)
            return CoastGuard.minDistStation(super.getCgCoordinates());
        return count * minDistShip();
    }

    @Override
    public int compareTo(Object o) {
        //+ve -> lower priority, -ve -> higher priority
        return ((NodeH1) o).h1()-h1();
    }
}
