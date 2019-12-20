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
      solve1();
      solve2();
   }

   public static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();

      List<Integer> output = runProgram( puzzle );
      Map<Point, CameraOutput> cameraOutputs = getCameraOutputs( output );
      List<Point> scaffoldIntersections = getScaffoldIntersections( cameraOutputs );
      int alignment = alignCameras( scaffoldIntersections );
      System.out.println( alignment );
   }

   public static void solve2() {
      System.out.println( "Solving 2..." );
   }

   public static List<Integer> runProgram( Puzzle puzzle ) {
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

   public static Map<Point, CameraOutput> getCameraOutputs( List<Integer> codes ){
      Map<Point, CameraOutput> cameraOutputs = new HashMap<>(  );
      int x = 0;
      int y = 0;

      for( int code : codes ){
         Point p = new Point( x, y );
         CameraOutput cameraOutput = CameraOutput.fromAsciiCode( code );
         if( CameraOutput.NEW_LINE.equals( cameraOutput )){
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

      for( Map.Entry<Point, CameraOutput> entry : cameraOutputs.entrySet() ){
         if ( CameraOutput.SCAFFOLD.equals( entry.getValue() ) ) {
            Point current = entry.getKey();
            Point up = new Point( current.getX() - 1, current.getY() );
            Point down = new Point( current.getX() + 1, current.getY() );
            Point left = new Point( current.getX(), current.getY() - 1 );
            Point right = new Point( current.getX(), current.getY() + 1 );
            if ( CameraOutput.SCAFFOLD.equals( cameraOutputs.get( up ) )
                  && CameraOutput.SCAFFOLD.equals( cameraOutputs.get( down ) )
                  && CameraOutput.SCAFFOLD.equals( cameraOutputs.get( left ) )
                  && CameraOutput.SCAFFOLD.equals( cameraOutputs.get( right ) ) ) {
               scaffoldIntersections.add( current );
            }
         }
      }

      return scaffoldIntersections;
   }

   public static List<Integer> outputToIntList() {
      String exampleString =
              "..#..........\n"
            + "..#..........\n"
            + "#######...###\n"
            + "#.#...#...#.#\n"
            + "#############\n"
            + "..#...#...#..\n"
            + "..#####...^..";

      List<Integer> output = new ArrayList<>(  );
      for( char c : exampleString.toCharArray() ){
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
