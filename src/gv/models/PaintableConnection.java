/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author anton
 */
public class PaintableConnection extends Connection {
    
    public PaintableConnection(String name, PaintableVertex first, PaintableVertex second) {
        super(name, first, second);
    }
    
    public void paint(Graphics g) {
        Point a = ((PaintableVertex) first).getCenter();
        Point b = ((PaintableVertex) second).getCenter();
        ((PaintableVertex) first).unpick();
        ((PaintableVertex) second).unpick();
        g.setColor(Color.red);
        g.drawLine(a.x, a.y, b.x, b.y);        
    }
    
}
