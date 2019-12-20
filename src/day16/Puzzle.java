package day16;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 16;

   public static final int[] basePattern = new int[]{ 0, 1, 0, -1 };

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      solve1();
      solve2();
   }

   public static void solve1() {
      System.out.println( "Solving 1..." );

      Puzzle puzzle = new Puzzle();
      String input = puzzle.readFile().get( 0 );
      //        System.out.println( input );

      String result = runPhases( input, 100 );
      //        System.out.println( result );

      String expected = puzzle.getAnswer1String();
      result = result.substring( 0, 8 );

      System.out.println( "Actual " + result );
      System.out.println( result.equals( expected ) );
   }

   private static String runPhases( String input, int numPhases ) {
      String result = input;

      for ( int i = 0; i < numPhases; i++ ) {
         result = runPhase( result.toCharArray() );
      }
      return result;
   }

   private static String runPhase( char[] chars ) {

      int[] ints = new int[chars.length];
      for ( int i = 0; i < chars.length; i++ ) {
         ints[i] = chars[i] - ASCII_OFFSET;
      }

      StringBuilder sb = new StringBuilder();
      for ( int i = 0; i < ints.length; i++ ) {

         long sum = 0;
         for ( int j = i; j < ints.length; j++ ) {
            int left = ints[j % ints.length];
            int right = getMultiplier( j + 1, i + 1 );
            sum += left * right;
         }

         sb.append( Math.abs( sum ) % 10 );
         //            System.out.println( "After " + i + " " + sb );
      }
      return sb.toString();
   }

   public static void solve2() {
      System.out.println( "Solving 2..." );

      Puzzle puzzle = new Puzzle();
      String input = puzzle.readFile().get( 0 );
      int offset = Integer.parseInt( input.substring( 0, 7 ) );
      //        System.out.println( offset );
      input = dupeInput( input, 10000 );

      for ( int i = 0; i < 100; i++ ) {
         input = solve2( input );
      }
      //        System.out.println( input );
      String actual = input.substring( offset, offset + 8 );
      System.out.println( actual );
      System.out.println( actual.equals( puzzle.getAnswer2String() ) );

   }

   private static String solve2( String input ) {
      StringBuffer b = new StringBuffer();

      int[] ints = new int[input.length()];
      for ( int i = 0; i < ints.length; i++ ) {
         ints[i] = input.charAt( i ) - ASCII_OFFSET;
      }

      int sum = 0;
      for ( int i = 0; i < ints.length; i++ ) {
         sum += ints[ints.length - i - 1];
         b.append( sum % 10 );
      }
      return b.reverse().toString();
   }

   public static String dupeInput( String input, int dupes ) {
      StringBuilder sb = new StringBuilder();
      for ( int i = 0; i < dupes; i++ ) {
         sb.append( input );
      }
      return sb.toString();
   }

   public static int getMultiplier( int element, int step ) {
      int idx = (int) ((element) / ((double) step));
      return basePattern[idx % 4];
   }

}

