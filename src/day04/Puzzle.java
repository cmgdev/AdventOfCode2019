package day04;

public class Puzzle {

   public static void main( String[] args ) {
      verifyNothingBroke();

      solve1();
      solve2();
   }

   public static void solve1() {
      System.out.println( "Solving 1..." );
      //108457-562041
      int low = 108457;
      int high = 562041;
      int count = 0;
      for ( int i = low; i < high; i++ ) {
         String s = Integer.toString( i );
         if ( isSixDigitNumber( s ) && atLeastTwoAdjacentDigitsSame( s ) && digitsNeverDecrease( s ) ) {
            count++;
         }
      }
      System.out.println( "Count is " + count );
      System.out.println( 2779 == count );
   }

   public static void solve2() {
      System.out.println( "Solving 2..." );
      //108457-562041
      int low = 108457;
      int high = 562041;
      int count = 0;

      for ( int i = low; i < high; i++ ) {
         String s = Integer.toString( i );
         if ( isSixDigitNumber( s ) && atLeastTwoAdjacentDigitsSame( s ) && digitsNeverDecrease( s ) && twoAdjacentDigitsSame( s ) ) {
            count++;
         }
      }
      System.out.println( "Count is " + count );
      System.out.println( 1972 == count );
   }

   public static boolean isSixDigitNumber( String input ) {
      return input.matches( "\\d{6}" );
   }

   public static boolean atLeastTwoAdjacentDigitsSame( String input ) {
      for ( int i = 0; i < input.length() - 1; i++ ) {
         if ( input.charAt( i ) == input.charAt( i + 1 ) ) {
            return true;
         }
      }
      return false;
   }

   public static boolean digitsNeverDecrease( String input ) {
      for ( int i = 0; i < input.length() - 1; i++ ) {
         if ( Integer.parseInt( input.substring( i, i + 1 ) ) > Integer.parseInt( input.substring( i + 1, i + 2 ) ) ) {
            return false;
         }
      }
      return true;
   }

   public static boolean twoAdjacentDigitsSame( String input ) {

      for ( int i = 1; i < input.length(); i++ ) {
         int currentAdjacentCount = 1;
         char previousChar = input.charAt( i - 1 );
         char currentChar = input.charAt( i );

         while ( currentChar == previousChar ) {
            currentAdjacentCount++;
            i++;
            if( i == input.length() ){
               break;
            }
            else {
               previousChar = input.charAt( i - 1 );
               currentChar = input.charAt( i );
            }
         }
         if ( currentAdjacentCount == 2 ) {
            return true;
         }
      }

      return false;
   }

   public static void verifyNothingBroke() {
      System.out.print( "Is everything still working? " );
      System.out.println(
            isSixDigitNumber( "123456" ) == true
            && isSixDigitNumber( "000000" ) == true
            && isSixDigitNumber( "123" ) == false
            && atLeastTwoAdjacentDigitsSame( "111111" ) == true
            && atLeastTwoAdjacentDigitsSame( "122345" ) == true
            && atLeastTwoAdjacentDigitsSame( "111123" ) == true
            && atLeastTwoAdjacentDigitsSame( "135679" ) == false
            && atLeastTwoAdjacentDigitsSame( "123789" ) == false
            && digitsNeverDecrease( "111111" ) == true
            && digitsNeverDecrease( "223450" ) == false
            && twoAdjacentDigitsSame( "112233" ) == true
            && twoAdjacentDigitsSame( "123444" ) == false
            && twoAdjacentDigitsSame( "111122" ) == true
            && twoAdjacentDigitsSame( "111111" ) == false
      );

   }
}
