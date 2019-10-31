package bean;

public class Variable { 

    private String type = null;
    private String name = null;

    public Variable() {
    }

    public Variable(Variable item) {
        this.type = item.getType();
        this.name = item.getName();
    }

    public Variable(String type,String name) {
        this.type = type;
        this.name = name;
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

    public void copyFrom(Variable item) {
        this.type = item.getType();
        this.name = item.getName();
    }

    @Override
    public boolean equals(Object obj) {
    	
    	if(obj == null) {
    		return false;
    	}
    	
    	if(obj instanceof Variable) {
    		Variable temp = (Variable)obj;
    		if(this.name.equals(temp.getName()) && this.type.equals(temp.getType())) {
    			return true;
    		}
    	}
    	
        return false;
    }

    public String toString() {
        return "Variable [ "+"type="+this.type+","+"name="+this.name+" ]";
    }

}