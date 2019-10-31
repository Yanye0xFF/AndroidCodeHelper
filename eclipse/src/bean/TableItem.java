package bean;

public class TableItem { 

    private int paramsType;
    private String varType;
    private String varName;
    private String resId;

    public TableItem() {
    }

    public TableItem(int paramsType,String varType,String varName,String resId) {
        this.paramsType = paramsType;
        this.varType = varType;
        this.varName = varName;
        this.resId = resId;
    }

    public int getParamsType() {
        return this.paramsType;
    }
    public void setParamsType(int paramsType) {
        this.paramsType = paramsType;
    }

    public String getVarType() {
        return this.varType;
    }
    public void setVarType(String varType) {
        this.varType = varType;
    }

    public String getVarName() {
        return this.varName;
    }
    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getResId() {
        return this.resId;
    }
    public void setResId(String resId) {
        this.resId = resId;
    }

    public void copyFrom(TableItem item) {
        this.paramsType = item.getParamsType();
        this.varType = item.getVarType();
        this.varName = item.getVarName();
        this.resId = item.getResId();
    }

    public String toString() {
        return "TableItem [ "+"paramsType="+this.paramsType+","+"varType="+this.varType+","+"varName="+this.varName+","+"resId="+this.resId+" ]";
    }

}