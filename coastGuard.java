public class coastGuard implements generalSearch{
    private int currCapacity;
    private int maxCapacity;

    public coastGuard() {
        currCapacity = 0;
        maxCapacity = (int)(Math.random()*70+30); //range 30 to 100 inclusive
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
        return"";
    }
    public static void solve(String grid, String strategy, Boolean visualize){
//        todo - convert grid to 2d arr, extract info

    }

}
