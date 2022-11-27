public class Ship {
    private int noOfPassengers;
    private int blackBoxTicks;


    private boolean blackBoxRetrieved;
    private int x, y;

    public Ship(int noOfPassengers, int x, int y) {
        this.noOfPassengers = noOfPassengers;
        this.blackBoxTicks = 0;
        this.x = x;
        this.y = y;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public int getBlackBoxTicks() {
        return blackBoxTicks;
    }

    public void setBlackBoxTicks(int blackBoxTicks) {
        this.blackBoxTicks = blackBoxTicks;
    }

    public boolean isBlackBoxRetrieved() {
        return blackBoxRetrieved;
    }

    public void setBlackBoxRetrieved(boolean blackBoxRetrieved) {
        this.blackBoxRetrieved = blackBoxRetrieved;
    }

    public void updateShip() {
        if (!blackBoxRetrieved) {
            if (noOfPassengers > 0)
                noOfPassengers--;
            else
                blackBoxTicks = Math.min(blackBoxTicks + 1, 20);
        }
    }

    public void unUpdateShip() {
        if (!blackBoxRetrieved) {
            if (this.blackBoxTicks > 0) {
                this.blackBoxTicks = Math.max(this.blackBoxTicks - 1, 0);
            } else {
                this.noOfPassengers++;
            }
        }

    }
}
