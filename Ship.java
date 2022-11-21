public class Ship {
    private int noOfPassengers;
    private int blackBoxTicks;
    public Ship (){
        this.noOfPassengers = (int)(Math.random()*100+1);
        this.blackBoxTicks=0;
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
