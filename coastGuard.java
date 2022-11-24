import java.util.Arrays;

public class coastGuard extends GeneralSearch {
    private int currCapacity;
    private int maxCapacity;

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
        String [] gridSplit = grid.split(";");
        //create grid
        int m = Integer.parseInt(gridSplit[0].split(",")[0]);
        int n = Integer.parseInt(gridSplit[0].split(",")[1]);
        Object[][] gridArr = new Object[n][m];
        //add coast guard
        int x = Integer.parseInt(gridSplit[2].split(",")[0]);
        int y = Integer.parseInt(gridSplit[2].split(",")[1]);
        gridArr[x][y]= new coastGuard(Integer.parseInt(gridSplit[1]));
        //add stations
        String [] stationsLocations = gridSplit[3].split(",");
        for(int i=0; i< stationsLocations.length-1;i+=2){
            x = Integer.parseInt(stationsLocations[i]);
            y = Integer.parseInt(stationsLocations[i+1]);
            gridArr[x][y]=new Station();
        }
        //add ships
        String [] shipsLocations = gridSplit[4].split(",");
        for(int i=0; i< shipsLocations.length-2;i+=3){
            x = Integer.parseInt(shipsLocations[i]);
            y = Integer.parseInt(shipsLocations[i+1]);
            gridArr[x][y]=new Ship(Integer.parseInt(shipsLocations[i+2]),x,y);
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
            int y = (int) (Math.random() * n);
            int x = (int) (Math.random() * m); //col
            while (assigned[y][x]) {
                y = (int) (Math.random() * n);
                x = (int) (Math.random() * m);
            }
            assigned[y][x] = true;
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
    public static void solve(String grid, String strategy, Boolean visualize){
        Object [][] gridArr = convertToGrid(grid);
        switch(strategy) {
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



    public static void main(String[] args) {
//        System.out.println(genGrid());
//        Object [][]arr = convertToGrid("3,4;97;1,2;0,1,3,0;3,2,65,0,0,10;");
//        for(Object [] x:arr)
//        System.out.println(Arrays.toString(x));
    }

}
