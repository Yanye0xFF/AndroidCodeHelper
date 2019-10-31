package bean;
public class Pointi { 

    private int x;
    private int y;

    public Pointi() {
    }

    public Pointi(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void copyFrom(Pointi item) {
        this.x = item.getX();
        this.y = item.getY();
    }

    public String toString() {
        return "Pointi [ "+"x="+this.x+","+"y="+this.y+" ]";
    }

}
