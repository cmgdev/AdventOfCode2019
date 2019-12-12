package day10;

import base.AbstractPuzzle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 10;

   private static final char ASTEROID = '#';

   private static int numTotalAsteroids = 0;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      solve1();
      solve2();
   }

   private static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();
      List<String> inputs = puzzle.readFile( "//" );

      int numAsteroids = getMostSeenAsteroids( inputs );
      int expected = IS_TEST ? 210 : 269;

      System.out.println( "Most seen asteroids is " + numAsteroids );
      System.out.println( expected == numAsteroids );
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
   }

   private static int getNumAsteroids( List<String> inputs ) {
      int numAsteroids = 0;
      for ( String input : inputs ) {
         for ( char c : input.toCharArray() ) {
            if ( c == ASTEROID ) {
               numAsteroids++;
            }
         }
      }
      return numAsteroids;
   }

   private static int getNumAsteroidsSeenFromPoint( int x1, int y1, List<String> inputs ) {
      if ( !isAsteroid( x1, y1, inputs ) ) {
         return 0;
      }

      // calculate this once
      int numOtherAsteroids = numTotalAsteroids == 0 ? getNumAsteroids( inputs ) - 1 : numTotalAsteroids;
      int width = inputs.get( 0 ).length();
      int height = inputs.size();
      List<Double> seenAngles = new ArrayList<>();

      for ( int x2 = 0; x2 < width; x2++ ) {
         for ( int y2 = 0; y2 < height; y2++ ) {
            if ( x1 != x2 || y1 != y2 ) {
               if ( isAsteroid( x2, y2, inputs ) ) {
                  double angle = getSlope( x1, y1, x2, y2 );
                  //               System.out.println( "Slope between " + x1 + "," + y1 + "," + x2 + "," + y2 + " is " + slope );
                  if ( seenAngles.contains( angle ) ) {
                     numOtherAsteroids--;
                  }
                  else {
                     seenAngles.add( angle );
                  }
               }
            }
         }
      }
      return numOtherAsteroids;
   }

   private static int getMostSeenAsteroids( List<String> inputs ) {
      int mostSeen = 0;
      int width = inputs.get( 0 ).length();
      int height = inputs.size();

      int[][] seens = new int[width][height];

      for ( int x = 0; x < width; x++ ) {
         for ( int y = 0; y < height; y++ ) {
            int seen = getNumAsteroidsSeenFromPoint( x, y, inputs );
            mostSeen = Math.max( mostSeen, seen );
            seens[x][y] = seen;
         }
      }
//      for ( int y = 0; y < height; y++ ) {
//         for ( int x = 0; x < width; x++ ) {
//            System.out.print( seens[x][y] );
//         }
//         System.out.println();
//      }

      return mostSeen;
   }

   private static boolean isAsteroid( int x, int y, List<String> inputs ) {
      return inputs.get( y ).charAt( x ) == ASTEROID;
   }

   private static double getSlope( int x1, int y1, int x2, int y2 ) {
      double rise =  y2 - y1;
      double run = x2 - x1;
      return Math.atan2( rise, run );
   }
}
