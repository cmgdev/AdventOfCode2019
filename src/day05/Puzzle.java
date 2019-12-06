package day05;

import base.AbstractPuzzle;

import java.util.ArrayList;
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
      int input = 1;
      int result = intcode.runProgram( input );

      System.out.println( "Result is " + result );
      if ( IS_TEST ) {
         System.out.println( 0 == result );
         System.out.println( intcode.program );
      }
      else {
         System.out.println( 4887191 == result );
      }
   }

   public static void solve2( List<String> input ) {
      System.out.println( "Solving 2..." );
   }

   public static class Intcode {

      private List<Integer> program = new ArrayList();
      private int current = 0;

      public Intcode( String instructions ) {
         init( instructions );
      }

      public Intcode( String instructions, int replaceFirst, int replaceSecond ) {
         init( instructions );
         program.set( 1, replaceFirst );
         program.set( 2, replaceSecond );
      }

      private void init( String instructions ) {
         String[] s = instructions.split( "," );
         for ( int i = 0; i < s.length; i++ ) {
            program.add( Integer.parseInt( s[i] ) );
         }
      }

      private int getCurrent() {
         return current;
      }

      private void next( int inc ) {
         current = current + inc;
      }

      public int getPosition0() {
         return program.get( 0 );
      }

      public int runProgram( int input ) {
         int result = input;
         while ( !shouldStop() ) {
            //            System.out.println( program );
            result = doOp( result );
         }
         //         System.out.println( program );

         return result;
      }

      private int doOp( int input ) {
         int opCode = getOpCode();
         int firstParamMode = getParamMode( 1 );
         int secondParamMode = getParamMode( 2 );
         int thirdParamMode = getParamMode( 3 );

         int firstPosition = getPositionNumber( 1, firstParamMode );

         if ( opCode == 1 || opCode == 2 ) {
            int secondPosition = getPositionNumber( 2, secondParamMode );
            int thirdPosition = getPositionNumber( 3, thirdParamMode );

            if ( opCode == 1 ) {
               program.set( thirdPosition, getValue( firstPosition ) + getValue( secondPosition ) );
            }
            else if ( opCode == 2 ) {
               program.set( thirdPosition, getValue( firstPosition ) * getValue( secondPosition ) );
            }
            next( 4 );
            return 0;
         }
         else if ( opCode == 3 || opCode == 4 ) {
            int result = 0;
            if ( opCode == 3 ) {
               program.set( firstPosition, input );
            }
            else if ( opCode == 4 ) {
               result = program.get( firstPosition );
            }
            next( 2 );
            return result;
         }
         else {
            throw new RuntimeException( "invalid opCode " + opCode + " at position " + getCurrent() );
         }

      }

      private int getOpCode() {
         return program.get( getCurrent() ) % 10;
      }

      private int getParamMode( int paramNumber ) {
         int fullInstruction = program.get( getCurrent() );
         if ( paramNumber == 3 ) {
            return fullInstruction / 10000;
         }
         else if ( paramNumber == 2 ) {
            return (fullInstruction / 1000) % 10;
         }
         else if ( paramNumber == 1 ) {
            return (fullInstruction / 100) % 10;
         }
         else {
            throw new RuntimeException( "invalid param mode " + fullInstruction + " param number " + paramNumber );
         }
      }

      private int getPositionNumber( int paramNumber, int paramMode ) {
         if ( paramMode == 0 ) {
            return program.get( getCurrent() + paramNumber );
         }
         else if ( paramMode == 1 ) {
            return getCurrent() + paramNumber;
         }
         else {
            throw new RuntimeException( "invalid paramMode " + paramMode );
         }
      }

      private int getValue( int positionNumber ) {
         return program.get( positionNumber );
      }

      private boolean shouldStop() {
         return program.get( getCurrent() ) == 99;
      }
   }
}
