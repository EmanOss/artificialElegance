package code;

import java.util.*;

public class CoastGuard extends GeneralSearch {
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {-1, 0, 1, 0};
    private static int maxCapacity;
    private static int cgX, cgY; //coastGuard location
    private static HashSet<Pair> stations;
    private static HashMap<Pair, Ship> initShips;
    private static final Pair cost = new Pair(0, 0);
    private static int gridCols, gridRows;
    static StringBuilder plan;
    static int expandedNodes;
    static Node goal;


    public CoastGuard(int maxCapacity) {
        maxCapacity = CoastGuard.maxCapacity;//range 30 to 100 inclusive
        stations = new HashSet<>();
        initShips = new HashMap<>();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        CoastGuard.maxCapacity = maxCapacity;
    }

    private static void extractGridInfo(String grid) {
        String[] gridSplit = grid.split(";");
        //create grid
        String[] dimension = gridSplit[0].split(",");
        gridCols = Integer.parseInt(dimension[0]);
        gridRows = Integer.parseInt(dimension[1]);
        //add coast guard
        String[] coastGuard = gridSplit[2].split(",");
        cgX = Integer.parseInt(coastGuard[0]);
        cgY = Integer.parseInt(coastGuard[1]);
        maxCapacity = Integer.parseInt(gridSplit[1]);
        //add stations
        int x, y;
        String[] stationsLocations = gridSplit[3].split(",");
        stations = new HashSet<>();
        for (int i = 0; i < stationsLocations.length - 1; i += 2) {
            x = Integer.parseInt(stationsLocations[i]);
            y = Integer.parseInt(stationsLocations[i + 1]);
            stations.add(new Pair(x, y));
        }
        //add ships
        String[] shipsLocations = gridSplit[4].split(",");
        initShips = new HashMap<>();
        Ship s;
        for (int i = 0; i < shipsLocations.length - 2; i += 3) {
            x = Integer.parseInt(shipsLocations[i]);
            y = Integer.parseInt(shipsLocations[i + 1]);
            s = new Ship(Integer.parseInt(shipsLocations[i + 2]));
            initShips.put(new Pair(x, y), s);
        }
    }

    public static String genGrid() {
        StringBuilder grid = new StringBuilder();
        int n = (int) (Math.random() * 11) + 5; //5<=n<=15 - i - rows
        int m = (int) (Math.random() * 11) + 5; //5<=m<=15 - j - columns
        grid.append(m + "," + n + ";");

        //randomize ships and stations
        // from 1 to n*m-2 to leave a cell for CG and a station
        int ships = ((int) (Math.random() * (m * n - 2))) + 1;
        //min between the random number and the remaining cells
        int stations = Math.min((int) (Math.random() * (m * n - 2)) + 1, n * m - 1 - ships);

        boolean[][] assigned = new boolean[n][m];
        for (int i = 0; i < stations + ships + 1; i++) {
            int x = (int) (Math.random() * n);
            int y = (int) (Math.random() * m); //col
            while (assigned[x][y]) {
                x = (int) (Math.random() * n);
                y = (int) (Math.random() * m);
            }
            assigned[x][y] = true;
            if (i == 0)
                grid.append(((int) (Math.random() * 71) + 30) + ";" + x + "," + y + ";");
            else if (i < stations)
                grid.append(x + "," + y + ",");
            else if (i == stations)
                grid.append(x + "," + y + ";");
            else
                grid.append(x + "," + y + "," + ((int) (Math.random() * 100) + 1) + ",");

        }

        grid.setCharAt(grid.length() - 1, ';');

        return grid.toString();
    }

    public static String solve(String gridStr, String strategy, Boolean visualize) {
        expandedNodes = 0;
        cost.setX(0);
        cost.setY(0);
        extractGridInfo(gridStr);
        plan = new StringBuilder();
        goal = null;
        switch (strategy) {
            case ("BF") -> BFS();
            case ("DF") -> DFS();
            case ("UC") -> UC();
            case ("ID") -> IDS();
            case ("GR1") -> greedy(1);
            case ("GR2") -> greedy(2);
            case ("AS1") -> aStar(1);
            case ("AS2") -> aStar(2);
        }
        return buildPlan(goal, visualize);
    }

    public static void BFS() {
        HashSet<VisitedCell> visited = new HashSet<>();
        Queue<Node> q = new LinkedList<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        q.add(start);
        while (!q.isEmpty()) {
            Node cur = q.poll();
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
                cur.setSavedPassengers(cur.getSavedPassengers() + takenPassengers);
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
                cur.setBlackBoxesSaved(cur.getBlackBoxesSaved() + 1);
            }
            if (cur.getPrevAction().equals("drop"))
                cur.setCurCapacitiy(0);

            if (!cur.getPrevAction().equals("")) {
                updateGridShips(ships);
            }
            cur.setDeaths(cur.getDeaths() + cost.getX());
            cur.setBlackBoxesDamaged(cur.getBlackBoxesDamaged() + cost.getY());

            if (isGoal(cur.getCurCapacitiy(), ships)) {
                goal = cur;
                break;
            }

            VisitedCell cell = new VisitedCell(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY(), cur.getCurCapacitiy(), cur.getSavedPassengers(), cur.getBlackBoxesSaved());
            if (visited.contains(cell)) {
                continue;
            }
            expandedNodes++;
            visited.add(cell);

            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    q.add(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
                    q.add(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                q.add(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
            }
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY)) {
                    String move = getMove(dx[i], dy[i]);
                    q.add(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }

        }
    }

    public static void DFS() {
        Stack<Node> s = new Stack<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        s.push(start);
        DFS2(s, -1);
    }


    public static void DFS2(Stack s, int maxDepth) {
        HashSet<VisitedCell> visited = new HashSet<>();
        while (!s.empty()) {
            Node cur = (Node) s.pop();
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
                cur.setSavedPassengers(cur.getSavedPassengers() + takenPassengers);
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
                cur.setBlackBoxesSaved(cur.getBlackBoxesSaved() + 1);
            }

            if (cur.getPrevAction().equals("drop"))
                cur.setCurCapacitiy(0);

            if (!cur.getPrevAction().equals("")) {
                updateGridShips(ships);
            }
            cur.setDeaths(cur.getDeaths() + cost.getX());
            cur.setBlackBoxesDamaged(cur.getBlackBoxesDamaged() + cost.getY());

            if (isGoal(cur.getCurCapacitiy(), ships)) {
                goal = cur;
                System.out.println(goal.getDepth());
                break;
            }
            if (cur.getDepth() == maxDepth)
                continue;
            VisitedCell cell = new VisitedCell(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY(), cur.getCurCapacitiy(), cur.getSavedPassengers(), cur.getBlackBoxesSaved());
            if (visited.contains(cell)) {
                continue;
            }
            expandedNodes++;
            visited.add(cell);
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY)) {
                    String move = getMove(dx[i], dy[i]);
                    s.push(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }
            //station
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                s.push(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
            }
            //ship
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    s.push(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
                    s.push(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }
        }
    }

    public static void IDS() {
        int i = 0;
        Stack<Node> s = new Stack<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        s.push(start);
        goal = null;
        while (goal == null) {
            s = new Stack<>();
            start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
            s.push(start);
            cost.setX(0);
            cost.setY(0);
            DFS2(s, i++);
        }
    }

    private static void UC() {
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeUC());
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        pq.add(start);
        pqTraversal(pq);
    }

    public static void aStar(int heuristic) {
        PriorityQueue<Node> pq;
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        if (heuristic == 1) {
            pq = new PriorityQueue<>(new NodeH1Star());
        } else {
            pq = new PriorityQueue<>(new NodeH2Star());
        }
        pq.add(start);
        pqTraversal(pq);
    }

    public static void greedy(int heuristic) {
        PriorityQueue<Node> pq;
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), 0, 0, 0);
        if (heuristic == 1) {
            pq = new PriorityQueue<>(new NodeH1());
        } else {
            pq = new PriorityQueue<>(new NodeH2());
        }
        pq.add(start);
        pqTraversal(pq);
    }

    private static void pqTraversal(PriorityQueue<Node> pq) {
        HashSet<VisitedCell> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
                cur.setCurCapacitiy(newCapacity);
                cur.setSavedPassengers(cur.getSavedPassengers() + takenPassengers);
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
                cur.setBlackBoxesSaved(cur.getBlackBoxesSaved() + 1);
            }

            if (cur.getPrevAction().equals("drop"))
                cur.setCurCapacitiy(0);

            if (!cur.getPrevAction().equals("")) {
                updateGridShips(ships);
            }
            cur.setDeaths(cur.getDeaths() + cost.getX());
            cur.setBlackBoxesDamaged(cur.getBlackBoxesDamaged() + cost.getY());

            if (isGoal(cur.getCurCapacitiy(), ships)) {
                goal = cur;
                break;
            }

            VisitedCell cell = new VisitedCell(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY(), cur.getCurCapacitiy(), cur.getSavedPassengers(), cur.getBlackBoxesSaved());
            if (visited.contains(cell)) {
                continue;
            }
            expandedNodes++;
            visited.add(cell);

            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY)) {
                    String move = getMove(dx[i], dy[i]);
                    pq.add(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }
            //station
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                pq.add(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
            }
            //ship
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    pq.add(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
                    pq.add(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, cur.getDepth() + 1, cur.getSavedPassengers(), cur.getBlackBoxesSaved()));
                }
            }


        }
    }

    static HashMap<Pair, Ship> deepClone(HashMap<Pair, Ship> ships) {
        HashMap<Pair, Ship> copy = new HashMap<>();
        for (Map.Entry<Pair, Ship> e : ships.entrySet()) {
            copy.put(e.getKey(), Ship.deepCloneShip(e.getValue()));
        }
        return copy;
    }

    public static String buildPlan(Node goal, boolean visualize) {
        Node tmp = goal;
        StringBuilder plan = new StringBuilder();
        StringBuilder visual = new StringBuilder();
        StringBuilder stationInfo = getStationInfo();
        while (tmp != null) {
            StringBuilder action = (new StringBuilder(tmp.getPrevAction())).reverse();
            plan.append(action + ",");
            if (visualize) {
                StringBuilder num = (new StringBuilder("--------------- Action #" + tmp.getDepth() + " ---------------")).reverse();
                StringBuilder action2 = (new StringBuilder("Action: " + tmp.getPrevAction())).reverse();
                StringBuilder deaths = (new StringBuilder("Deaths: " + tmp.getDeaths())).reverse();
                StringBuilder blackBoxes = (new StringBuilder("Damaged Black Boxes: " + tmp.getDeaths())).reverse();
                StringBuilder capacity = (new StringBuilder("Coast Guard Capacity: " + tmp.getCurCapacitiy())).reverse();
                StringBuilder shipInfo = getShipInfo(tmp);
                StringBuilder coor = (new StringBuilder("Coast Guard Coordinates: " + tmp.getCgCoordinates().toString())).reverse();

                visual.append(stationInfo + "\n" + shipInfo + "\n" + blackBoxes + "\n" + deaths + "\n" + capacity + "\n" + coor + "\n" + action2 + "\n" + num + "\n");
            }
            tmp = tmp.getParent();
        }
        plan.deleteCharAt(plan.length() - 1);
        plan.deleteCharAt(plan.length() - 1);
        plan.reverse();
        plan.append(";" + goal.getDeaths());
        plan.append(";" + (goal.getShips().size() - goal.getBlackBoxesDamaged()));
        plan.append(";" + expandedNodes);
        System.out.println(plan);
        if (visualize)
            System.out.println("\n" + "PLAN VISUALIZATION: " + "\n" + visual.reverse());
        return plan.toString();
    }

    private static StringBuilder getStationInfo() {
        StringBuilder info = new StringBuilder("    Stations Locations" + "\n");
        for (Pair st : stations) {
            info.append(st.toString() + "\n");
        }
        return info.reverse();
    }

    private static StringBuilder getShipInfo(Node tmp) {
        StringBuilder info = new StringBuilder("        Ships");
        for (Map.Entry<Pair, Ship> s : tmp.getShips().entrySet()) {
            String loc = "code.Ship at Location: " + s.getKey().toString();
            String passengers = "Passengers: " + s.getValue().getNoOfPassengers();
            info.append("\n" + loc + "\n" + passengers + "\n" + "Black Box Ticks: " + s.getValue().getBlackBoxTicks() + "\n");
        }
        return info.reverse();
    }

    public static void updateGridShips(HashMap<Pair, Ship> ships) {
        cost.setX(0);
        cost.setY(0);
        for (Pair p : ships.keySet()) {
            Pair shipCost = ships.get(p).updateShip();
            cost.setX(shipCost.getX() + cost.getX());
            cost.setY(shipCost.getY() + cost.getY());
        }
    }

    public static boolean isGoal(int curCapacity, HashMap<Pair, Ship> ships) {
        if (curCapacity != 0)
            return false;
        for (Map.Entry<Pair, Ship> s : ships.entrySet()) {
            if (s.getValue().getNoOfPassengers() > 0)
                return false;
            if (!(s.getValue().isBlackBoxRetrieved()) && s.getValue().getBlackBoxTicks() < 20)
                return false;
        }
        return true;
    }

    public static String getMove(int dx, int dy) {
        if (dx == 0 && dy == 1)
            return "right";
        if (dx == 0 && dy == -1)
            return "left";
        if (dx == 1 && dy == 0)
            return "down";
        else
            return "up";
    }

    static boolean validCell(int newX, int newY) {
        return newX >= 0 && newX < gridRows && newY >= 0 && newY < gridCols;
    }

    public static int distance(Pair p1, Pair p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }
    public static void main(String[] args) {

    }
}
