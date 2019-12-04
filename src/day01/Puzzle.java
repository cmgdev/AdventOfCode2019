package day01;

import base.AbstractPuzzle;

import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 1;

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
      //      int EXPECTED_TEST_VALUE = 34241;
      int EXPECTED_TEST_VALUE = 33583;
      int EXPECTED_VALUE = 3457681;

      System.out.println( "Solving 1..." );

      int sum = input.stream().mapToInt( Integer::valueOf ).map( i -> fuelRequired( i ) ).sum();

      System.out.println( "Sum is " + sum );
      if ( IS_TEST ) {
         System.out.println( sum == EXPECTED_TEST_VALUE );
      }
      else {
         System.out.println( sum == EXPECTED_VALUE );
      }
   }

   public static void solve2( List<String> input ) {
      int EXPECTED_TEST_VALUE = 50346;
      int EXPECTED_VALUE = 3457681;

      System.out.println( "Solving 2..." );

      long sum = input.stream().mapToInt( Integer::valueOf ).mapToLong( i -> allFuelRequired( i ) ).sum();

      System.out.println( "Sum is " + sum );
      if ( IS_TEST ) {
         System.out.println( sum == EXPECTED_TEST_VALUE );
      }
      else {
         System.out.println( sum == EXPECTED_VALUE );
      }
   }

   public static int fuelRequired( int weight ) {
      return Math.max( (weight / 3) - 2, 0 );
   }

   public static long allFuelRequired( int weight ) {
      long sum = 0;

      int fuelRequired = fuelRequired( weight );
      while ( fuelRequired > 0 ) {
         sum += fuelRequired;
         fuelRequired = fuelRequired( fuelRequired );
      }

      return sum;
   }

}
