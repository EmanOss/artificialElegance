public class coastGuard implements generalSearch{
    private int currCapacity;
    private int maxCapacity;

    public coastGuard(int maxCapacity) {
        currCapacity = 0;
        maxCapacity =  this.maxCapacity;//range 30 to 100 inclusive
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

    public static void BFS(String grid, Boolean visualize) {

    }
    public static void DFS(String grid, Boolean visualize) {

    }


    public static void IDS(String grid, Boolean visualize) {

    }
    public static void greedyH1(String grid, Boolean visualize) {


    }

    public static void greedyH2(String grid, Boolean visualize) {

    }


    public static void aStarH1(String grid, Boolean visualize) {

    }

    public static void aStarH2(String grid, Boolean visualize) {

    }
    public static String genGrid(){
        StringBuilder grid= new StringBuilder();
        int n = (int)(Math.random()*11+5); //5<=n<=15 - i - rows
        int m = (int)(Math.random()*11+5); //5<=m<=15 - j - columns
        grid.append(m+","+n+";");

        //todo- randomize no. of ships & stations

        boolean [][] assigned = new boolean[n][m];
        for (int i=0;i<5;i++) {
            int y = (int) (Math.random() * n); //row
            int x = (int) (Math.random() * m); //col
            while (assigned[y][x]) {
                y = (int) (Math.random() * n);
                x = (int) (Math.random() * m);
            }
            assigned[y][x]=true;
            if(i==0)
                grid.append((int)(Math.random()*71+30)+";"+ x+","+y+";");
            else if(i==1)
                grid.append(x+","+y+",");
            else if(i==2)
                grid.append(x+","+y+";");
            else{
                grid.append(x+","+y+","+(int)(Math.random()*101+1)+",");
            }
        }
        grid.setCharAt(grid.length()-1,';');

        return grid.toString();
    }
    public static void solve(String grid, String strategy, Boolean visualize){
//        todo - convert grid to 2d arr, extract info
        switch(strategy) {
            case ("BF"):
            case ("DF"):
            case ("ID"):
            case ("GR1"):
            case ("GR2"):
            case ("AS1"):
            case ("AS2"):
        }
    }

    public static void main(String[] args) {
        System.out.println(genGrid());
    }

}
