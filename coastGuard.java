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

    @Override
    public void BFS() {

    }

    @Override
    public void DFS() {

    }

    @Override
    public void IDS() {

    }

    @Override
    public void greedy(int heuristic) {

    }

    @Override
    public void aStar(int heuristic) {

    }
    public static String genGrid(){
        StringBuilder grid= new StringBuilder();
        int n = (int)(Math.random()*11+5); //5<=n<=15 - i - rows
        int m = (int)(Math.random()*11+5); //5<=m<=15 - j - columns
        grid.append(m+","+n+";");

        //2 ships, 2 stations

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

    }

    public static void main(String[] args) {
        System.out.println(genGrid());
    }

}
