package day03;

import base.AbstractPuzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 3;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      Puzzle puzzle = new Puzzle();
      List<String> input = puzzle.readFile();

      solve1( input );
      solve2( input );
   }

   public static void solve1( List<String> input ) {
      System.out.println( "Solving 1..." );

      PointMap pointMap = new PointMap();
      pointMap.occupyPoints( input );

      String closestMultiOccupiedPoint = pointMap.getClosestMultiOccupiedPoint();
      int shortestDistance = pointMap.getManhattanDistance( closestMultiOccupiedPoint );
      System.out.println( "Shortest distance is " + shortestDistance );
      if ( IS_TEST ) {
//         System.out.println( pointMap );
         System.out.println( shortestDistance == 6 );
      }
      else {
         System.out.println( shortestDistance == 1084 );
      }
   }

   public static void solve2( List<String> input ) {
      System.out.println( "Solving 2..." );

      PointMap pointMap = new PointMap();
      pointMap.occupyPoints( input );

      int shortestDistance = pointMap.getShortestDistanceToMultiOccupiedPoint();
      System.out.println( "Shortest distance is " + shortestDistance );
      if ( IS_TEST ) {
//         System.out.println( pointMap );
         System.out.println( shortestDistance == 410 );
      }
      else {
         System.out.println( shortestDistance == 9240 );
      }
   }

   public static class PointMap {

      private static final String keySep = ",";

      Map<String, Set<Occupant>> points = new HashMap<>();

      public void occupyPoints( List<String> inputs ) {
         int lineNumber = 0;
         for ( String input : inputs ) {
            String[] directions = input.split( "," );

            int startX = 0;
            int startY = 0;
            int encounterOrder = 0;

            for ( int i = 0; i < directions.length; i++ ) {
               String instruction = directions[i];
               char direction = instruction.charAt( 0 );
               int length = Integer.parseInt( instruction.substring( 1 ) );

               if ( direction == 'R' ) {
                  int endX = startX + length;
                  for ( int j = startX; j < endX; j++ ) {
                     occupyPoint( j, startY, lineNumber, encounterOrder++ );
                  }
                  startX = endX;
               }
               else if ( direction == 'L' ) {
                  int endX = startX - length;
                  for ( int j = startX; j > endX; j-- ) {
                     occupyPoint( j, startY, lineNumber, encounterOrder++ );
                  }
                  startX = endX;
               }
               else if ( direction == 'U' ) {
                  int endY = startY + length;
                  for ( int j = startY; j < endY; j++ ) {
                     occupyPoint( startX, j, lineNumber, encounterOrder++ );
                  }
                  startY = endY;
               }
               else if ( direction == 'D' ) {
                  int endY = startY - length;
                  for ( int j = startY; j > endY; j-- ) {
                     occupyPoint( startX, j, lineNumber, encounterOrder++ );
                  }
                  startY = endY;
               }
            }
            lineNumber++;
         }
      }

      public String getClosestMultiOccupiedPoint() {
         int closest = 9999999;
         String closestPoint = "";

         for ( String point : getMultiOccupiedPoints() ) {
            int distance = getManhattanDistance( point );
            if ( distance > 0 && distance < closest ) {
               closest = distance;
               closestPoint = point;
            }
         }

         return closestPoint;
      }

      public int getManhattanDistance( String location ) {
         String[] l = location.split( keySep );
         return Math.abs( Integer.valueOf( l[0] ) ) + Math.abs( Integer.valueOf( l[1] ) );
      }

      public int getShortestDistanceToMultiOccupiedPoint() {
         int distance = 9999999;

         for ( String point : getMultiOccupiedPoints() ) {
            Set<Occupant> occupants = points.get( point );
            int thisDistance = occupants.stream().mapToInt( Occupant::getEncounterOrder ).sum();
            if ( thisDistance > 0 && thisDistance < distance ) {
               distance = thisDistance;
            }
         }
         return distance;
      }

      private List<String> getMultiOccupiedPoints() {
         return points.entrySet().stream().filter( e -> e.getValue().size() > 1 )
               .filter( e -> !e.getKey().equals( getPointKey( 0, 0 ) ) ).map( Map.Entry::getKey )
               .collect( Collectors.toList() );
      }

      private void occupyPoint( int x, int y, int lineNumber, int encounterOrder ) {
         String key = getPointKey( x, y );
         Set<Occupant> occupants = points.getOrDefault( key, new HashSet<>() );
         occupants.add( new Occupant( lineNumber, encounterOrder ) );
         points.put( key, occupants );
      }

      private String getPointKey( int x, int y ) {
         return x + keySep + y;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         Map<String, Integer> corners = getCorners();
         String closest = getClosestMultiOccupiedPoint();

         for ( int y = corners.get( "MAX_Y" ); y >= corners.get( "MIN_Y" ); y-- ) {
            for ( int x = corners.get( "MIN_X" ); x <= corners.get( "MAX_X" ); x++ ) {
               String thisLocation = getPointKey( x, y );
               if ( thisLocation.equals( closest ) ) {
                  sb.append( "X" );
               }
               else if ( thisLocation.equals( getPointKey( 0, 0 ) ) ) {
                  sb.append( "C" );
               }
               else {
                  sb.append( points.getOrDefault( thisLocation, new HashSet<>() ).size() );
               }
            }
            sb.append( "\n" );
         }
         return sb.toString();
      }

      private Map<String, Integer> getCorners() {
         int minX = 0;
         int maxX = 0;
         int minY = 0;
         int maxY = 0;

         for ( String location : points.keySet() ) {
            String[] l = location.split( keySep );
            int x = Integer.parseInt( l[0] );
            int y = Integer.parseInt( l[1] );
            minX = Math.min( x, minX );
            maxX = Math.max( x, maxX );
            minY = Math.min( y, minY );
            maxY = Math.max( y, maxY );
         }

         Map<String, Integer> corners = new HashMap<>();
         corners.put( "MIN_X", minX );
         corners.put( "MAX_X", maxX );
         corners.put( "MIN_Y", minY );
         corners.put( "MAX_Y", maxY );

         return corners;
      }
   }

   public static class Occupant {

      int occupantId;
      int encounterOrder;

      public Occupant( int occupantId, int encounterOrder ) {
         this.occupantId = occupantId;
         this.encounterOrder = encounterOrder;
      }

      public int getEncounterOrder() {
         return encounterOrder;
      }

      @Override
      public String toString() {
         return occupantId + ":" + encounterOrder;
      }

      @Override
      public boolean equals( Object obj ) {
         return this.occupantId == ((Occupant) obj).occupantId;
      }

      @Override
      public int hashCode() {
         return occupantId;
      }
   }

}
