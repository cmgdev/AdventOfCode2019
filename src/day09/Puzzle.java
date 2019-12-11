package day09;

import base.AbstractPuzzle;
import day05.Intcode;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 9;

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
      if ( !IS_TEST ) {
         intcode.addInput( 1 );
      }
      intcode.runProgram();
      long output = intcode.getOutput().get();
      long expected = IS_TEST ? 1125899906842624l : 3512778005l;

      System.out.println( "Output is " + output );
      System.out.println( expected == output );

   }

   private static void solve2() {
      System.out.println( "Solving 2..." );

      Puzzle puzzle = new Puzzle();
      String instructions = puzzle.readFile().get( 0 );

      Intcode intcode = new Intcode( instructions );
      intcode.addInput( 2 );
      intcode.runProgram();
      long output = intcode.getOutput().get();
      System.out.println( "Output is " + output );
      System.out.println( 35920 == output );
   }

}
