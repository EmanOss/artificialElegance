public class Ship {
    private int noOfPassengers;
    private int blackBoxTicks;
    private boolean blackBoxRetrieved;
//    private int x, y;

    public Ship(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
        this.blackBoxTicks = 0;
//        this.x = x;
//        this.y = y;
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

    public Pair updateShip() {
        int dead=0;
        int boxDamaged=0;
        if (!blackBoxRetrieved) {
            if (noOfPassengers > 0) {
                dead=1;
                noOfPassengers--;
            }
            else {
                blackBoxTicks = Math.min(blackBoxTicks + 1, 20);
                if(blackBoxTicks==20)
                    boxDamaged=1;
            }
        }
        return new Pair(dead, boxDamaged);
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
    public static Ship deepCloneShip(Ship ship){
        Ship copy = new Ship(ship.getNoOfPassengers());
        copy.setBlackBoxRetrieved(ship.isBlackBoxRetrieved());
        copy.setBlackBoxTicks(ship.getBlackBoxTicks());
        return copy;
    }
}
