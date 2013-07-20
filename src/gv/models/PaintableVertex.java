/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gv.models;

import gv.types.ModelKind;
import gv.models.libraries.VertexLibrary;
import gv.types.ProductionType;
import gv.types.Role;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.border.Border;

/**
 *
 * @author anton
 */
public class PaintableVertex extends Vertex {

    private Point center;
    private final int RADIUS = 25;
    private int radius = RADIUS;
    private Rectangle bounds = new Rectangle();

    public PaintableVertex(int x, int y, ModelKind kind) {
        super(kind);
        this.center = new Point(x, y);
        this.setBounds();
    }

    public void paint(Graphics g) {

        radius = RADIUS;
        if (role == Role.Property) {
            radius /= 1.5;
        } else
        if (productionType == ProductionType.Fact) {
            radius *= 1.5;
        } else if (productionType == ProductionType.State) {
            radius *= 2.25;
        } 
        
        
        this.setBounds();
        if (kind == ModelKind.Semantic) {
            if (objectType == objectType.Class) {
                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.orange);
            }
            g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
        } else if (kind == ModelKind.Frame) {
            if (objectType == objectType.Class) {
                g.setColor(Color.cyan);
            } else {
                g.setColor(Color.blue);
            }
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            if (objectType == objectType.Class) {
                g.setColor(Color.pink);
            } else {
                g.setColor(Color.magenta);
            }
            int xpoints[] = {bounds.x, bounds.x + bounds.width / 2, bounds.x + bounds.width};
            int ypoints[] = {bounds.y + bounds.height, bounds.y, bounds.y + bounds.height};
            g.fillPolygon(xpoints, ypoints, 3);
        }
        if (this.isPicked()) {
            //g.setColor(Color.red);
            //g.drawRect(bounds.x, bounds.y, bounds.height, bounds.width);
        } else if (this.isSelected()) {
            g.setColor(Color.black);
            g.drawRect(bounds.x, bounds.y, bounds.height, bounds.width);
        }


    }

    public void setBounds() {
        bounds.setBounds(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point newCenter) {
        center = newCenter;
    }

    public boolean contains(Point p) {

        return bounds.contains(p);

    }

    public boolean isSelected() {
        return (this == VertexLibrary.selectedVertex);
    }

    public void select() {      
        VertexLibrary.unselectSelected();
        unpick();
        VertexLibrary.selectedVertex = this;
    }

    public void unselect() {
        VertexLibrary.selectedVertex = null;
    }

    public boolean isPicked() {
        return (this == VertexLibrary.pickedVertex);
    }

    public void pick() {
        VertexLibrary.unpickPicked();
        VertexLibrary.pickedVertex = this;
    }

    public void unpick() {
        VertexLibrary.pickedVertex = null;
    }
}
