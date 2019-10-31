package bean;


public class CodeRecord { 

    private String recType = null;
    private String recTime = null;
    private String recContent = null;

    public CodeRecord() {
    }

    public CodeRecord(CodeRecord item) {
        this.recType = item.getRecType();
        this.recTime = item.getRecTime();
        this.recContent = item.getRecContent();
    }

    public CodeRecord(String recType,String recTime,String recContent) {
        this.recType = recType;
        this.recTime = recTime;
        this.recContent = recContent;
    }

    public String getRecType() {
        return this.recType;
    }
    public void setRecType(String recType) {
        this.recType = recType;
    }

    public String getRecTime() {
        return this.recTime;
    }
    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getRecContent() {
        return this.recContent;
    }
    public void setRecContent(String recContent) {
        this.recContent = recContent;
    }

    public void copyFrom(CodeRecord item) {
        this.recType = item.getRecType();
        this.recTime = item.getRecTime();
        this.recContent = item.getRecContent();
    }

    public String toString() {
        return "CodeRecord [ "+"recType="+this.recType+","+"recTime="+this.recTime+","+"recContent="+this.recContent+" ]";
    }

}