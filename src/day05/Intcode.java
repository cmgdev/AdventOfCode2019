package day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Intcode {

   private Map<Long, Long> program = new HashMap<>();
   private List<Long> inputs = new ArrayList();
   private List<Long> outputs = new ArrayList<>();

   private long current = 0;
   private long relativeBase = 0;

   public static final int OP_ADD = 1;
   public static final int OP_MULTIPLY = 2;
   public static final int OP_WRITE = 3;
   public static final int OP_READ = 4;
   public static final int OP_JUMP_IF_TRUE = 5;
   public static final int OP_JUMP_IF_FALSE = 6;
   public static final int OP_LESS_THAN = 7;
   public static final int OP_EQUALS = 8;
   public static final int OP_ADJUST_RELATIVE_BASE = 9;

   public static final int PARAM_MODE_POSITION = 0;
   public static final int PARAM_MODE_IMMEDIATE = 1;
   public static final int PARAM_MODE_RELATIVE = 2;

   public enum ExitCondition {
      OK,
      CONTINUE,
      INPUT_NEEDED
   }

   public Intcode( String instructions ) {
      init( instructions );
   }

   private void init( String instructions ) {
      String[] s = instructions.split( "," );
      for ( int i = 0; i < s.length; i++ ) {
         program.put( new Long( i ), Long.parseLong( s[i] ) );
      }
   }

   private long getCurrent() {
      return current;
   }

   private void next( long inc ) {
      current += inc;
   }

   private void jump( long position ) {
      current = position;
   }

   public ExitCondition runProgram() {
      ExitCondition exitCondition = ExitCondition.CONTINUE;
      while ( exitCondition == ExitCondition.CONTINUE ) {
         exitCondition = doOp();
      }
      return exitCondition;
   }

   private ExitCondition doOp() {
      if( shouldStop() ) {
         return ExitCondition.OK;
      }

      long opCode = getOpCode();
      long firstPosition = getPositionNumber( 1 );

      if ( opCode == OP_ADD || opCode == OP_MULTIPLY || opCode == OP_LESS_THAN || opCode == OP_EQUALS ) {
         long secondPosition = getPositionNumber( 2 );
         long thirdPosition = getPositionNumber( 3 );

         if ( opCode == OP_ADD ) {
            program.put( thirdPosition, getValue( firstPosition ) + getValue( secondPosition ) );
         }

         else if ( opCode == OP_MULTIPLY ) {
            program.put( thirdPosition, getValue( firstPosition ) * getValue( secondPosition ) );
         }

         else if ( opCode == OP_LESS_THAN ) {
            long lessThan = getValue( firstPosition ) < getValue( secondPosition ) ? 1 : 0;
            program.put( thirdPosition, lessThan );
         }

         else if ( opCode == OP_EQUALS ) {
            long equals = getValue( firstPosition ) == getValue( secondPosition ) ? 1 : 0;
            program.put( thirdPosition, equals );
         }

         next( 4 );
      }
      else if ( opCode == OP_WRITE || opCode == OP_READ || opCode == OP_ADJUST_RELATIVE_BASE ) {
         if ( opCode == OP_WRITE ) {
            if ( inputs.isEmpty() ) {
               return ExitCondition.INPUT_NEEDED;
            }
            program.put( firstPosition, inputs.remove( 0 ) );
         }

         else if ( opCode == OP_READ ) {
            setOutput( getValue( firstPosition ) );
         }

         else if ( opCode == OP_ADJUST_RELATIVE_BASE ) {
            adjustRelativeBase( getValue( firstPosition ) );
         }
         next( 2 );
      }
      else if ( opCode == OP_JUMP_IF_TRUE || opCode == OP_JUMP_IF_FALSE ) {
         long firstVal = getValue( firstPosition );
         long secondPosition = getPositionNumber( 2 );

         if ( (opCode == OP_JUMP_IF_TRUE && firstVal != 0) || (opCode == OP_JUMP_IF_FALSE && firstVal == 0) ) {
            jump( getValue( secondPosition ) );
         }

         else {
            next( 3 );
         }
      }
      else {
         throw new RuntimeException( "invalid opCode " + opCode + " at position " + getCurrent() );
      }
      return ExitCondition.CONTINUE;

   }

   private long getOpCode() {
      return program.get( getCurrent() ) % 10;
   }

   private long getParamMode( long paramNumber ) {
      long fullInstruction = program.get( getCurrent() );
      if ( paramNumber >= 1 && paramNumber <= 3 ) {
         return (int) ((fullInstruction / Math.pow( 10, (paramNumber + 1) )) % 10);
      }
      else {
         throw new RuntimeException( "invalid param mode " + fullInstruction + " param number " + paramNumber );
      }
   }

   private long getPositionNumber( long paramNumber ) {
      long paramMode = getParamMode( paramNumber );
      if ( paramMode == PARAM_MODE_POSITION ) {
         return getValue( getCurrent() + paramNumber );
      }
      else if ( paramMode == PARAM_MODE_IMMEDIATE ) {
         return getCurrent() + paramNumber;
      }
      else if ( paramMode == PARAM_MODE_RELATIVE ) {
         return getValue( getCurrent() + paramNumber ) + relativeBase;
      }
      else {
         throw new RuntimeException( "invalid paramMode " + paramMode );
      }
   }

   public int getNumOutputs() {
      return outputs.size();
   }

   private long getValue( long positionNumber ) {
      return program.getOrDefault( positionNumber, 0l );
   }

   private boolean shouldStop() {
      return program.get( getCurrent() ) == 99;
   }

   public Map<Long, Long> getProgram() {
      return program;
   }

   private void adjustRelativeBase( long adjustment ) {
      relativeBase += adjustment;
   }

   public void addInput( long input ){
      inputs.add( input );
   }

   public Optional<Long> getOutput(){
      if( outputs.isEmpty()){
         return Optional.empty();
      }
      return Optional.of( outputs.remove( 0 ) );
   }

   private void setOutput( long output ){
      outputs.add( 0, output );
   }
}