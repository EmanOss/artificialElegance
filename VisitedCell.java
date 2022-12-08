public class VisitedCell {
    private int x;
    private int y;
    private int curCapacity;
    private int savedSoFar;

    public int getBlackBoxesRetrieved() {
        return blackBoxesRetrieved;
    }

    public void setBlackBoxesRetrieved(int blackBoxesRetrieved) {
        this.blackBoxesRetrieved = blackBoxesRetrieved;
    }

    private int blackBoxesRetrieved;

    public VisitedCell(int x, int y, int curCapacity, int savedSoFar, int blackBoxesRetrieved){
        this.x= x;
        this.y= y;
        this.curCapacity= curCapacity;
        this.savedSoFar= savedSoFar;
        this.blackBoxesRetrieved=blackBoxesRetrieved;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCurCapacity() {
        return curCapacity;
    }

    public void setCurCapacity(int curCapacity) {
        this.curCapacity = curCapacity;
    }

    public int getSavedSoFar() {
        return savedSoFar;
    }

    public void setSavedSoFar(int savedSoFar) {
        this.savedSoFar = savedSoFar;
    }
    public String toString() {
        return "( "+x+" , "+y+" , "+curCapacity+" , "+savedSoFar+" )";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitedCell)) return false;
        VisitedCell cell = (VisitedCell) o;
        return x == cell.x && y == cell.y && curCapacity== cell.curCapacity && savedSoFar== cell.savedSoFar && blackBoxesRetrieved==cell.blackBoxesRetrieved;
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result =31* result+y;
        result= 31*result+ curCapacity;
        result= 31*result+ savedSoFar;
        result= 31*result+ blackBoxesRetrieved;
        return result;
    }
    public VisitedCell deepClonePair(){
        VisitedCell copy = new VisitedCell(this.x, this.y, this.curCapacity, this.savedSoFar, this.blackBoxesRetrieved);
        return copy;
    }

}
