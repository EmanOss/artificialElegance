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

    public static void solve(String grid, String strategy, Boolean visualize) {
//        todo - convert grid to 2d arr, extract info
    }

    public static void main(String[] args) {
        System.out.println(genGrid());
    }

}
