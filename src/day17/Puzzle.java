package day17;

import base.AbstractPuzzle;
import base.model.Intcode;
import base.model.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = false;
    public static final int DAY = 17;

    public Puzzle() {
        super( IS_TEST, DAY );
    }

    public static void main( String... args ) {
//                        solve1();
        solve2();
    }

    public static void solve1() {
        System.out.println( "Solving 1..." );
        Puzzle puzzle = new Puzzle();

        List<Integer> output = initCameraFeed( puzzle );
        Map<Point, CameraOutput> cameraOutputs = getCameraOutputs( output );
        List<Point> scaffoldIntersections = getScaffoldIntersections( cameraOutputs );
        int alignment = alignCameras( scaffoldIntersections );
        System.out.println( alignment );
        System.out.println( alignment == getAnswer1( puzzle ) );
    }

    public static void solve2() {
        System.out.println( "Solving 2..." );
        Puzzle puzzle = new Puzzle();

        List<Integer> output = initCameraFeed( puzzle );
        Map<Point, CameraOutput> cameraOutputs = getCameraOutputs( output );

        System.out.println( Point.pointMapToString( cameraOutputs, ( p, c ) -> String.valueOf( c.output ) ) );

    }

    public static int getAnswer1( Puzzle puzzle ) {
        if ( IS_TEST ) {
            return 76;
        }
        return (int) puzzle.getAnswer1();
    }

    public static List<Integer> initCameraFeed( Puzzle puzzle ) {
        if ( IS_TEST ) {
            return outputToIntList();
        }
        else {
            String instructions = puzzle.readFile().get( 0 );
            Intcode intcode = new Intcode( instructions );
            intcode.runProgram();
            Optional<Long> out = intcode.getOutput();

            List<Integer> output = new ArrayList<>();
            while ( out.isPresent() ) {
                output.add( out.get().intValue() );
                out = intcode.getOutput();
            }

            Collections.reverse( output );
            return output;
        }
    }

    public static int alignCameras( List<Point> scaffoldIntersections ) {
        int alignment = 0;
        for ( Point p : scaffoldIntersections ) {
            alignment += (p.getX() * p.getY());
        }
        return alignment;
    }

    public static Map<Point, CameraOutput> getCameraOutputs( List<Integer> codes ) {
        Map<Point, CameraOutput> cameraOutputs = new HashMap<>();
        int x = 0;
        int y = 0;

        for ( int code : codes ) {
            Point p = new Point( x, y );
            CameraOutput cameraOutput = CameraOutput.fromAsciiCode( code );
            if ( CameraOutput.NEW_LINE.equals( cameraOutput ) ) {
                y++;
                x = 0;
            }
            else {
                cameraOutputs.put( p, cameraOutput );
                x++;
            }
        }

        return cameraOutputs;
    }

    public static List<Point> getScaffoldIntersections( Map<Point, CameraOutput> cameraOutputs ) {
        List<Point> scaffoldIntersections = new ArrayList<>();

        for ( Map.Entry<Point, CameraOutput> entry : cameraOutputs.entrySet() ) {
            if ( CameraOutput.SCAFFOLD.equals( entry.getValue() ) ) {
                Point current = entry.getKey();

                if ( current.getAdjacentPoints().stream()
                      .allMatch( p -> CameraOutput.SCAFFOLD.equals( cameraOutputs.get( p ) ) ) ) {
                    scaffoldIntersections.add( current );
                }
            }
        }

        return scaffoldIntersections;
    }

    public static List<Integer> outputToIntList() {
        //@formatter:off
      String exampleString =
              "..#..........\n"
            + "..#..........\n"
            + "#######...###\n"
            + "#.#...#...#.#\n"
            + "#############\n"
            + "..#...#...#..\n"
            + "..#####...^..";
      //@formatter:on

        List<Integer> output = new ArrayList<>();
        for ( char c : exampleString.toCharArray() ) {
            output.add( (int) c );
        }
        return output;
    }

    enum CameraOutput {
        SCAFFOLD( '#' ),
        EMPTY( '.' ),
        ROBOT_UP( '^' ),
        ROBOT_DOWN( 'v' ),
        ROBOT_LEFT( '<' ),
        ROBOT_RIGHT( '>' ),
        ROBOT_DEAD( 'X' ),
        NEW_LINE( '\n' );

        char output;

        CameraOutput( char output ) {
            this.output = output;
        }

        public static CameraOutput fromAsciiCode( int asciiCode ) {
            for ( CameraOutput c : CameraOutput.values() ) {
                if ( c.output == asciiCode ) {
                    return c;
                }
            }
            return null;
        }
    }


}
