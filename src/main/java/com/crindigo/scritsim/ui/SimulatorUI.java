package com.crindigo.scritsim.ui;

import com.crindigo.scritsim.model.FissionReactor;
import com.crindigo.scritsim.model.components.CoolantChannel;
import com.crindigo.scritsim.model.components.FuelRod;
import com.crindigo.scritsim.model.components.ReactorComponent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class SimulatorUI extends JFrame
{
    static boolean ioControl = false;
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
    JLabel simulatorResults = new JLabel("");
    JLabel componentDetails = new JLabel("");

    int reactorDepth = 1;
    ComponentPalette.Paint currentPaint = null;
    ComponentHoverListener hoverListener;
    FissionReactor reactor;

    public SimulatorUI(String name) {
        super(name);
        setResizable(true);
        hoverListener = new ComponentHoverListener();
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
                    b.addMouseListener(hoverListener);
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

    private class ComponentHoverListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            if ( reactor == null ) {
                componentDetails.setText("");
                return;
            }

            String[] xy = ((JButton) e.getSource()).getActionCommand().split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            // show some info on the bottom right or else we just use tooltips, would have to
            // save all the JButton instances
            ReactorComponent component = reactor.getComponent(x, y);
            if ( component != null ) {
                String info = component.info().stream().reduce("<html>",
                        (result, item) -> result + item + "<br>");
                componentDetails.setText(info);
            } else {
                componentDetails.setText("");
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            componentDetails.setText("");
        }
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
            String depth = (String) depthComboBox.getSelectedItem();
            reactorDepth = Integer.parseInt(depth);
            updateReactorLayout(Integer.parseInt(diameter));
        });
        pane.add(controls, BorderLayout.LINE_START);
        pane.add(reactorComponents, BorderLayout.CENTER);

        JPanel simulateControls = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        simulateControls.setBorder(padding);
        simulateControls.setLayout(new BoxLayout(simulateControls, BoxLayout.Y_AXIS));

        var loadButton = new JButton("Load");
        var saveButton = new JButton("Save");

        loadButton.addActionListener(e -> {
            var fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("scdesign files", "scdesign"));
            var ok = fileChooser.showOpenDialog(null);
            if ( ok == JFileChooser.APPROVE_OPTION ) {
                var f = fileChooser.getSelectedFile();
                try {
                    var designString = Files.readString(f.toPath());
                    var design = ReactorSaveHandler.loadDesign(designString);
                    setDesign(design, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ReactorSaveHandler.DesignParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Import Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        saveButton.addActionListener(e -> {
            var fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("scdesign files", "scdesign"));
            var ok = fileChooser.showSaveDialog(null);
            if ( ok == JFileChooser.APPROVE_OPTION ) {
                var f = fileChooser.getSelectedFile();
                if ( !f.getName().endsWith("." + ReactorSaveHandler.EXTENSION) ) {
                    f = new File(f.getParentFile(), f.getName() + "." + ReactorSaveHandler.EXTENSION);
                    var design = ReactorSaveHandler.saveDesign(new ReactorSaveHandler.Design(reactorDepth, reactorCanvas));
                    try {
                        Files.writeString(f.toPath(), design);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        var topRight = new JPanel();
        topRight.setPreferredSize(new Dimension(250, 400));
        //topRight.setMaximumSize(new Dimension(400, 400));
        topRight.setLayout(null);
        topRight.add(loadButton);
        topRight.add(saveButton);
        var simulateButton = new JButton("Simulate");
        simulateButton.addActionListener(this::simulateButtonClicked);
        topRight.add(simulateButton);
        topRight.add(simulatorResults);
        topRight.add(componentDetails);

        loadButton.setBounds(0, 0, 80, 30);
        saveButton.setBounds(100, 0, 80, 30);
        simulateButton.setBounds(0, 40, 180, 30);
        simulatorResults.setVerticalAlignment(SwingConstants.TOP);
        simulatorResults.setBounds(0, 80, 250, 150);
        componentDetails.setVerticalAlignment(SwingConstants.BOTTOM);
        componentDetails.setBounds(0, 200, 250, 200);

        simulateControls.add(topRight);

        //JSeparator sep = new JSeparator();
        //sep.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        //simulateControls.add(sep);
        //simulateControls.add(componentDetails);

        pane.add(simulateControls, BorderLayout.LINE_END);
        updateReactorLayout(3);
    }

    public void setDesign(ReactorSaveHandler.Design design, boolean updateUI) {
        reactorCanvas = design.canvas;
        reactorDepth = design.depth;
        if (updateUI) {
            sizeComboBox.setSelectedItem("" + reactorCanvas.length);
            depthComboBox.setSelectedItem("" + reactorDepth);
            updateReactorLayout(reactorCanvas.length);
        }
    }

    private void simulateButtonClicked(ActionEvent e) {
        updateResults(runSimulation(21600));
    }

    public String runSimulation(int totalSteps) {
        int diameter = reactorCanvas.length;
        FissionReactor fr = new FissionReactor(diameter, reactorDepth, 0.5);
        this.reactor = fr;

        List<FuelRod> fuelRods = new ArrayList<>();
        List<CoolantChannel> coolantChannels = new ArrayList<>();
        for ( int y = 0; y < diameter; y++ ) {
            for ( int x = 0; x < diameter; x++ ) {
                if ( reactorCanvas[x][y] == null || reactorCanvas[x][y].componentSupplier == null ) {
                    continue;
                }

                ReactorComponent component = reactorCanvas[x][y].componentSupplier.get();
                fr.addComponent(component, x, y);
                if ( component instanceof FuelRod rod ) {
                    fuelRods.add(rod);
                }
                if ( component instanceof CoolantChannel channel ) {
                    coolantChannels.add(channel);
                }
            }
        }

        fr.prepareThermalProperties();
        fr.computeGeometry();

        // Track all types of consumed fuel and generated hot coolant
        Map<String, Double> consumedFuel = new HashMap<>();
        Map<String, Integer> generatedCoolant = new HashMap<>();
        Map<String, Integer> generatedCoolantWarmedUp = new HashMap<>();
        int steps;
        for ( steps = 0; steps < totalSteps; steps++ ) {
            // scrit checks fuel depletion before updating state
            for (FuelRod fuelRod : fuelRods) {
                String fuelType = fuelRod.getFuel().getId();
                if (fuelRod.isDepleted(fr.fuelDepletion)) {
                    fuelRod.markUndepleted();
                    fuelRod.consume();
                    consumedFuel.compute(fuelType, (k, v) -> (v == null) ? 1 : (v + 1));
                }
            }

            fr.updatePower();
            fr.updateTemperature();

            // this updates in updateTemperature so calculate it right after
            for (CoolantChannel cc : coolantChannels) {
                final String hotCoolant = cc.getCoolant().getHotCoolant().getName();
                final int liters = cc.getGeneratedHotCoolant();
                generatedCoolant.compute(hotCoolant, (k, v) -> (v == null) ? liters : (v + liters));

                // Track coolant for the latest hour of runtime when we know it's warmed up
                if ( steps >= (totalSteps - 3600) ) {
                    generatedCoolantWarmedUp.compute(hotCoolant,
                            (k, v) -> (v == null) ? liters : (v + liters));

                    cc.setLastHourCoolantGenerated(cc.getLastHourCoolantGenerated() + liters);
                }
            }

            fr.updatePressure();
            fr.updateNeutronPoisoning();
            fr.regulateControlRods();

            if ( fr.checkForMeltdown() ) {
                return ioControl ? ("meltdown " + steps + "\n") : ("Melted down after " + steps + " seconds");
            }
            if ( fr.checkForExplosion() ) {
                return ioControl ? ("explosion " + steps + "\n") : ("Exploded after " + steps + " seconds");
            }

//            if (steps % 100 == 0) {
//                System.out.printf("%d | Keff = %.5f, Ctrl = %.4f, Pwr: %.2f/%.2f, Temp: %.2f, Pr: %.0f/%.0f, Dep: %.2f\n",
//                        steps, fr.kEff, fr.controlRodInsertion, fr.power, fr.maxPower, fr.temperature,
//                        fr.pressure, fr.maxPressure, fr.fuelDepletion);
//            }
        }

        // Add in how much fractional fuel has been consumed on the current rods
        for (FuelRod fuelRod : fuelRods) {
            String fuelType = fuelRod.getFuel().getId();
            var fraction = fuelRod.depletionPercent(fr.fuelDepletion);
            consumedFuel.compute(fuelType, (k, v) -> (v == null) ? 0 : (v + fraction));
        }

        StringBuilder builder = new StringBuilder();
        if ( ioControl ) {
            // steps 21600
            // consume heu_235_dioxide 123
            // generate hp_wet_steam 1000
            //builder.append("steps ").append(steps).append('\n');
            consumedFuel.forEach((k, v) -> {
                builder.append("consume ").append(k).append(' ').append(v).append('\n');
            });
            generatedCoolantWarmedUp.forEach((k, v) -> {
                builder.append("generate ").append(k).append(' ').append(v / 3600.).append('\n');
            });
        } else {
            builder.append("Steps ran: ").append(steps).append("<br>");
            consumedFuel.forEach((k, v) -> {
                builder.append(k).append(" consumed: ").append(String.format("%.3f", v)).append("<br>");
            });
            generatedCoolantWarmedUp.forEach((k, v) -> {
                builder.append(k).append(" generated: ").append(v / 3600).append(" L/s<br>");
            });
        }
        return builder.toString();
    }

    private void updateResults(String results) {
        simulatorResults.setText("<html><b>Results:</b><br>" + results);
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

        if ( ioControl ) {
            var t = new Thread(new PipeHandler(frame));
            t.start();
        }
    }

    public static void main(String[] args)
    {
        // not really headless that will take longer
        if (Arrays.asList(args).contains("--iocontrol")) {
            SimulatorUI.ioControl = true;
        }

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
