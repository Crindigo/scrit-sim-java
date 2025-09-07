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
        Dimension square = new Dimension(50, 50);
        int radius = (diameter - 1) / 2;
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                if (Math.pow(i - radius, 2) + Math.pow(j - radius, 2) < Math.pow(radius + 0.5f, 2)) {
                    JButton b = new JButton(new ImageIcon(getClass().getResource("/fuel_rod_mox_susy.png")));
                    b.setPreferredSize(square);
                    b.setMaximumSize(square);
                    b.setMinimumSize(square);
                    b.setActionCommand(j + "," + (diameter - 1 - i)); // Y=0 is at bottom
                    b.setToolTipText("<html>Low-Grade MOX<br>X: " + j + "<br>Y: " + (diameter - 1 - i));
                    b.addActionListener(this::reactorButtonClicked);
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

    public void reactorButtonClicked(ActionEvent e) {
        System.out.println(e.getActionCommand());
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
