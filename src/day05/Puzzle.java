package day05;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 5;

   public static final int OP_ADD = 1;
   public static final int OP_MULTIPLY = 2;
   public static final int OP_WRITE = 3;
   public static final int OP_READ = 4;
   public static final int OP_JUMP_IF_TRUE = 5;
   public static final int OP_JUMP_IF_FALSE = 6;
   public static final int OP_LESS_THAN = 7;
   public static final int OP_EQUALS = 8;

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

   public static void solve2( List<String> instructions ) {
      System.out.println( "Solving 2..." );

      if( IS_TEST ) {
         Intcode intcode = new Intcode( "3,3,1105,-1,9,1101,0,0,12,4,12,99,1" );
         int input = 0;
         int result = intcode.runProgram( input );

         System.out.println( "Result is " + result );
      }
      else {
         Intcode intcode = new Intcode( instructions.get( 0 ) );
         int input = 5;
         int result = intcode.runProgram( input );

         System.out.println( "Result is " + result );
         System.out.println( 3419022 == result );
      }
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
         current += inc;
      }

      private void jump( int position ) {
         current = position;
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
         int firstPosition = getPositionNumber( 1 );

         if ( opCode == OP_ADD || opCode == OP_MULTIPLY || opCode == OP_LESS_THAN || opCode == OP_EQUALS ) {
            int secondPosition = getPositionNumber( 2 );
            int thirdPosition = getPositionNumber( 3 );

            if ( opCode == OP_ADD ) {
               program.set( thirdPosition, getValue( firstPosition ) + getValue( secondPosition ) );
            }

            else if ( opCode == OP_MULTIPLY ) {
               program.set( thirdPosition, getValue( firstPosition ) * getValue( secondPosition ) );
            }

            else if( opCode == OP_LESS_THAN ) {
               int lessThan = getValue( firstPosition ) < getValue( secondPosition ) ? 1 : 0;
               program.set( thirdPosition, lessThan );
            }

            else if( opCode == OP_EQUALS ) {
               int equals = getValue( firstPosition ) == getValue( secondPosition ) ? 1 : 0;
               program.set( thirdPosition, equals );
            }

            next( 4 );
            return 0;
         }
         else if ( opCode == OP_WRITE || opCode == OP_READ ) {
            int result = 0;

            if ( opCode == OP_WRITE ) {
               program.set( firstPosition, input );
            }

            else if ( opCode == OP_READ ) {
               result = program.get( firstPosition );
            }
            next( 2 );
            return result;
         }
         else if ( opCode == OP_JUMP_IF_TRUE || opCode == OP_JUMP_IF_FALSE ) {
            int firstVal = getValue( firstPosition );
            int secondPosition = getPositionNumber( 2 );

            if ( (opCode == OP_JUMP_IF_TRUE && firstVal != 0) || (opCode == OP_JUMP_IF_FALSE && firstVal == 0) ) {
               jump( getValue( secondPosition ) );
            }

            else {
               next( 3 );
            }
            return 0;
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
         if ( paramNumber >= 1 && paramNumber <= 3 ) {
            return (int) ((fullInstruction / Math.pow( 10, (paramNumber + 1) )) % 10);
         }
         else {
            throw new RuntimeException( "invalid param mode " + fullInstruction + " param number " + paramNumber );
         }
      }

      private int getPositionNumber( int paramNumber ) {
         int paramMode = getParamMode( paramNumber );
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
