public class Ship {
    private int noOfPassengers;
    private int blackBoxTicks;
    private int x,y;

    public Ship (int noOfPassengers, int x, int y){
        this.noOfPassengers = noOfPassengers;
        this.blackBoxTicks=0;
        this.x=x;
        this.y=y;
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

    public void updateShip(){
        if(noOfPassengers>0)
            noOfPassengers--;
        else
            blackBoxTicks = Math.min(blackBoxTicks+1,20);
    }
}
