package day09;

import base.AbstractPuzzle;
import day05.Intcode;

import java.util.Optional;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 9;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      solve1();
      solve2();
      solveQuine();
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

   private static void solveQuine() {
      System.out.println( "Solving Quine" );

      String instructions = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";

      Intcode intcode = new Intcode( instructions );
      intcode.runProgram();
      Optional<Long> output = intcode.getOutput();
      while ( output.isPresent() ) {
         System.out.println( output.get() );
         output = intcode.getOutput();
      }
   }
}
