package gv.forms;

import gv.models.Connection;
import gv.types.ModelKind;
import gv.models.PaintableConnection;
import gv.models.PaintableVertex;
import gv.models.Vertex;
import gv.models.libraries.ConnectionLibrary;
import gv.models.libraries.VertexLibrary;
import gv.types.ObjectType;
import gv.types.ProductionType;
import gv.types.Role;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GraphViewer extends JComponent {

    private GraphViewer frame;
    
    private Action newVertexAction = new NewVertexAction("New");
    private Action clearAllAction = new ClearAllAction("Clear");
    private JButton newVertexButton = new JButton(newVertexAction);
    private JButton clearAllButton = new JButton(clearAllAction);
    
    private static final int WINWIDTH = 800;
    private static final int WINHEIGHT = 600;
    private GraphToolBar toolBar = new GraphToolBar();
    private JPanel menuPanel = new JPanel();
    private JScrollPane scroll = new JScrollPane(toolBar);
    private Point mousePoint = new Point(WINWIDTH / 2, WINHEIGHT / 2);
    //private boolean selecting = false;

    public static void main(String[] args) {


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JFrame frame = new JFrame("GraphViewer");
                frame.setSize(WINWIDTH, WINHEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GraphViewer graphViewer = new GraphViewer();
                
                graphViewer.frame = graphViewer;
                
                graphViewer.menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                graphViewer.menuPanel.add(graphViewer.newVertexButton);
                graphViewer.menuPanel.add(graphViewer.clearAllButton);
                
                frame.add(graphViewer.menuPanel, BorderLayout.NORTH);
                graphViewer.toolBar.setMinimumSize(new Dimension(300, 600));
                graphViewer.toolBar.setMaximumSize(new Dimension(300, 600));
                graphViewer.toolBar.setAutoscrolls(true);
                frame.add(graphViewer.toolBar, BorderLayout.EAST);
                frame.add(graphViewer.scroll);
                frame.add(new JScrollPane(graphViewer), BorderLayout.CENTER);
                //frame.setBackground(Color.white);
                frame.setVisible(true);
                
            }
        });

    }

    public GraphViewer() {
        this.setOpaque(true);
        this.addMouseListener(new WindowMouseAdapter());
        this.addMouseMotionListener(new WindowMouseMotionAdapter());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINWIDTH, WINHEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Vertex v : VertexLibrary.vertices.values()) {
            for (Long vertexID : v.connections.keySet()) {
                Long connectionID = (Long) (v.connections.get(vertexID).toArray())[0];
                ((PaintableConnection) ConnectionLibrary.getById(connectionID)).paint(g);
            }
        }

        for (Vertex v : VertexLibrary.vertices.values()) {
            ((PaintableVertex) v).paint(g);
        }

    }

    void reloadMenu() {
        if (VertexLibrary.selectedVertex != null) {
            toolBar.nameBox.setEnabled(true);
            toolBar.nameBox.setText(VertexLibrary.selectedVertex.getName());
            toolBar.kindBox.setEnabled(true);
            toolBar.kindBox.setSelectedItem(VertexLibrary.selectedVertex.getKind());
            toolBar.objectBox.setEnabled(true);
            toolBar.objectBox.setSelectedItem(VertexLibrary.selectedVertex.getObjectType());
            toolBar.productionBox.setEnabled(true);
            toolBar.productionBox.setSelectedItem(VertexLibrary.selectedVertex.getProductionType());
            toolBar.roleBox.setEnabled(true);
            toolBar.roleBox.setSelectedItem(VertexLibrary.selectedVertex.getRole());
            toolBar.connectionBox.setEnabled(true);
            toolBar.connectionBox.removeAllItems();

            for (Long vertexID : VertexLibrary.selectedVertex.connections.keySet()) {
                toolBar.connectionBox.addItem(VertexLibrary.getVertex(vertexID).getName());
            }
            if (toolBar.connectionBox.getItemCount() == 0) {
                toolBar.connectionBox.addItem("Nothing");
            }

        } else {
            toolBar.nameBox.setEnabled(false);
            toolBar.nameBox.setText("Nothing");
            toolBar.kindBox.setEnabled(false);
            toolBar.objectBox.setEnabled(false);
            toolBar.productionBox.setEnabled(false);
            toolBar.roleBox.setEnabled(false);
            toolBar.connectionBox.removeAllItems();
            toolBar.connectionBox.addItem("Nothing");
            toolBar.connectionBox.setEnabled(false);
        }
    }

    private class WindowMouseAdapter extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                VertexLibrary.pickVertex(mousePoint);
                showPopup(e);
            }
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mousePoint = e.getPoint();
            if (e.isPopupTrigger()) {
                VertexLibrary.pickVertex(mousePoint);
                showPopup(e);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                VertexLibrary.selectVertex(mousePoint);
                reloadMenu();
            }
            repaint();

        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                toolBar.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class WindowMouseMotionAdapter extends MouseMotionAdapter {

        Point delta = new Point();

        @Override
        public void mouseDragged(MouseEvent e) {

            delta.setLocation(e.getX() - mousePoint.getX(), e.getY() - mousePoint.getY());
            VertexLibrary.updatePosition(delta);
            mousePoint = e.getPoint();

            repaint();

        }
    }

    private class GraphToolBar extends JPanel {

        
        
        private Action changeKindAction = new ChangeKindAction("ChangeKind");
        private Action changeObjectTypeAction = new ChangeObjectTypeAction("ObjectType");
        private Action changeProductionTypeAction = new ChangeProductionTypeAction("ProductionType");
        private Action changeRoleAction = new ChangeRoleAction("Role");
        private Action changeNameAction = new ChangeNameAction("ChangeName");
        private Action connect = new ConnectAction("Connect");
        
        private ModelKind kinds[] = {ModelKind.Semantic, ModelKind.Frame, ModelKind.Onto};
        private ObjectType otypes[] = {ObjectType.Class, ObjectType.Exemplar};
        private ProductionType ptypes[] = {ProductionType.State, ProductionType.Fact, ProductionType.Concept};
        private Role roles[] = {Role.Common, Role.Property};
        
        
        private JTextField nameBox = new JTextField();
        private JComboBox kindBox = new JComboBox(kinds);
        private JComboBox productionBox = new JComboBox(ptypes);
        private JComboBox objectBox = new JComboBox(otypes);
        private JComboBox roleBox = new JComboBox(roles);
        private JComboBox connectionBox = new JComboBox();
        
        private JLabel nameLabel = new JLabel("Name:");
        private JLabel modelKindLabel = new JLabel("Model kind:");
        private JLabel objectTypeLabel = new JLabel("Object type:");
        private JLabel productionTypeLabel = new JLabel("Production type:");
        private JLabel roleLabel = new JLabel("Role:");
        private JLabel connectionsLabel = new JLabel("Connections:");
        
        private JPopupMenu popup = new JPopupMenu();
        
        

        public GraphToolBar() {

            GridBagLayout gbl = new GridBagLayout();
            this.setLayout(gbl);
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.NONE;
            c.gridheight = 1;
            c.gridwidth = 0;
            c.gridx = 0;
            c.gridy = GridBagConstraints.RELATIVE;
            c.weighty = 0.0;
            c.weightx = 0.0;
            c.insets = new Insets(5, 5, 0, 0);
            
            this.setBackground(Color.lightGray);
            
            gbl.setConstraints(nameLabel, c);
            this.add(nameLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            nameBox.setColumns(10);
            nameBox.setText("Nothing");
            nameBox.setEnabled(false);
            nameBox.addActionListener(changeNameAction);
            gbl.setConstraints(nameBox, c);
            this.add(nameBox);
            
            c.insets = new Insets(20, 5, 0, 0);
            gbl.setConstraints(modelKindLabel, c);
            this.add(modelKindLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            kindBox.setEnabled(false);
            kindBox.addActionListener(changeKindAction);
            gbl.setConstraints(kindBox, c);
            this.add(kindBox);
            
            c.insets = new Insets(20, 5, 0, 0);
            gbl.setConstraints(objectTypeLabel, c);
            this.add(objectTypeLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            objectBox.setEnabled(false);
            objectBox.addActionListener(changeObjectTypeAction);
            gbl.setConstraints(objectBox, c);
            this.add(objectBox);
            
            c.insets = new Insets(20, 5, 0, 0);
            gbl.setConstraints(productionTypeLabel, c);
            this.add(productionTypeLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            productionBox.setEnabled(false);
            productionBox.addActionListener(changeProductionTypeAction);
            gbl.setConstraints(productionBox, c);
            this.add(productionBox);
            
            c.insets = new Insets(20, 5, 0, 0);
            gbl.setConstraints(roleLabel, c);
            this.add(roleLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            roleBox.setEnabled(false);
            roleBox.addActionListener(changeRoleAction);
            gbl.setConstraints(roleBox, c);
            this.add(roleBox);

            c.insets = new Insets(20, 5, 0, 0);
            gbl.setConstraints(connectionsLabel, c);
            this.add(connectionsLabel);
            
            c.insets = new Insets(5, 5, 0, 0);
            c.weighty = 1.0;
            connectionBox.addItem("Nothing");
            connectionBox.setEnabled(false);
            gbl.setConstraints(connectionBox, c);
            this.add(connectionBox);

            popup.add(new JMenuItem(connect));
            this.add(popup);
            

        }
    }

    private class ChangeNameAction extends AbstractAction {

        public ChangeNameAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            VertexLibrary.selectedVertex.setName(toolBar.nameBox.getText());
            reloadMenu();
            repaint();

        }
    }
    
    void changeDFS(Vertex v, ModelKind k) {
        
        v.setKind(k);
        for (Long lv : v.connections.keySet()) {
            Vertex to = VertexLibrary.getVertex(lv);
            if (to.getKind() != k) {
                changeDFS(to, k);
            }
        }
        
    }

    private class ChangeKindAction extends AbstractAction {

        public ChangeKindAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            changeDFS(VertexLibrary.selectedVertex, (ModelKind) toolBar.kindBox.getSelectedItem());
            reloadMenu();
            repaint();

        }
    }
    
    private class ChangeObjectTypeAction extends AbstractAction {

        public ChangeObjectTypeAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            VertexLibrary.selectedVertex.setObjectType((ObjectType) toolBar.objectBox.getSelectedItem());
            reloadMenu();
            repaint();

        }
    }
    
    private class ChangeProductionTypeAction extends AbstractAction {

        public ChangeProductionTypeAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            VertexLibrary.selectedVertex.setProductionType((ProductionType) toolBar.productionBox.getSelectedItem());
            reloadMenu();
            repaint();

        }
    }
    
    private class ChangeRoleAction extends AbstractAction {

        public ChangeRoleAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            VertexLibrary.selectedVertex.setRole((Role) toolBar.roleBox.getSelectedItem());
            reloadMenu();
            repaint();

        }
    }

    private class NewVertexAction extends AbstractAction {

        public NewVertexAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            PaintableVertex v = new PaintableVertex(100, 100, ModelKind.Semantic);
            VertexLibrary.addVertex(v);
            v.select();
            reloadMenu();
            repaint();

        }
    }
    
    private class ClearAllAction extends AbstractAction {

        public ClearAllAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            VertexLibrary.clearAll();
            ConnectionLibrary.clearAll();
            reloadMenu();
            repaint();

        }
    }

    private class ConnectAction extends AbstractAction {

        public ConnectAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (!VertexLibrary.connectVertices(mousePoint)) {
                JOptionPane.showMessageDialog(frame, "Sorry, can't connect vertices", "Error", JOptionPane.WARNING_MESSAGE);
            }
            reloadMenu();
            repaint();

        }
    }
}
