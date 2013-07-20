/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models.libraries;

import gv.models.PaintableVertex;
import gv.models.Vertex;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Anton
 */
public class VertexLibrary {

    private static long maxNumber = 0;
    public static Vertex selectedVertex, pickedVertex;
    public static Map<Long, Vertex> vertices = new TreeMap<>();
    
    public static void addVertex(Vertex v) {

        v.setID(maxNumber);
        v.setName("Object" + String.valueOf(maxNumber));
        vertices.put(maxNumber, v);
        maxNumber++;

    }

    public static Vertex getVertex(Long index) {

        return vertices.get(index);

    }

    public static void unselectSelected() {
        if (selectedVertex != null) {
            PaintableVertex pv = (PaintableVertex) VertexLibrary.selectedVertex;
            pv.unselect();
        }
    }

    public static void unpickPicked() {
        if (pickedVertex != null) {
            PaintableVertex pv = (PaintableVertex) VertexLibrary.pickedVertex;
            pv.unpick();
        }
    }

    public static void selectVertex(Point p) {

        unpickPicked();
        unselectSelected();
        for (Vertex v : vertices.values()) {
            PaintableVertex pv = (PaintableVertex) v;
            if (pv.contains(p)) {
                pv.select();
                return;
            }
        }

    }

    public static void pickVertex(Point p) {

        unpickPicked();
        for (Vertex v : vertices.values()) {
            PaintableVertex pv = (PaintableVertex) v;
            if (pv.contains(p)) {
                pv.pick();
                return;
            }
        }

    }

    public static void updatePosition(Point d) {

        for (Vertex v : vertices.values()) {
            PaintableVertex pv = (PaintableVertex) v;
            if (pv.isSelected()) {
                Point c = pv.getCenter();
                pv.setCenter(new Point(c.x + d.x, c.y + d.y));
                pv.setBounds();
            }
        }

    }

    public static boolean connectVertices(Point p) {

        unpickPicked();
        if (selectedVertex == null) {
            return false;
        }
        for (Vertex v : vertices.values()) {
            PaintableVertex pv = (PaintableVertex) v;
            if (pv.contains(p)) {
                if (!pv.connectTo(selectedVertex)) {
                    return false;
                }
                return selectedVertex.connectTo(v);
            }
        }
        return false;
    }
    
    public static void clearAll() {
        vertices.clear();
        selectedVertex = null;
        pickedVertex = null;
        maxNumber = 0;
    }
    
}
