package day05;

import java.util.ArrayList;
import java.util.List;

public class Intcode {

   private List<Integer> program = new ArrayList();
   private int current = 0;

   public static final int OP_ADD = 1;
   public static final int OP_MULTIPLY = 2;
   public static final int OP_WRITE = 3;
   public static final int OP_READ = 4;
   public static final int OP_JUMP_IF_TRUE = 5;
   public static final int OP_JUMP_IF_FALSE = 6;
   public static final int OP_LESS_THAN = 7;
   public static final int OP_EQUALS = 8;

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
      List<Integer> inputs = new ArrayList<>();
      inputs.add( input );
      return runProgram( inputs );
   }

   public int runProgram( List<Integer> inputs ) {
      int result = 0;
      while ( !shouldStop() ) {
         result = doOp( inputs );
      }
      return result;
   }

   private int doOp( List<Integer> inputs ) {
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

         else if ( opCode == OP_LESS_THAN ) {
            int lessThan = getValue( firstPosition ) < getValue( secondPosition ) ? 1 : 0;
            program.set( thirdPosition, lessThan );
         }

         else if ( opCode == OP_EQUALS ) {
            int equals = getValue( firstPosition ) == getValue( secondPosition ) ? 1 : 0;
            program.set( thirdPosition, equals );
         }

         next( 4 );
         return 0;
      }
      else if ( opCode == OP_WRITE || opCode == OP_READ ) {
         int result = 0;

         if ( opCode == OP_WRITE ) {
            program.set( firstPosition, inputs.remove( 0 ) );
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

   public List<Integer> getProgram() {
      return program;
   }
}