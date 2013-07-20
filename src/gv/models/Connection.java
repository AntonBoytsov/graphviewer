/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models;

/**
 *
 * @author Anton
 */
public class Connection {
    
    private long ID = 0;
    protected Vertex first, second;
    private Value value;
    private String name; 

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
    
    public Connection(String name, Vertex first, Vertex second) {
        this.setName(name);
        this.first = first;
        this.second = second;
    }
    
    
    
}
