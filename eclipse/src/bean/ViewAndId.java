package bean;
public class ViewAndId { 

    private String type;
    private String name;
    private String resId;

    public ViewAndId() {
    }

    public ViewAndId(String type,String name,String resId) {
        this.type = type;
        this.name = name;
        this.resId = resId;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getResId() {
        return this.resId;
    }
    public void setResId(String resId) {
        this.resId = resId;
    }

    public void copyFrom(ViewAndId item) {
        this.type = item.getType();
        this.name = item.getName();
        this.resId = item.getResId();
    }

    public String toString() {
        return "ViewAndId [ "+"type="+this.type+","+"name="+this.name+","+"resId="+this.resId+" ]";
    }

} 
