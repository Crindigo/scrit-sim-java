package com.crindigo.scritsim.ui;

import com.crindigo.scritsim.model.components.ReactorComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimulatorUI extends JFrame
{
    static final String[] sizeOptions = {"3", "5", "7", "9", "11", "13"};
    static final String[] depthOptions = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    JComboBox<String> sizeComboBox;
    JComboBox<String> depthComboBox;
    JButton applyButton = new JButton("Update Reactor Size");
    GridLayout reactorLayout = new GridLayout(0,3);
    JPanel reactorComponents = new JPanel();

    ComponentPalette.Paint[][] reactorCanvas = new ComponentPalette.Paint[3][3];

    GridLayout paletteLayout = new GridLayout(0, 3);
    JPanel palettePanel = new JPanel();

    ComponentPalette.Paint currentPaint = null;

    public SimulatorUI(String name) {
        super(name);
        setResizable(true);
    }

    public void initDropdowns() {
        sizeComboBox = new JComboBox<>(sizeOptions);
        depthComboBox = new JComboBox<>(depthOptions);
    }

    private void initPalette() {
        ComponentPalette.init();
        palettePanel.setLayout(paletteLayout);
        Dimension square = new Dimension(50, 50);
        final ButtonGroup group = new ButtonGroup();
        ComponentPalette.allPaints().forEach(paint -> {
            JToggleButton b;
            if ( paint.image.isEmpty() ) {
                b = new JToggleButton(" ");
            } else {
                b = new JToggleButton(new ImageIcon(getClass().getResource("/" + paint.image)));
            }
            b.setPreferredSize(square);
            b.setMaximumSize(square);
            b.setToolTipText(paint.name);
            b.addActionListener(e -> {
                currentPaint = paint;
                System.out.println("current paint = " + currentPaint.name);
            });
            palettePanel.add(b);
            group.add(b);
        });
    }

    private void updateReactorLayout(int diameter) {
        reactorLayout.setColumns(diameter);
        reactorComponents.removeAll();
        resizeCanvas(diameter);
        Dimension square = new Dimension(50, 50);
        int radius = (diameter - 1) / 2;
        for (int i = 0; i < diameter; i++) {
            for (int x = 0; x < diameter; x++) {
                if (Math.pow(i - radius, 2) + Math.pow(x - radius, 2) < Math.pow(radius + 0.5f, 2)) {
                    int y = diameter - 1 - i;

                    JButton b = new JButton();
                    b.setPreferredSize(square);
                    b.setMaximumSize(square);
                    b.setMinimumSize(square);
                    b.setActionCommand(x + "," + y); // Y=0 is at bottom
                    b.addActionListener(this::reactorButtonClicked);
                    updateButton(b, x, y, reactorCanvas[x][y]);
                    reactorComponents.add(b);
                } else {
                    JLabel wall = new JLabel(" ");
                    wall.setPreferredSize(square);
                    reactorComponents.add(wall);
                }
            }
        }
        reactorComponents.revalidate();
        reactorComponents.repaint();
        this.pack();
    }

    private void resizeCanvas(int diameter) {
        int oldDiameter = reactorCanvas.length;
        // Copy existing items to the new canvas, when sizing up, keep old items in the middle.
        // an item at 1,1 diameter 3 becomes 2,2 diameter 5, or 3,3 diameter 7.
        // if you have an item at 2,0 D=5 it would be 1,-1 and outside the canvas if going to D=3.
        // an item at 2,4 D=5 would be 1,3 and also outside the canvas if going to D=3.
        ComponentPalette.Paint[][] newCanvas = new ComponentPalette.Paint[diameter][diameter];
        int adjustment = (diameter - oldDiameter) / 2;
        for ( int y = 0; y < oldDiameter; y++ ) {
            for ( int x = 0; x < oldDiameter; x++ ) {
                int newX = x + adjustment;
                int newY = y + adjustment;
                if ( newX < 0 || newX >= diameter || newY < 0 || newY >= diameter) {
                    continue;
                }
                newCanvas[newX][newY] = reactorCanvas[x][y];
            }
        }

        reactorCanvas = newCanvas;
    }

    public void reactorButtonClicked(ActionEvent e) {
        if ( currentPaint == null ) {
            return;
        }

        String[] xy = e.getActionCommand().split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        System.out.println("Paint " + currentPaint.name + " to " + x + "," + y);

        reactorCanvas[x][y] = currentPaint;
        JButton b = (JButton) e.getSource();
        updateButton(b, x, y, currentPaint);
    }

    private void updateButton(JButton b, int x, int y, ComponentPalette.Paint paint) {
        if ( paint == null ) {
            paint = ComponentPalette.empty;
        }
        if ( paint.image.isEmpty() ) {
            b.setIcon(null);
        } else {
            b.setIcon(new ImageIcon(getClass().getResource("/" + paint.image)));
        }
        b.setToolTipText("<html>" + paint.name + "<br>X: " + x + "<br>Y: " + y);
    }

    public void addComponentsToPane(final Container pane) {
        initDropdowns();
        reactorComponents.setLayout(reactorLayout);
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        JPanel topLeft = new JPanel();
        topLeft.setLayout(null);
        topLeft.setPreferredSize(new Dimension(200, 140));

        JLabel diameterLabel = new JLabel("Diameter:");
        JLabel depthLabel = new JLabel("Depth:");

        topLeft.add(diameterLabel);
        topLeft.add(sizeComboBox);
        topLeft.add(depthLabel);
        topLeft.add(depthComboBox);
        topLeft.add(applyButton);

        diameterLabel.setBounds(10, 10, 80, 30);
        sizeComboBox.setBounds(100, 10, 90, 30);
        depthLabel.setBounds(10, 50, 80, 30);
        depthComboBox.setBounds(100, 50, 90, 30);
        applyButton.setBounds(10, 90, 180, 30);

        controls.add(topLeft);

        initPalette();
        controls.add(palettePanel);

        applyButton.addActionListener(e -> {
            String diameter = (String) sizeComboBox.getSelectedItem();
            updateReactorLayout(Integer.parseInt(diameter));
        });
        pane.add(controls, BorderLayout.LINE_START);
        pane.add(reactorComponents, BorderLayout.CENTER);
        updateReactorLayout(3);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        SimulatorUI frame = new SimulatorUI("Supercritical Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        javax.swing.SwingUtilities.invokeLater(SimulatorUI::createAndShowGUI);
    }
}
