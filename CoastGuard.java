import java.util.*;

public class CoastGuard extends GeneralSearch {
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {-1, 0, 1, 0};
    private static int maxCapacity;
    private static int cgX, cgY; //coastGuard location
    private static HashSet<Pair> stations;
    private static HashMap<Pair, Ship> initShips;
    private static Pair cost = new Pair(0, 0);
    static private Object[][] grid;
    static StringBuilder plan;
    static int expandedNodes;
//    static int savedPassengers;
    static Node goal;
//    static HashSet<VisitedCell> visited;
    static boolean goalFound = false;


    public CoastGuard(int maxCapacity) {
        maxCapacity = this.maxCapacity;//range 30 to 100 inclusive
        stations = new HashSet<>();
        initShips = new HashMap<>();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    private static Object[][] convertToGrid(String grid) {
        String[] gridSplit = grid.split(";");
        //create grid
        String[] dimension = gridSplit[0].split(",");
        int m = Integer.parseInt(dimension[0]);
        int n = Integer.parseInt(dimension[1]);
        Object[][] gridArr = new Object[n][m];
        //add coast guard
        String[] coastGuard = gridSplit[2].split(",");
        cgX = Integer.parseInt(coastGuard[0]);
        cgY = Integer.parseInt(coastGuard[1]);
        //shouldn't it be yx?
        gridArr[cgX][cgY] = new CoastGuard(Integer.parseInt(gridSplit[1]));
        maxCapacity = Integer.parseInt(gridSplit[1]);
        //add stations
        int x, y;
        String[] stationsLocations = gridSplit[3].split(",");
        for (int i = 0; i < stationsLocations.length - 1; i += 2) {
            x = Integer.parseInt(stationsLocations[i]);
            y = Integer.parseInt(stationsLocations[i + 1]);
            gridArr[x][y] = new Station();
            stations.add(new Pair(x, y));
        }
        //add ships
        String[] shipsLocations = gridSplit[4].split(",");
        Ship s;
        for (int i = 0; i < shipsLocations.length - 2; i += 3) {
            x = Integer.parseInt(shipsLocations[i]);
            y = Integer.parseInt(shipsLocations[i + 1]);
//            s = new Ship(Integer.parseInt(shipsLocations[i + 2]), x, y);
            s = new Ship(Integer.parseInt(shipsLocations[i + 2]));
            gridArr[x][y] = s;
            initShips.put(new Pair(x, y), s);
        }
        return gridArr;
    }

    public static String genGrid() {
        StringBuilder grid = new StringBuilder();
        int n = (int) (Math.random() * 11 + 5); //5<=n<=15 - i - rows
        int m = (int) (Math.random() * 11 + 5); //5<=m<=15 - j - columns
        grid.append(m + "," + n + ";");

        //randomize ships and stations
        // from 1 to n*m-2 to leave a cell for CG and a station
        int ships = (int) (Math.random() * ((m * n - 2) + 1));
        //min between the random number and the remaining cells
        int stations = Math.min((int) (Math.random() * ((m * n - 2) + 1)), n * m - 1 - ships);

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
                grid.append((int) (Math.random() * 71 + 30) + ";" + x + "," + y + ";");
            else if (i < stations)
                grid.append(x + "," + y + ",");
            else if (i == stations + 1)
                grid.append(x + "," + y + ";");
            else
                grid.append(x + "," + y + "," + (int) (Math.random() * 101 + 1) + ",");

        }

        grid.setCharAt(grid.length() - 1, ';');

        return grid.toString();
    }

    public static String solve(String gridStr, String strategy, Boolean visualize) {
//        savedPassengers=0;
//        visited= new HashSet<>();
        expandedNodes = 0;
        cost.setX(0);
        cost.setY(0);
        grid = convertToGrid(gridStr);
        plan = new StringBuilder("");
        goal= null;
        switch (strategy) {
            case ("BF"):
             BFS();
                break;
            case ("DF"):
                DFS();
                break;
            case ("ID"):
                IDS();
                break;
            case ("GR1"):
                greedy(1);
                break;
            case ("GR2"):
                break;
            case ("AS1"):
                break;
            case ("AS2"):
                break;
        }
        return buildPlan(goal);
    }

//    public static void DFS(int xStart, int yStart, StringBuilder p) {
//        if (isGoal()&&!goalFound){
//            plan= new StringBuilder(p);
//            goalFound= true;
//            return;
//        }
//        if(!goalFound){
//        if (grid[xStart][yStart] instanceof Ship && !((Ship) grid[xStart][yStart]).isBlackBoxRetrieved()) {
//            Ship ship = (Ship) grid[xStart][yStart];
//            //pickup
//            if (ship.getNoOfPassengers() > 0 && currCapacity < maxCapacity) {
//                int takenPassengers = Math.min(maxCapacity-currCapacity, ship.getNoOfPassengers());
//                currCapacity += takenPassengers;
//                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
//                updateGridShips();
//                p.append("pickup,");
//                DFS(xStart, yStart,p);
//                p.delete(p.length()-7, p.length());
//                unUpdateGridShips();
//                ship.setNoOfPassengers(ship.getNoOfPassengers() + takenPassengers);
//                currCapacity -= takenPassengers;
//            }
//            //retrieve
//            if (ship.getNoOfPassengers() == 0) {
//                ship.setBlackBoxRetrieved(true);
//                updateGridShips();
//                p.append("retrieve,");
//                DFS(xStart, yStart, p);
//                p.delete(p.length()-9, p.length());
//                unUpdateGridShips();
//                ship.setBlackBoxRetrieved(false);
//            }
//        }
//        //drop
//        if (grid[xStart][yStart] instanceof Station) {
//            //drop
//            int passengersToDrop = currCapacity;
//            currCapacity = 0;
//            updateGridShips();
//            p.append("drop,");
//            DFS(xStart, yStart, p);
//            p.delete(p.length()-5, p.length());
//            unUpdateGridShips();
//            currCapacity = passengersToDrop;
//            //un-drop
//        }
//
//        for (int i = 0; i < 4; i++) {
//            //update ships
//            int newX = xStart + dx[i];
//            int newY = yStart + dy[i];
//            if (validCell(newX, newY)) {
//                updateGridShips();
//                String move=getMove(dx[i],dy[i]);
//                p.append(move);
//                DFS(newX, newY,p);
//                p.delete(p.length()-move.length(), p.length());
//                unUpdateGridShips();
//            }
//            // should go to original state here to continue actions if there is
//        }
//        }
//    }

    public static void BFS() {
        int savedPassengers=0;
        HashSet<VisitedCell> visited= new HashSet<>();
        System.out.println(visited.size());
        expandedNodes = 0;
        cost.setX(0);
        cost.setY(0);
        plan = new StringBuilder("");
        goal= null;
        Queue<Node> q = new LinkedList<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), new HashSet<>(), 0);
        q.add(start);
        while (!q.isEmpty()) {
            Node cur = q.poll();
            expandedNodes++;
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                savedPassengers += takenPassengers;
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
//                if(ship.getNoOfPassengers()==0)
//                {
//                    ship.setBlackBoxTicks(-1);
//                }
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
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

            VisitedCell cell = new VisitedCell(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY(), cur.getCurCapacitiy(), savedPassengers);
            if (visited.contains(cell)) {
                continue;
            }
            visited.add(cell);

//            HashSet<Pair> hs;
//            if (!(cur.getPrevAction().equals("pickup") || cur.getPrevAction().equals("drop") || cur.getPrevAction().equals("retrieve"))) {
//                hs = deepCloneHS(cur.getVisitedCells());
//                hs.add(coord);
//            } else {
//                hs = new HashSet<>();
//            }
            HashSet<Pair> hs = new HashSet<>();
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    q.add(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
                    q.add(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
            }
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                q.add(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
            }
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY)) {
                    String move = getMove(dx[i], dy[i]);
                    q.add(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, hs, cur.getDepth() + 1));
                }
            }

        }
    }

    public static void DFS() {
        Stack<Node> s = new Stack<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), new HashSet<>(), 0);
        s.push(start);
        while (!s.empty()) {
            Node cur = s.pop();
            expandedNodes++;
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
//                if(ship.getNoOfPassengers()==0)
//                {
//                    ship.setBlackBoxTicks(-1);
//                }
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
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
                System.out.println(depth(goal));
                break;
            }

            HashSet<Pair> hs;
            if (!(cur.getPrevAction().equals("pickup") || cur.getPrevAction().equals("drop") || cur.getPrevAction().equals("retrieve"))) {
                hs = deepCloneHS(cur.getVisitedCells());
                hs.add(coord);
            } else {
                hs = new HashSet<>();
            }
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY) && !hs.contains(newCoord)) {
                    String move = getMove(dx[i], dy[i]);
                    s.push(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, hs, cur.getDepth() + 1));
                }
            }
            //station
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                s.push(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
            }
            //ship
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    s.push(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
                    s.push(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
            }


        }

    }

    static int depth(Node n) { //todo - check if this method works correctly
        int c = 0;
        while (!n.getPrevAction().equals("")) {
            c++;
            n = n.getParent();
        }
        return c;
    }

    public static void DFS2(Stack s, int maxDepth) {
//        System.out.println();
        while (!s.empty() && goal == null) {
            Node cur = (Node) s.pop();
            if (depth(cur) > maxDepth)
                continue;
//            System.out.println(cur.getPrevAction());
//            System.out.println(cur.getCgCoordinates().getX()+", "+ cur.getCgCoordinates().getY());
            expandedNodes++;
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
//                if(ship.getNoOfPassengers()==0)
//                {
//                    ship.setBlackBoxTicks(-1);
//                }
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
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

            HashSet<Pair> hs;
            if (!(cur.getPrevAction().equals("pickup") || cur.getPrevAction().equals("drop") || cur.getPrevAction().equals("retrieve"))) {
                hs = deepCloneHS(cur.getVisitedCells());
                hs.add(coord);
            } else {
                hs = new HashSet<>();
            }
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY) && !hs.contains(newCoord)) {
                    String move = getMove(dx[i], dy[i]);
                    s.push(new Node(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, hs, cur.getDepth() + 1));
                }
            }
            //station
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                s.push(new Node("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
            }
            //ship
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    s.push(new Node("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
//                    System.out.println(cur.getPrevAction()+ ship.isBlackBoxRetrieved());
                    s.push(new Node("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
            }


        }

    }

    public static void IDS() {
        int i = 10;
        Stack<Node> s = new Stack<>();
        Node start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), new HashSet<>(), 0);
        s.push(start);
        goal = null;
        while (goal == null) {
            s = new Stack<>();
            start = new Node("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), new HashSet<>(), 0);
            s.push(start);
            cost.setX(0);
            cost.setY(0);
            DFS2(s, i++);
        }
    }

    static HashMap<Pair, Ship> deepClone(HashMap<Pair, Ship> ships) {
        HashMap<Pair, Ship> copy = new HashMap<>();
        for (Map.Entry<Pair, Ship> e : ships.entrySet()) {
            copy.put(e.getKey(), Ship.deepCloneShip(e.getValue()));
        }
        return copy;
    }

    static HashSet<Pair> deepCloneHS(HashSet<Pair> cells) {
        HashSet<Pair> copy = new HashSet<>();
        for (Pair p : cells) {
            copy.add(p.deepClonePair());
        }
        return copy;
    }

    public static String buildPlan(Node goal) {
        Node tmp = goal;
        StringBuilder plan = new StringBuilder("");
        while (tmp != null) {
            StringBuilder action = (new StringBuilder(tmp.getPrevAction())).reverse();
            plan.append(action + ",");
            tmp = tmp.getParent();
        }
        plan.deleteCharAt(plan.length() - 1);
        plan.deleteCharAt(plan.length() - 1);
        plan.reverse();
        plan.append(";" + goal.getDeaths());
//        System.out.println(goal.getBlackBoxesDamaged());
        plan.append(";" + (goal.getShips().size() - goal.getBlackBoxesDamaged()));
        plan.append(";" + expandedNodes);
        System.out.println(plan);
        return plan.toString();
    }

    public static void updateGridShips(HashMap<Pair, Ship> ships) {
        cost.setX(0);
        cost.setY(0);
        int i = 0;
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

//    public static void unUpdateGridShips(HashMap<Pair, Ship> ships) {
//        for (Pair p : ships.keySet()) {
//
//            ships.get(p).unUpdateShip();
//        }
//    }

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
        return newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length;
    }

    public static void greedy(int heuristic) {
        NodeH1 start = new NodeH1("", initShips, null, 0, 0, 0, new Pair(cgX, cgY), new HashSet<>(), 0);
        PriorityQueue<NodeH1> pq = new PriorityQueue<>();
        pq.add(start);
        if (heuristic == 1)
            greedyH1(pq);
        else
            greedyH2();
    }

    public static void greedyH1(PriorityQueue<NodeH1> pq) {
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            expandedNodes++;
            Pair coord = new Pair(cur.getCgCoordinates().getX(), cur.getCgCoordinates().getY());
            HashMap<Pair, Ship> ships = deepClone(cur.getShips());
            if (cur.getPrevAction().equals("pickup")) {
                Ship ship = ships.get(coord);
                int takenPassengers = Math.min(maxCapacity - cur.getCurCapacitiy(), ship.getNoOfPassengers());
                int newCapacity = cur.getCurCapacitiy() + takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
                cur.setCurCapacitiy(newCapacity);
            }
            if (cur.getPrevAction().equals("retrieve")) {
                ships.get(coord).setBlackBoxRetrieved(true);
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
                System.out.println(depth(goal));
                break;
            }

            HashSet<Pair> hs;
            if (!(cur.getPrevAction().equals("pickup") || cur.getPrevAction().equals("drop") || cur.getPrevAction().equals("retrieve"))) {
                hs = deepCloneHS(cur.getVisitedCells());
                hs.add(coord);
            } else {
                hs = new HashSet<>();
            }
            //move
            for (int i = 0; i < 4; i++) {
                //update ships
                int newX = coord.getX() + dx[i];
                int newY = coord.getY() + dy[i];
                Pair newCoord = new Pair(newX, newY);
                if (validCell(newX, newY) && !hs.contains(newCoord)) {
                    String move = getMove(dx[i], dy[i]);
                    pq.add(new NodeH1(move, ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), newCoord, hs, cur.getDepth() + 1));
                }
            }
            //station
            if (stations.contains(coord) && cur.getCurCapacitiy() > 0) {
                //drop
                pq.add(new NodeH1("drop", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
            }
            //ship
            if (ships.containsKey(coord) && !ships.get(coord).isBlackBoxRetrieved()) {
                Ship ship = ships.get(coord);
                //pickup
                if (ship.getNoOfPassengers() > 0 && cur.getCurCapacitiy() < maxCapacity) {

                    pq.add(new NodeH1("pickup", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
                //retrieve
                if (ship.getNoOfPassengers() == 0) {
//                    System.out.println(cur.getPrevAction()+ ship.isBlackBoxRetrieved());
                    pq.add(new NodeH1("retrieve", ships, cur, cur.getDeaths(), cur.getBlackBoxesDamaged(), cur.getCurCapacitiy(), coord, hs, cur.getDepth() + 1));
                }
            }


        }


//        Node curr;
//        while (!pq.isEmpty()) {
//            curr = pq.remove();
//            expandedNodes++;
////            System.out.println(curr.getPrevAction()+", ");
//            //check curr is goal
//            if (curr.isGoal()) {
//                goal = curr;
//                return;
//            }
//            //expand curr - add possible children to queue
//            HashMap<Pair, Ship> ships = deepClone(curr.getShips());
//            if(!curr.getPrevAction().equals(""))
//                updateGridShips(ships);
//            Pair coord = new Pair(curr.getCgCoordinates().getX(), curr.getCgCoordinates().getY());
//            if (ships.containsKey(coord) && !(ships.get(coord).isBlackBoxRetrieved())) {
//                Ship s = ships.get(coord);
//                int takenPassengers = pickup(s, curr.getCurCapacitiy());
//                if (takenPassengers > 0) {
//                    //pickup
//                    int newCapacity = curr.getCurCapacitiy() + takenPassengers;
//                    ships.put(coord, new Ship(s.getNoOfPassengers() - takenPassengers));
//                    pq.add(new NodeH1("pickup", ships, curr, cost.getX() + curr.getDeaths(), cost.getY() + curr.getBlackBoxesDamaged(), newCapacity, coord));
//                } else if (s.getNoOfPassengers() == 0 ) {
//                    //retrieve
//                    s.setBlackBoxRetrieved(true);
//                    pq.add(new NodeH1("retrieve", ships, curr, cost.getX() + curr.getDeaths(), cost.getY() + curr.getBlackBoxesDamaged(), curr.getCurCapacitiy(), coord));
//                }
//            }
//            if (stations.contains(coord) && curr.getCurCapacitiy() > 0) {
//                //drop-off
//                pq.add(new NodeH1("drop", ships, curr, cost.getX() + curr.getDeaths(), cost.getY() + curr.getBlackBoxesDamaged(), 0, coord));
//            } else {
//                //move
//                for (int i = 0; i < 4; i++) {
//                    int newX = coord.getX() + dx[i];
//                    int newY = coord.getY() + dy[i];
//                    if (validCell(newX, newY) &&
//                            (curr.getParent() == null || (curr.getParent() != null && !(newX == curr.getParent().getCgCoordinates().getX() && newY == curr.getParent().getCgCoordinates().getY())))) {
////                        if(curr.getParent()!=null) System.out.println("cg "+cgX+","+cgY+" new "+newX+", "+ newY+" parent "+ curr.getParent().getCgCoordinates().getX()+", "+curr.getParent().getCgCoordinates().getY());
//                        pq.add(new NodeH1(getMove(dx[i], dy[i]), ships, curr, cost.getX() + curr.getDeaths(), cost.getY() + curr.getBlackBoxesDamaged(), curr.getCurCapacitiy(), new Pair(newX, newY)));
//                    }
//                }
//            }
//        }
    }

    public static int minDistStation(Pair cg) {
        int min = Integer.MAX_VALUE;
        for (Pair p : stations) {
            min = Math.min(min, distance(cg, p));
        }
        return min;
    }

    public static int distance(Pair p1, Pair p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    public static int pickup(Ship ship, int currCap) {
        //return noOfPassengers picked up, or 0 if pickup action not valid
        return (ship.getNoOfPassengers() > 0 && currCap < maxCapacity) ? Math.min(maxCapacity - currCap, ship.getNoOfPassengers()) : 0;
    }

    public static void greedyH2() {
        //todo
    }


    public static void main(String[] args) {
//        Pair p1= new Pair(5,5);
//        Pair p2= new Pair(3,4);
//        Pair p3= new Pair(1,2);
//        HashMap<Pair, Ship> hm= new HashMap<>();
//        hm.put(p1, new Ship(5,5,20));
//        hm.put(p2, new Ship(5,5,20));
//        hm.put(p3, new Ship(5,5,20));
//        System.out.println(hm.containsKey(new Pair(1,1)));


    }
}
