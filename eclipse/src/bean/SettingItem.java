package bean;

public class SettingItem { 

    private boolean expandAll;
    private String encodeType;

    public SettingItem() {
    }

    public SettingItem(boolean expandAll,String encodeType) {
        this.expandAll = expandAll;
        this.encodeType = encodeType;
    }

    public boolean getExpandAll() {
        return this.expandAll;
    }
    public void setExpandAll(boolean expandAll) {
        this.expandAll = expandAll;
    }



    public String getEncodeType() {
        return this.encodeType;
    }
    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public void copyFrom(SettingItem item) {
        this.expandAll = item.getExpandAll();
        this.encodeType = item.getEncodeType();
    }

    public String toString() {
        return "SettingItem [ "+"expandAll="+this.expandAll+","+"encodeType="+this.encodeType+" ]";
    }

}