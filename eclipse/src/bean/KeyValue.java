package bean;
public class KeyValue { 

    private String key;
    private String value;

    public KeyValue() {
    }

    public KeyValue(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public void copyFrom(KeyValue item) {
        this.key = item.getKey();
        this.value = item.getValue();
    }

    public String toString() {
        return "KeyValue [ "+"key="+this.key+","+"value="+this.value+" ]";
    }

}
