package com.crindigo.scritsim.ui;

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

    public SimulatorUI(String name) {
        super(name);
        setResizable(true);
    }

    public void initGaps() {
        sizeComboBox = new JComboBox<>(sizeOptions);
        depthComboBox = new JComboBox<>(depthOptions);
    }

    private void updateReactorLayout(int diameter) {
        reactorLayout.setColumns(diameter);
        reactorComponents.removeAll();
        Dimension square = new Dimension(50, 50);
        int radius = (diameter - 1) / 2;
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                if (Math.pow(i - radius, 2) + Math.pow(j - radius, 2) < Math.pow(radius + 0.5f, 2)) {
                    JButton b = new JButton("X");
                    b.setPreferredSize(square);
                    b.setActionCommand(j + "," + (diameter - 1 - i)); // Y=0 is at bottom
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
        initGaps();
        reactorComponents.setLayout(reactorLayout);
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        JPanel diameterRow = new JPanel();
        diameterRow.add(new JLabel("Diameter:"));
        sizeComboBox.setMaximumSize(sizeComboBox.getPreferredSize());
        diameterRow.add(sizeComboBox);
        diameterRow.setMaximumSize(diameterRow.getPreferredSize());
        controls.add(diameterRow);

        JPanel depthRow = new JPanel();
        depthRow.add(new JLabel("Depth:"));
        depthComboBox.setMaximumSize(depthComboBox.getPreferredSize());
        depthRow.add(depthComboBox);
        depthRow.setMaximumSize(depthRow.getPreferredSize());
        controls.add(depthRow);
        controls.add(applyButton);

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
