import java.util.HashMap;
import java.util.HashSet;

public class CoastGuard extends GeneralSearch {
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {-1, 0, 1, 0};
    private static int currCapacity;
    private static int maxCapacity;
    private static int cgX, cgY; //coastGuard location
    private static HashSet<Pair> stations;
    private static HashMap<Pair, Ship> ships;

    static private Object[][] grid;
    static StringBuilder plan;

    public CoastGuard(int maxCapacity) {
        currCapacity = 0;
        maxCapacity = this.maxCapacity;//range 30 to 100 inclusive
        stations= new HashSet<>();
        ships= new HashMap<>();
    }

    public int getCurrCapacity() {
        return currCapacity;
    }

    public void setCurrCapacity(int currCapacity) {
        this.currCapacity = currCapacity;
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
        int x,y;
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
            s = new Ship(Integer.parseInt(shipsLocations[i + 2]), x, y);
            gridArr[x][y] = s;
            ships.put(new Pair(x, y), s);
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
        grid = convertToGrid(gridStr);
        plan = new StringBuilder("");
        switch (strategy) {
            case ("BF"):
            case ("DF"):
                DFS(cgX, cgY);
            case ("ID"):
            case ("GR1"):
//                greedyH1(grid, visualize);
//                break;
            case ("GR2"):
            case ("AS1"):
            case ("AS2"):
        }
        return plan.toString();
    }

    public static void DFS(int xStart, int yStart) {
        if (isGoal()){
            System.out.println(plan.toString());
            return;

        }
        if (grid[xStart][yStart] instanceof Ship && !((Ship) grid[xStart][yStart]).isBlackBoxRetrieved()) {
            Ship ship = (Ship) grid[xStart][yStart];
            //pickup
            if (ship.getNoOfPassengers() > 0 && currCapacity < maxCapacity) {
                int takenPassengers = Math.min(maxCapacity-currCapacity, ship.getNoOfPassengers());
                currCapacity += takenPassengers;
                ship.setNoOfPassengers(ship.getNoOfPassengers() - takenPassengers);
                updateGridShips();
                plan.append("pickup,");
                DFS(xStart, yStart);
                plan.delete(plan.length()-7, plan.length());
                unUpdateGridShips();
                ship.setNoOfPassengers(ship.getNoOfPassengers() + takenPassengers);
                currCapacity -= takenPassengers;
            }
            //retrieve
            if (ship.getNoOfPassengers() == 0) {
                ship.setBlackBoxRetrieved(true);
                updateGridShips();
                plan.append("retrieve,");
                DFS(xStart, yStart);
                plan.delete(plan.length()-9, plan.length());
                unUpdateGridShips();
                ship.setBlackBoxRetrieved(true);
            }
        }
        //drop
        if (grid[xStart][yStart] instanceof Station) {
            //drop
            int passengersToDrop = currCapacity;
            currCapacity = 0;
            updateGridShips();
            plan.append("drop,");
            DFS(xStart, yStart);
            plan.delete(plan.length()-5, plan.length());
            unUpdateGridShips();
            currCapacity = passengersToDrop;
            //un-drop
        }

        for (int i = 0; i < 4; i++) {
            //update ships
            int newX = xStart + dx[i];
            int newY = yStart + dy[i];
            if (validCell(newX, newY)) {
                updateGridShips();
                String move=getMove(dx[i],dy[i]);
                plan.append(move);
                DFS(newX, newY);
                plan.delete(plan.length()-move.length(), plan.length());
                unUpdateGridShips();
            }

            // should go to original state here to continue actions if there is
        }
    }

    public static void updateGridShips() {
        for (Pair p: ships.keySet()) {
            ships.get(p).updateShip();
        }
    }

    public static void unUpdateGridShips() {
        for (Pair p: ships.keySet()) {

            ships.get(p).unUpdateShip();
        }
    }

    public static String getMove(int dx, int dy)
    {
        if(dx==0 && dy==1)
            return "right,";
        if(dx==0 && dy==-1)
            return "left,";
        if(dx==1 && dy==0)
            return "down,";
        else
            return "up,";
    }
    static boolean validCell(int newX, int newY) {
        return newX > 0 && newX < grid.length && newY > 0 && newY < grid[0].length;
    }

    static int distance(Pair p) {
        return (p.getX() - cgX) + (p.getY() - cgY);
    }

    //    get distance to nearest ship
    static int minDist() {
        int min = 0; //todo - make sure en 0 tmam
        for (Pair p : ships.keySet()) {
            min = Math.min(min, distance(p));
        }
        return min;
    }

    public static void greedy(int heuristic) {
        Node root = new Node("root", ships, null, 0, 0);
        if (heuristic == 1)
            greedyH1(root);
        else
            greedyH2();
    }

    public static void greedyH1(Node root) {
        //check root is goal
        if (root.isGoal()) {
            getPath(root);
            return;
        }
        //expand root - add possible children to queue
        String prevAction = "";
        //todo - update grid/ships
        if (grid[cgX][cgY] instanceof Ship s) {
            if (currCapacity < maxCapacity && s.getNoOfPassengers() > 0) {
                //pickup
                pickup(s);
                prevAction = "pickup";
//                Node c1 = new Node(prevAction,ships,root,root.getDeaths()+?, root.getBlackBoxesDamaged()+?);
            } else if (s.getNoOfPassengers() == 0 && !(s.isBlackBoxRetrieved())) {
                //retrieve
                s.setBlackBoxRetrieved(true);
                //todo - update blackboxes count somewhere
                prevAction = "retrieve";
            }
        }
        if (grid[cgX][cgY] instanceof Station) {
            //dropoff
            currCapacity = 0;
            prevAction = "drop";
        } else {
            //move
            for (int i = 0; i < 4; i++) {
                int newX = cgX + dx[i];
                int newY = cgY + dy[i];
                if (validCell(newX, newY)) {

                }
            }
        }
        //todo - add children to queue
        //todo - in order to create new Nodes, i need to calculate cost as parent cost + action cost
        //todo - action cost calculated feen? - update unUpdateGridShips, updateShip, ... ?
        //feen el queue asln
        //pop next and call greedy1 wla da f solve?
    }

    public static void pickup(Ship s) {
        int diff = maxCapacity - currCapacity;
        if (diff < s.getNoOfPassengers()) {
            s.setNoOfPassengers(s.getNoOfPassengers() - diff);
            currCapacity = maxCapacity;
        } else {
            s.setNoOfPassengers(0);
            currCapacity += diff;
        }
    }

    public static void greedyH2() {
        //todo
    }

    static int h1(Node n) {
        return n.getShips().size() * minDist();
    }

    public static void getPath(Node last) {
        //todo
    }

    public static void main(String[] args) {
//        System.out.println(genGrid());
//        Object [][]arr = convertToGrid("3,4;97;1,2;0,1,3,0;3,2,65,0,0,10;");
//        for(Object [] x:arr)
//        System.out.println(Arrays.toString(x));
    }

    public static boolean isGoal() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] instanceof Ship) {
                    Ship s = (Ship) grid[i][j];
                    if (s.getNoOfPassengers() > 0)
                        return false;
                    if (!(s.isBlackBoxRetrieved()) && s.getBlackBoxTicks() < 20)
                        return false;
                }
            }
        }
        return true;
    }

}
