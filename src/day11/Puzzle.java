package day11;

import base.AbstractPuzzle;
import day05.Intcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 11;

   public static final int BLACK = 0;
   public static final int WHITE = 1;

   public static final int LEFT = 0;
   public static final int RIGHT = 1;

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

      Intcode intcode = new Intcode( instructions );

      Robot robot = new Robot();
      Intcode.ExitCondition exitCondition = Intcode.ExitCondition.CONTINUE;
      while ( exitCondition != Intcode.ExitCondition.OK ) {
         intcode.addInput( robot.getCurrentLocationColor() );
         exitCondition = intcode.runProgram();

         int direction = intcode.getOutput().get().intValue();
         int paintColor = intcode.getOutput().get().intValue();

         robot.setCurrentLocationColor( paintColor );
         robot.turnAndMove( direction );
      }

      System.out.println( robot.panelGrid );

      long countPainted = robot.panelGrid.values().stream().map( Panel::isPainted ).filter( p -> p == true ).count();
      System.out.println( countPainted + " " + (countPainted > 2184) );
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
      Puzzle puzzle = new Puzzle();
      List<String> inputs = puzzle.readFile();
   }

   public static class Robot {

      Map<String, Panel> panelGrid = new HashMap<>();
      String currentLocation = "0,0";
      char currentDirection = 'U';

      public Robot() {
         panelGrid.put( currentLocation, new Panel() );
      }

      public void setCurrentLocationColor( int color ) {
         Panel panel = panelGrid.getOrDefault( currentLocation, new Panel() );
         panel.setColor( color );
         panelGrid.put( currentLocation, panel );
      }

      public int getCurrentLocationColor() {
         return panelGrid.getOrDefault( currentLocation, new Panel() ).getColor();
      }

      public void turnAndMove( int direction ) {
         turn( direction );
         move();
      }

      public void turn( int direction ) {
         if ( direction == RIGHT ) {
            if ( currentDirection == 'U' ) {
               currentDirection = 'R';
            }
            else if ( currentDirection == 'R' ) {
               currentDirection = 'D';
            }
            else if ( currentDirection == 'D' ) {
               currentDirection = 'L';
            }
            else if ( currentDirection == 'L' ) {
               currentDirection = 'U';
            }
         }
         else if ( direction == LEFT ) {
            if ( currentDirection == 'U' ) {
               currentDirection = 'L';
            }
            else if ( currentDirection == 'R' ) {
               currentDirection = 'U';
            }
            else if ( currentDirection == 'D' ) {
               currentDirection = 'R';
            }
            else if ( currentDirection == 'L' ) {
               currentDirection = 'D';
            }
         }
      }

      public void move() {
         if ( currentDirection == 'U' ) {
            setY( getY() + 1 );
         }
         else if ( currentDirection == 'D' ) {
            setY( getY() - 1 );
         }
         else if ( currentDirection == 'R' ) {
            setX( getX() + 1 );
         }
         else if ( currentDirection == 'L' ) {
            setX( getX() - 1 );
         }
         System.out.println( currentLocation );
      }

      public int getX() {
         return Integer.parseInt( getXY()[0] );
      }

      public int getY() {
         return Integer.parseInt( getXY()[1] );
      }

      public void setX( int x ) {
         currentLocation = x + "," + getY();
      }

      public void setY( int y ) {
         currentLocation = getX() + "," + y;
      }

      public String[] getXY() {
         return currentLocation.split( "," );
      }

   }

   public static class Panel {

      private int color = BLACK;
      private boolean isPainted = false;

      public int getColor() {
         return color;
      }

      public void setColor( int color ) {
         this.color = color;
         this.isPainted = true;
      }

      public boolean isPainted() {
         return isPainted;
      }

      @Override
      public String toString() {
         return "Panel{" + "color=" + color + ", isPainted=" + isPainted + '}';
      }
   }
}
