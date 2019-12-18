package day15;

import base.AbstractPuzzle;
import base.model.Intcode;
import base.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 15;
   public static final Point startingPoint = new Point( 0, 0 );

   enum Direction {
      NORTH( 1, 0, 1 ),
      SOUTH( 2, 0, -1 ),
      WEST( 3, -1, 0 ),
      EAST( 4, 1, 0 );

      int id;
      int relativeX;
      int relativeY;

      Direction( int id, int relativeX, int relativeY ) {
         this.id = id;
         this.relativeX = relativeX;
         this.relativeY = relativeY;
      }

      public static Direction getById( int id ) {
         for ( Direction direction : Direction.values() ) {
            if ( direction.id == id ) {
               return direction;
            }
         }
         return null;
      }

      public Direction getOppositeDirection() {
         if ( this.equals( Direction.NORTH ) ) {
            return Direction.SOUTH;
         }
         if ( this.equals( Direction.SOUTH ) ) {
            return Direction.NORTH;
         }
         if ( this.equals( Direction.EAST ) ) {
            return Direction.WEST;
         }
         if ( this.equals( Direction.WEST ) ) {
            return Direction.EAST;
         }
         return null;
      }
   }

   enum Status {
      WALL( 0, false, '#' ),
      OK( 1, true, ' ' ),
      OXYGEN( 2, true, 'O' );

      int id;
      boolean hasMoved;
      char character;

      Status( int id, boolean hasMoved, char character ) {
         this.id = id;
         this.hasMoved = hasMoved;
         this.character = character;
      }

      public static Status getStatusById( int id ) {
         for ( Status s : Status.values() ) {
            if ( s.id == id ) {
               return s;
            }
         }
         return null;
      }
   }

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
      String instructions = puzzle.readFile().get( 0 );

      Map<Point, Status> pointsToOxygenSystem = new HashMap<>();
      int numUnknown = 4;
      int numUnknownThisRound = 5;

      while ( numUnknown != numUnknownThisRound ) {
         numUnknown = numUnknownThisRound;
         numUnknownThisRound = 0;
         Intcode intcode = new Intcode( instructions );
         pointsToOxygenSystem.putAll( findOxygenSystem( intcode ) );

         int minX = 0, maxX = 0, minY = 0, maxY = 0;
         for ( Point p : pointsToOxygenSystem.keySet() ) {
            minX = Math.min( minX, p.getX() );
            maxX = Math.max( maxX, p.getX() );
            minY = Math.min( minY, p.getY() );
            maxY = Math.max( maxY, p.getY() );
         }

         for ( int y = maxY; y >= minY; y-- ) {
            for ( int x = minX; x < maxX; x++ ) {
               char print = '?';
               if ( x == 0 && y == 0 ) {
                  print = 'S';
               }
               else {
                  Status s = pointsToOxygenSystem.get( new Point( x, y ) );
                  if ( s != null ) {
                     print = s.character;
                  }
                  else {
                     numUnknownThisRound++;
                  }
               }

               System.out.print( print );
            }
            System.out.println();
         }
         System.out.println( "Still haven't visited " + numUnknown );
         //         sleep();
      }

      // 1065 too high, 66 wrong, 314 too low, 315 wrong

      int shortestDistance = getShortestDistanceToOxygenSystem( pointsToOxygenSystem );
      System.out.println( "Shortest distance is " + shortestDistance );
      long expected = puzzle.getAnswer1();
      System.out.println( expected == shortestDistance );
   }


   private static int getShortestDistanceToOxygenSystem( Map<Point, Status> points ) {
      Map<Point, Point> pointsToParents = breadthFirstSearch( points );

      Point oxygen = null;
      for ( Map.Entry<Point, Status> entry : points.entrySet() ) {
         if ( Status.OXYGEN.equals( entry.getValue() ) ) {
            oxygen = entry.getKey();
            break;
         }
      }

      int length = 0;

      Point current = oxygen;
      while ( !current.equals( startingPoint ) ) {
//         System.out.println( current );
         current = pointsToParents.get( current );
         length++;
      }

      return length;
   }

   private static Map<Point, Point> breadthFirstSearch( Map<Point, Status> points ) {
      Map<Point, Point> pointsToParents = new HashMap<>();

      List<Point> queue = new ArrayList<>();
      queue.add( startingPoint );

      while ( !queue.isEmpty() ) {
         Point next = queue.remove( queue.size() - 1 );
         for ( Direction direction : Direction.values() ) {
            Point p = new Point( next.getX() + direction.relativeX, next.getY() + direction.relativeY );
            Status s = points.get( p );
            if ( (Status.OK.equals( s ) || Status.OXYGEN.equals( s )) && !pointsToParents.containsKey( p ) ) {
               pointsToParents.put( p, next );
               queue.add( p );
            }
         }
      }

      return pointsToParents;
   }

   private static void sleep() {
      try {
         Thread.sleep( 500 );
      }
      catch ( InterruptedException e ) {
         e.printStackTrace();
      }
   }

   private static Map<Point, Status> findOxygenSystem( Intcode intcode ) {
      Random random = new Random();

      Point current = startingPoint;
      Map<Point, Status> points = new HashMap<>();
      points.put( current, Status.OK );

      Status status = Status.OK;
      while ( status != Status.OXYGEN ) {
         List<Direction> unexplored = getSurroundingUnexploredDirections( points, current );

         // pick a random direction
         Direction nextDirection = unexplored.isEmpty() ?
               Direction.getById( random.nextInt( Direction.values().length ) + 1 ) :
               unexplored.get( random.nextInt( unexplored.size() ) );
         Point nextPoint = new Point( current.getX() + nextDirection.relativeX, current.getY() + nextDirection.relativeY );

         intcode.addInput( nextDirection.id );
         intcode.runProgram();
         status = Status.getStatusById( intcode.getOutput().get().intValue() );

         points.put( nextPoint, status );
         if ( status.hasMoved ) {
            current = nextPoint;
         }

      }

      return points;
   }

   private static List<Direction> getSurroundingUnexploredDirections( Map<Point, Status> exploredPoints, Point current ) {
      List<Direction> unexploredDirections = new ArrayList<>();

      for ( Direction direction : Direction.values() ) {
         if ( !exploredPoints
               .containsKey( new Point( current.getX() + direction.relativeX, current.getY() + direction.relativeY ) ) ) {
            unexploredDirections.add( direction );
         }
      }
      return unexploredDirections;
   }

   private static List<Direction> getSurroundingOpenDirections( Map<Point, Status> exploredPoints, Point current,
         Direction lastDirection ) {
      List<Direction> openDirections = new ArrayList<>();

      for ( Direction direction : Direction.values() ) {
         if ( lastDirection != null && direction == lastDirection.getOppositeDirection() ) {
            continue;
         }
         if ( Status.OK.equals(
               exploredPoints.get( new Point( current.getX() + direction.relativeX, current.getY() + direction.relativeY ) ) ) ) {
            openDirections.add( direction );
         }
      }
      return openDirections;
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
   }


}
