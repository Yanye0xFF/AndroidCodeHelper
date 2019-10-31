package bean;

public class Pointf { 

    private float x;
    private float y;

    public Pointf() {
    }

    public Pointf(float x,float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void copyFrom(Pointf item) {
        this.x = item.getX();
        this.y = item.getY();
    }

    public String toString() {
        return "Pointf [ "+"x="+this.x+","+"y="+this.y+" ]";
    }

}
