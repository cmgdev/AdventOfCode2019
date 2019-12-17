package day05;

import base.AbstractPuzzle;
import base.model.Intcode;

import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 5;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      Puzzle puzzle = new Puzzle();
      List<String> instructions = puzzle.readFile();
      solve1( instructions );
      solve2( instructions );
   }

   public static void solve1( List<String> instructions ) {
      System.out.println( "Solving 1..." );
      Intcode intcode = new Intcode( instructions.get( 0 ) );
      intcode.addInput( 1 );
      intcode.runProgram();

      long result = intcode.getOutput().get();

      System.out.println( "Result is " + result );
      if ( IS_TEST ) {
         System.out.println( 0 == result );
         System.out.println( intcode.getProgram() );
      }
      else {
         System.out.println( 4887191 == result );
      }
   }

   public static void solve2( List<String> instructions ) {
      System.out.println( "Solving 2..." );

      if( IS_TEST ) {
         Intcode intcode = new Intcode( "3,3,1105,-1,9,1101,0,0,12,4,12,99,1" );
         intcode.addInput( 0 );
         intcode.runProgram();
         long result = intcode.getOutput().get();

         System.out.println( "Result is " + result );
      }
      else {
         Intcode intcode = new Intcode( instructions.get( 0 ) );
         intcode.addInput( 5 );
         intcode.runProgram();
         long result = intcode.getOutput().get();

         System.out.println( "Result is " + result );
         System.out.println( 3419022 == result );
      }
   }
}
