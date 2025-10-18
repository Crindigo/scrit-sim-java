package com.crindigo.scritsim.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PipeHandler implements Runnable
{
    private final SimulatorUI ui;
    private final BufferedReader input;

    public PipeHandler(SimulatorUI ui) {
        this.ui = ui;
        this.input = new BufferedReader(new InputStreamReader(System.in));
    }

    // < "design"
    // < enter scd file format...
    // < "."
    // < sim 12345
    // > consume type amt
    // > generate type amt_per_s
    // > or "meltdown steps" or "explosion steps"
    @Override
    public void run() {
        try {
            var designText = new StringBuilder();
            var designMode = false;

            while (true) {
                var line = input.readLine();
                if (line.equals("design")) {
                    designMode = true;
                    continue;
                } else if (line.startsWith("sim ")) {
                    var steps = 0;
                    try {
                        steps = Integer.parseInt(line.substring(4));
                        if ( steps < 1 || steps > 1000000 ) {
                            System.out.println("use between 1 and 1,000,000");
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("invalid number");
                        continue;
                    }
                    var results = ui.runSimulation(steps);
                    System.out.print(results);
                } else if (line.equals("q") || line.equals("exit")) {
                    System.exit(0);
                }

                if (designMode) {
                    if (line.equals(".")) {
                        var design = ReactorSaveHandler.loadDesign(designText.toString());
                        ui.setDesign(design, false);
                        designMode = false;
                        designText.setLength(0);
                    } else {
                        designText.append(line).append('\n');
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
            System.exit(1);
        }
    }
}
