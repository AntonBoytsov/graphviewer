/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models.libraries;

import gv.models.Connection;
import gv.models.PaintableConnection;
import gv.models.PaintableVertex;
import gv.models.Vertex;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Anton
 */
public class ConnectionLibrary {
    
    private static long maxNumber = 0;
    public static Map<Long, Connection> connections = new TreeMap<>();
    
    public static Long add(Vertex first, Vertex second) {
        
        Connection c = new PaintableConnection("Pie", (PaintableVertex)first, (PaintableVertex)second);
        c.setID(maxNumber);
        connections.put(maxNumber, c);
        maxNumber++;
        return maxNumber - 1;
        
    }
    
    public static Connection getById(Long Id) {
        
        return connections.get(Id);
        
    }
    
    public static void clearAll() {
        connections.clear();
        maxNumber = 0;
    }
    
}
