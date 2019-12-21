package day15;

import base.AbstractPuzzle;
import base.model.Direction;
import base.model.Intcode;
import base.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 15;
   public static final Point startingPoint = new Point( 0, 0 );

   public static final Direction[] directions = new Direction[]{ Direction.NORTH, Direction.SOUTH, Direction.WEST,
         Direction.EAST };

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
      Map<Point, Status> pointsToOxygenSystem = solve1();
      solve2( pointsToOxygenSystem );
   }

   private static Map<Point, Status> solve1() {
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

         String pointMapToString = Point.pointMapToString( pointsToOxygenSystem, ( point, status ) -> {
            String print = "?";
            if ( point.getX() == 0 && point.getY() == 0 ) {
               print = "S";
            }
            else if ( status != null ) {
               print = String.valueOf( status.character );
            }
            return print;
         } );

         System.out.println( pointMapToString );

         for ( char c : pointMapToString.toCharArray() ) {
            if ( c == '?' ) {
               numUnknownThisRound++;
            }
         }
         System.out.println( "Still haven't visited " + numUnknown );
      }

      int shortestDistance = getShortestDistanceToOxygenSystem( pointsToOxygenSystem );
      System.out.println( "Shortest distance is " + shortestDistance );
      long expected = puzzle.getAnswer1();
      System.out.println( expected == shortestDistance );

      return pointsToOxygenSystem;
   }

   private static int getShortestDistanceToOxygenSystem( Map<Point, Status> points ) {
      Map<Point, Point> pointsToParents = breadthFirstSearch( points );

      Point oxygen = getPointWithOxygen( points );

      int length = 0;

      Point current = oxygen;
      while ( !current.equals( startingPoint ) ) {
//         System.out.println( current );
         current = pointsToParents.get( current );
         length++;
      }

      return length;
   }

   private static Point getPointWithOxygen( Map<Point, Status> points ) {
      Point oxygen = null;
      for ( Map.Entry<Point, Status> entry : points.entrySet() ) {
         if ( Status.OXYGEN.equals( entry.getValue() ) ) {
            oxygen = entry.getKey();
            break;
         }
      }
      return oxygen;
   }

   private static Map<Point, Point> breadthFirstSearch( Map<Point, Status> points ) {
      Map<Point, Point> pointsToParents = new HashMap<>();

      List<Point> queue = new ArrayList<>();
      queue.add( startingPoint );

      while ( !queue.isEmpty() ) {
         Point next = queue.remove( queue.size() - 1 );
         for ( Point p : next.getAdjacentPoints() ) {
            Status s = points.get( p );
            if ( (Status.OK.equals( s ) || Status.OXYGEN.equals( s )) && !pointsToParents.containsKey( p ) ) {
               pointsToParents.put( p, next );
               queue.add( p );
            }
         }
      }

      return pointsToParents;
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
         Direction nextDirection = unexplored.isEmpty() ? getDirectionById( random.nextInt( Direction.values().length ) + 1 ) :
               unexplored.get( random.nextInt( unexplored.size() ) );
         Point nextPoint = current.getAdjacentPoint( nextDirection );

         intcode.addInput( getDirectionId( nextDirection ) );
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
         if ( !exploredPoints.containsKey( current.getAdjacentPoint( direction ) ) ) {
            unexploredDirections.add( direction );
         }
      }
      return unexploredDirections;
   }

   private static void solve2( Map<Point, Status> pointsToOxygenSystem ) {
      System.out.println( "Solving 2..." );

      int numMinutes = 0;

      int numOpenPoints = countOpenPoints( pointsToOxygenSystem );
      while ( numOpenPoints > 0 ) {

         List<Point> pointsWithOxygen = getPointsWithOxygen( pointsToOxygenSystem );
         List<Point> getOxygenThisMinute = new ArrayList<>();

         for ( Point pointWithOxygen : pointsWithOxygen ) {
            for ( Point adjacent : pointWithOxygen.getAdjacentPoints() ) {
               if ( Status.OK.equals( pointsToOxygenSystem.get( adjacent ) ) ) {
                  getOxygenThisMinute.add( adjacent );
               }
            }
         }
         for ( Point addOxygen : getOxygenThisMinute ) {
            pointsToOxygenSystem.put( addOxygen, Status.OXYGEN );
         }
         System.out.println( "Add oxygen to " + getOxygenThisMinute.size() );
         numMinutes++;
         numOpenPoints = countOpenPoints( pointsToOxygenSystem );
      }

      System.out.println( "Took " + numMinutes );

      Puzzle puzzle = new Puzzle();
      puzzle.readFile();
      long expected = puzzle.getAnswer2();

      System.out.println( expected == numMinutes );

   }

   private static int countOpenPoints( Map<Point, Status> points ) {
      return (int) points.values().stream().filter( s -> Status.OK.equals( s ) ).count();
   }

   private static List<Point> getPointsWithOxygen( Map<Point, Status> points ) {
      return points.entrySet().stream().filter( e -> Status.OXYGEN.equals( e.getValue() ) ).map( Map.Entry::getKey )
            .collect( Collectors.toList() );
   }

   public static Direction getDirectionById( int id ) {
      return directions[id - 1];
   }

   public static int getDirectionId( Direction direction ) {
      for ( int i = 0; i < directions.length; i++ ) {
         if ( directions[i].equals( direction ) ) {
            return i + 1;
         }
      }
      return -1;
   }


}
