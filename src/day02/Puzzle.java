package day02;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 2;

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

      Intcode intcode = new Intcode( input.get( 0 ) );
      intcode.runProgram();
      int result = intcode.getPosition0();

      System.out.println( "Result is " + result );
      if ( IS_TEST ) {
         System.out.println( result == 2 );
      }
      else {
         System.out.println( result == 3101844 );
      }
   }

   public static void solve2( List<String> input ) {
      System.out.println( "Solving 2..." );

      for ( int noun = 0; noun < 99; noun++ ) {
         for ( int verb = 0; verb < 99; verb++ ) {
            Intcode intcode = new Intcode( input.get( 0 ), noun, verb );
            intcode.runProgram();
            int result = intcode.getPosition0();

            if ( result == 19690720 ) {
               System.out.println( "noun=" + noun );
               System.out.println( "verb=" + verb );
               System.out.println( (100 * noun) + verb == 8478 );
               return;
            }
         }
      }
      System.out.println( "Shit! No answer found!" );
   }

   public static class Intcode {

      private List<Integer> program = new ArrayList();
      private int current = 0;

      public Intcode( String input ) {
         init( input );
      }

      public Intcode( String input, int replaceFirst, int replaceSecond ) {
         init( input );
         program.set( 1, replaceFirst );
         program.set( 2, replaceSecond );
      }

      private void init( String input ) {
         String[] s = input.split( "," );
         for ( int i = 0; i < s.length; i++ ) {
            program.add( Integer.parseInt( s[i] ) );
         }
      }

      private int getCurrent() {
         return current;
      }

      private void next() {
         current = current + 4;
      }

      public int getPosition0() {
         return program.get( 0 );
      }

      public void runProgram() {
         while ( !shouldStop() ) {
            //            System.out.println( program );
            doOp();
         }
         //         System.out.println( program );
      }

      private void doOp() {
         int currentPosition = getCurrent();
         int firstPosition = program.get( currentPosition + 1 );
         int secondPosition = program.get( currentPosition + 2 );
         int resultPosition = program.get( currentPosition + 3 );

         int opCode = program.get( currentPosition );
         int first = program.get( firstPosition );
         int second = program.get( secondPosition );

         if ( opCode == 1 ) {
            program.set( resultPosition, first + second );
         }
         else if ( opCode == 2 ) {
            program.set( resultPosition, first * second );
         }
         else {
            throw new RuntimeException( "invalid opCode " + opCode + " at position " + currentPosition );
         }
         next();
      }

      private boolean shouldStop() {
         return program.get( getCurrent() ) == 99;
      }
   }
}
