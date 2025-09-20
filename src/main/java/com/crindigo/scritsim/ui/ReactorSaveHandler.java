package com.crindigo.scritsim.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ReactorSaveHandler
{
    public static final String EXTENSION = "scdesign";

    /*
     demo.scdesign:
     "scd" version
     diameter depth
     x y component_id
     x y component_id
     ...
     */

    public static String saveDesign(Design design) {
        var canvas = design.canvas;
        var b = new StringBuilder();
        b.append("scd ").append(1).append('\n');
        b.append(canvas.length).append(' ').append(design.depth).append('\n');
        for ( int x = 0; x < canvas.length; x++ ) {
            for ( int y = 0; y < canvas[x].length; y++ ) {
                if ( canvas[x][y] == null || canvas[x][y].id.isEmpty() ) {
                    continue;
                }
                b.append(x).append(' ').append(y).append(' ').append(canvas[x][y].id).append('\n');
            }
        }
        return b.toString();
    }

    public static Design loadDesign(String design) {
        var scanner = new Scanner(design);
        var scd = scanner.next();
        if ( !scd.equals("scd") ) {
            throw new DesignParseException("File did not start with scd");
        }

        try {
            var version = scanner.nextInt();
            if ( version != 1 ) {
                throw new DesignParseException("Unrecognized version");
            }

            var diameter = scanner.nextInt();
            var depth = scanner.nextInt();
            var canvas = new ComponentPalette.Paint[diameter][diameter];
            while ( scanner.hasNext() ) {
                var x = scanner.nextInt();
                var y = scanner.nextInt();
                if ( x < 0 || x >= diameter || y < 0 || y >= diameter ) {
                    System.err.println("Skipping coordinate outside bounds");
                    continue;
                }

                if ( scanner.hasNextInt() ) {
                    // workaround for a bug: it shouldn't have been adding empty to the file.
                    // so we just check if the next item is a number because the component is blank.
                    continue;
                }

                if ( !scanner.hasNext() ) {
                    // related to the above if empty is the final component
                    break;
                }

                var componentId = scanner.next();
                canvas[x][y] = ComponentPalette.idToPaint.get(componentId);
            }

            return new Design(depth, canvas);
        } catch ( InputMismatchException e ) {
            throw new DesignParseException("Unexpected input while parsing design");
        }
    }

    public static class DesignParseException extends RuntimeException {

        public DesignParseException(String message) {
            super(message);
        }
    }

    public static class Design {
        public ComponentPalette.Paint[][] canvas;
        public int depth;

        public Design(int depth, ComponentPalette.Paint[][] canvas) {
            this.depth = depth;
            this.canvas = canvas;
        }
    }
}
