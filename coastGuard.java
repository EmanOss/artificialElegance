import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class coastGuard extends GeneralSearch {
    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {-1, 0, 1, 0};
    private int currCapacity;
    private int maxCapacity;
    private static int x,y; //coastGuard location
    private static HashSet<Pair> stations;
    private static HashMap<Pair,Ship> ships;

    static private Object[][] grid;

    public coastGuard(int maxCapacity) {
        currCapacity = 0;
        maxCapacity = this.maxCapacity;//range 30 to 100 inclusive
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
        int x = Integer.parseInt(coastGuard[0]);
        int y = Integer.parseInt(coastGuard[1]);
        //shouldn't it be yx?
        gridArr[x][y] = new coastGuard(Integer.parseInt(gridSplit[1]));
        //add stations
        String[] stationsLocations = gridSplit[3].split(",");
        for (int i = 0; i < stationsLocations.length - 1; i += 2) {
            x = Integer.parseInt(stationsLocations[i]);
            y = Integer.parseInt(stationsLocations[i + 1]);
            gridArr[x][y] = new Station();
            stations.add(new Pair(x,y));
        }
        //add ships
        String[] shipsLocations = gridSplit[4].split(",");
        Ship s;
        for (int i = 0; i < shipsLocations.length - 2; i += 3) {
            x = Integer.parseInt(shipsLocations[i]);
            y = Integer.parseInt(shipsLocations[i + 1]);
            s = new Ship(Integer.parseInt(shipsLocations[i + 2]), x, y);
            gridArr[x][y] = s;
            ships.put(new Pair(x,y),s);
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

    public static void solve(String gridStr, String strategy, Boolean visualize) {
        grid = convertToGrid(gridStr);
        switch (strategy) {
            case ("BF"):
            case ("DF"):
            case ("ID"):
            case ("GR1"):
//                greedyH1(grid, visualize);
//                break;
            case ("GR2"):
            case ("AS1"):
            case ("AS2"):
        }
    }

    public static void DFS(int xStart, int yStart) {
        // check if goal is reached
        //if (ZeroPassengers and boxs)
//        return;


        //there are 7 actions
        //pickup or retrieve
        if (grid[xStart][yStart] instanceof Ship) {
            //updateaction
            updateGridShips();
            DFS(xStart, yStart);
            unUpdateGridShips();
            //unretrieve
        }
        //drop
        if (grid[xStart][yStart] instanceof Station) {
            //drop
            updateGridShips();
            DFS(xStart, yStart);
            unUpdateGridShips();
            //un-drop
        }

        for (int i = 0; i < 4; i++) {
            //update ships
            int newX = xStart + dx[i];
            int newY = yStart + dy[i];
            if (validCell(newX, newY)) {
                updateGridShips();
                DFS(newX, newY);
                unUpdateGridShips();
            }

            // should go to original state here to continue actions if there is
        }
    }

    public static void updateGridShips() {
        for(int i=0; i< ships.size();i++){
            ships.get(i).updateShip();
        }
    }

    public static void unUpdateGridShips() {
        for(int i=0; i< ships.size();i++){
            ships.get(i).unUpdateShip();
        }
    }

    static boolean validCell(int newX, int newY) {
        return newX > 0 && newX < grid.length && newY > 0 && newY < grid[0].length;
    }
    static int distance(Pair p){
        return (p.getX()-x) + (p.getY()-y);
    }

    public static void greedy(int heuristic) {
        if(heuristic==1)
            greedyH1();
        else
            greedyH2();
    }
    public static void greedyH1(){

    }
    public static void greedyH2(){

    }
    public static void main(String[] args) {
//        System.out.println(genGrid());
//        Object [][]arr = convertToGrid("3,4;97;1,2;0,1,3,0;3,2,65,0,0,10;");
//        for(Object [] x:arr)
//        System.out.println(Arrays.toString(x));
    }

}
