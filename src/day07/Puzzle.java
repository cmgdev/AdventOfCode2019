package day07;

import base.AbstractPuzzle;
import day05.Intcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = true;
   public static final int DAY = 7;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String[] args ) {
      solve1();
//      solve2();
   }

   private static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();
      String instructions = puzzle.readFile().get( 0 );

      Set<Integer[]> permutations = getPermutationsRecursive( new Integer[]{ 0, 1, 2, 3, 4 } );

      int highestOutput = 0;
      Integer[] bestPhases = null;
      for ( Integer[] permutation : permutations ) {
         int prevOutput = 0;
         for ( int i = 0; i < 5; i++ ) {
            Intcode amp = new Intcode( instructions );
            amp.addInput( permutation[i]  );
            amp.addInput( prevOutput );
            amp.runProgram();
            prevOutput = amp.getOutput();
         }
         if ( prevOutput > highestOutput ) {
            highestOutput = prevOutput;
            bestPhases = permutation;
         }
      }
      System.out.println( "Highest " + highestOutput );
      System.out.println( "With Permutation " + Arrays.toString( bestPhases ) );
      if ( IS_TEST ) {
         System.out.println( 65210 == highestOutput );
      }
      else {
         System.out.println( 99376 == highestOutput );
      }
   }

   private static void solve2() {
   }

   // copied from https://stackoverflow.com/a/35946398
   public static Set<Integer[]> getPermutationsRecursive( Integer[] num ) {
      if ( num == null ) { return null; }

      Set<Integer[]> perms = new HashSet<>();

      //base case
      if ( num.length == 0 ) {
         perms.add( new Integer[0] );
         return perms;
      }

      //shave off first int then get sub perms on remaining ints.
      //...then insert the first into each position of each sub perm..recurse

      int first = num[0];
      Integer[] remainder = Arrays.copyOfRange( num, 1, num.length );
      Set<Integer[]> subPerms = getPermutationsRecursive( remainder );
      for ( Integer[] subPerm : subPerms ) {
         for ( int i = 0; i <= subPerm.length; ++i ) { // '<='   IMPORTANT !!!
            Integer[] newPerm = Arrays.copyOf( subPerm, subPerm.length + 1 );
            for ( int j = newPerm.length - 1; j > i; --j ) { newPerm[j] = newPerm[j - 1]; }
            newPerm[i] = first;
            perms.add( newPerm );
         }
      }

      return perms;
   }
}
