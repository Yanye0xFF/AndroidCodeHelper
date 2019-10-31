package bean;

public class WindowPosition { 

    private int x;
    private int y;
    private int w;
    private int h;

    public WindowPosition() {
    }

    public WindowPosition(int x,int y,int w,int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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

    public int getW() {
        return this.w;
    }
    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }
    public void setH(int h) {
        this.h = h;
    }

    public void copyFrom(WindowPosition item) {
        this.x = item.getX();
        this.y = item.getY();
        this.w = item.getW();
        this.h = item.getH();
    }

    public String toString() {
        return "WindowPosition [ "+"x="+this.x+","+"y="+this.y+","+"w="+this.w+","+"h="+this.h+" ]";
    }

}