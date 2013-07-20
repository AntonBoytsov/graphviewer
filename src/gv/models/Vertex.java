/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models;

import gv.types.ModelKind;
import gv.models.libraries.ConnectionLibrary;
import gv.types.ObjectType;
import gv.types.ProductionType;
import gv.types.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Anton
 */
public class Vertex {
    
    private long ID;
    public Map<Long, Set<Long>> connections = new TreeMap<>();
    String name;
    ModelKind kind = ModelKind.Semantic;
    ObjectType objectType = ObjectType.Class;
    ProductionType productionType = ProductionType.Concept;
    Role role = Role.Common;
    
    public Vertex(ModelKind kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getID() {
        return ID;
    }
    
    public void setID(Long ID) {
        this.ID = ID;
    }
    
    public ModelKind getKind() {
        return kind;
    }

    public void setKind(ModelKind kind) {
        this.kind = kind;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public ProductionType getProductionType() {
        return productionType;
    }

    public void setProductionType(ProductionType productionType) {
        this.productionType = productionType;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public boolean connectTo(Vertex v) {
        
        if (this == v || connections.containsKey(v.getID()) || this.kind != v.kind) {
            return false;
        }
        
        if (!connections.containsKey(v.getID())) {
            connections.put(v.getID(), new HashSet<Long>());
        }
        Long l = ConnectionLibrary.add(this, v);
        connections.get(v.getID()).add(l);
        
        return true;
    }
    
}
