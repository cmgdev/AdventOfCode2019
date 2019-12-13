package day10;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 10;

   private static final char ASTEROID = '#';

   private static int numTotalAsteroids = 0;

   private enum Keys {
      NUM_SEEN,
      X,
      Y
   }

   private static final int UPPER_RIGHT = 1;
   private static final int LOWER_RIGHT = 2;
   private static final int LOWER_LEFT = 3;
   private static final int UPPER_LEFT = 4;

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

      int numAsteroids = getMostSeenAsteroids( inputs ).get( Keys.NUM_SEEN );
      int expected = IS_TEST ? 210 : 269;

      System.out.println( "Most seen asteroids is " + numAsteroids );
      System.out.println( expected == numAsteroids );
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );

      Puzzle puzzle = new Puzzle();
      List<String> inputs = puzzle.readFile( "//" );

      Map<Keys, Integer> resultMap = getMostSeenAsteroids( inputs );

      Map<Double, List<Asteroid>> relativeAsteroids = getAsteroidsRelativeToPoint( resultMap.get( Keys.X ),
            resultMap.get( Keys.Y ), inputs );

      // sort asteroids by distance
      for ( List<Asteroid> asteroids : relativeAsteroids.values() ) {
         Collections.sort( asteroids, ( a1, a2 ) -> {
            if ( a1.getDistance() == a2.getDistance() ) {
               return 0;
            }
            return a1.getDistance() > a2.getDistance() ? 1 : -1;
         } );
      }
      // -1.5707963267948966 straight up has 12

      List<Double> sortedDegreesClockwiseFrom90 = sortDegreesClockwiseFrom90( relativeAsteroids.keySet() );
      int removed = 0;
      Asteroid asteroid200 = null;

      while ( asteroid200 == null ) {
         for ( int i = 0; i < sortedDegreesClockwiseFrom90.size(); i++ ) {
            List<Asteroid> asteroids = relativeAsteroids.get( sortedDegreesClockwiseFrom90.get( i ) );
            if ( !asteroids.isEmpty() ) {
               Asteroid asteroid = asteroids.remove( 0 );
               removed++;

               if ( removed == 200 ) {
                  asteroid200 = asteroid;
                  break;
               }
            }
         }
      }

      System.out.println( "Asteroid200 is " + asteroid200 );
      int answer = (asteroid200.getX() * 100) + asteroid200.getY();
      int expected = IS_TEST ? 802 : 612;
      System.out.println( answer );
      System.out.println( answer == expected );
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
                  double angle = getAtan2( x1, y1, x2, y2 );
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

   private static Map<Double, List<Asteroid>> getAsteroidsRelativeToPoint( int x1, int y1, List<String> inputs ) {
      Map<Double, List<Asteroid>> asteroidsRelativeToPoint = new HashMap<>();

      int width = inputs.get( 0 ).length();
      int height = inputs.size();

      for ( int x2 = 0; x2 < width; x2++ ) {
         for ( int y2 = 0; y2 < height; y2++ ) {
            if ( x1 != x2 || y1 != y2 ) {
               if ( isAsteroid( x2, y2, inputs ) ) {
                  double angle = Math.toDegrees( getAtan2( x1, y1, x2, y2 ) );
                  double distance = getDistance( x1, y1, x2, y2 );
                  List<Asteroid> distances = asteroidsRelativeToPoint.getOrDefault( angle, new ArrayList<>() );
                  distances.add( new Asteroid( distance, x2, y2 ) );
                  asteroidsRelativeToPoint.put( angle, distances );
               }
            }
         }
      }

      return asteroidsRelativeToPoint;
   }

   private static Map<Keys, Integer> getMostSeenAsteroids( List<String> inputs ) {
      Map<Keys, Integer> result = new HashMap<>();
      result.put( Keys.NUM_SEEN, 0 );
      result.put( Keys.X, 0 );
      result.put( Keys.Y, 0 );
      int width = inputs.get( 0 ).length();
      int height = inputs.size();

      for ( int x = 0; x < width; x++ ) {
         for ( int y = 0; y < height; y++ ) {
            int seen = getNumAsteroidsSeenFromPoint( x, y, inputs );
            if ( seen > result.get( Keys.NUM_SEEN ) ) {
               result.put( Keys.NUM_SEEN, seen );
               result.put( Keys.X, x );
               result.put( Keys.Y, y );
            }
         }
      }

      return result;
   }

   private static boolean isAsteroid( int x, int y, List<String> inputs ) {
      return inputs.get( y ).charAt( x ) == ASTEROID;
   }

   private static double getAtan2( int x1, int y1, int x2, int y2 ) {
      double rise =  y2 - y1;
      double run = x2 - x1;
      return Math.atan2( rise, run ) * -1;
   }

   private static double getDistance( int x1, int y1, int x2, int y2 ) {
      double x = Math.pow( x2 - x1, 2 );
      double y = Math.pow( y2 - y1, 2 );
      return Math.sqrt( x + y );
   }

   private static List<Double> sortDegreesClockwiseFrom90( Collection<Double> degrees ) {
      List<Double> sortedDegrees = new ArrayList<>( degrees );
      Collections.sort( sortedDegrees, ( o1, o2 ) -> {
         Integer o1Quadrant = getQuadrant( o1 );
         Integer o2Quadrant = getQuadrant( o2 );

         if ( o1Quadrant != o2Quadrant ) {
            return o1Quadrant.compareTo( o2Quadrant );
         }
         return o2.compareTo( o1 );
      } );
      return sortedDegrees;
   }

   private static int getQuadrant( double deg ) {
      if ( deg <= 90 && deg >= 0 ) {
         return UPPER_RIGHT;
      }
      else if ( deg >= -90 && deg < 0 ) {
         return LOWER_RIGHT;
      }
      else if ( deg >= -180 && deg < -90 ) {
         return LOWER_LEFT;
      }
      return UPPER_LEFT;
   }

   public static class Asteroid {

      private double distance;
      private int x;
      private int y;

      public Asteroid( double distance, int x, int y ) {
         this.distance = distance;
         this.x = x;
         this.y = y;
      }

      public double getDistance() {
         return distance;
      }

      public int getX() {
         return x;
      }

      public int getY() {
         return y;
      }

      @Override
      public boolean equals( Object o ) {
         if ( this == o ) { return true; }
         if ( o == null || getClass() != o.getClass() ) { return false; }

         Asteroid asteroid = (Asteroid) o;

         if ( Double.compare( asteroid.distance, distance ) != 0 ) { return false; }
         if ( x != asteroid.x ) { return false; }
         return y == asteroid.y;
      }

      @Override
      public int hashCode() {
         int result;
         long temp;
         temp = Double.doubleToLongBits( distance );
         result = (int) (temp ^ (temp >>> 32));
         result = 31 * result + x;
         result = 31 * result + y;
         return result;
      }

      @Override
      public String toString() {
         return "Asteroid{" + "distance=" + distance + ", x=" + x + ", y=" + y + '}';
      }
   }
}
