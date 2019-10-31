package bean;


public class FuncItem { 

    private String name = null;
    private int id = 0;
    private String iconPath = null;

    public FuncItem() {
    }

    public FuncItem(FuncItem item) {
        this.name = item.getName();
        this.id = item.getId();
        this.iconPath = item.getIconPath();
    }

    public FuncItem(String name,int id,String iconPath) {
        this.name = name;
        this.id = id;
        this.iconPath = iconPath;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getIconPath() {
        return this.iconPath;
    }
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void copyFrom(FuncItem item) {
        this.name = item.getName();
        this.id = item.getId();
        this.iconPath = item.getIconPath();
    }

    public String toString() {
        return this.name;
    }

}