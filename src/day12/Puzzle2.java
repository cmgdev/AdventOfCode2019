package day12;

import base.AbstractPuzzle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle2 extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 12;

   public static final String[] planetNames = new String[]{ "Io", "Europa", "Ganymede", "Callisto" };

   private static final int POS_X = 0;
   private static final int POS_Y = 1;
   private static final int POS_Z = 2;
   private static final int VEL_X = 3;
   private static final int VEL_Y = 4;
   private static final int VEL_Z = 5;

   private static final int[][] planetPairs = new int[][]{ new int[]{ 0, 1 }, new int[]{ 0, 2 }, new int[]{ 0, 3 },
         new int[]{ 1, 2 }, new int[]{ 1, 3 }, new int[]{ 2, 3 }, };

   public Puzzle2() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      solve2();
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
      Puzzle2 puzzle = new Puzzle2();
      List<String> inputs = puzzle.readFile();

      long[][] planets = getPlanets( inputs, 0 );
      long[] iterationsToGetBackToZero = new long[3];
      int X_ITR = 0;
      int Y_ITR = 1;
      int Z_ITR = 2;

      long start = System.currentTimeMillis();

      for ( int i = 0; i < iterationsToGetBackToZero.length; i++ ) {
         long count = 0;
         boolean backAtVelZero = false;
         while ( !backAtVelZero ) {
            int velIdx = i + 3;
            applyGravity( planets, i, velIdx );
            applyVelocity( planets );

            applyGravity( planets, i, velIdx );
            applyVelocity( planets );

            count += 2;
            backAtVelZero = areAllZeroVelocity( planets, velIdx );
         }
         iterationsToGetBackToZero[i] = count;
      }

      // final 344,491,356,000 too low
      //       145,157,310,783,264 is the Least Common Multiple of [42016, 143166, 96526]
      //       290314621566528 is 2 * that
      //      count *= 2;
      long expected = IS_TEST ? 4686774924L : 0;
      //      System.out.println( "Count = " + count );
      System.out.println( "Duration = " + (System.currentTimeMillis() - start) / 1000 );
      //      System.out.println( count == expected );
      System.out.println( Arrays.toString( iterationsToGetBackToZero ) );
   }

   private static void solve2a() {
      System.out.println( "Solving 2..." );
      Puzzle2 puzzle = new Puzzle2();
      List<String> inputs = puzzle.readFile();

      long lastStop = IS_TEST ? 0 : 74000000000L;
      long[][] planets = getPlanets( inputs, lastStop );

      long count = lastStop;
      long start = System.currentTimeMillis();
      long totalEnergy = 1;

      while ( totalEnergy > 0 ) {
         applyGravity( planets );
         applyVelocity( planets );

         applyGravity( planets );
         applyVelocity( planets );

         if ( areAllZeroVelocity( planets ) ) {
            totalEnergy = getEnergy( planets );
         }
         count += 2;

         if ( count % 100000000 == 0 ) {
            System.out.println( new DecimalFormat().format( count ) + " : " + (System.currentTimeMillis() - start) / 1000 );
         }
         if ( count % 1000000000 == 0 ) {
            System.out.println( "Planets at " + count );
            printPlanets( planets );
         }
      }

      // final 344,491,356,000 too low
      count *= 2;
      long expected = IS_TEST ? 4686774924L : 0;
      System.out.println( "Count = " + count );
      System.out.println( "Duration = " + (System.currentTimeMillis() - start) / 1000 );
      System.out.println( count == expected );

      //Best test
      //Count = 4686774924
      //Duration = 142
   }

   public static void applyGravity( long[][] planets ) {
      for ( int[] pair : planetPairs ) {
         long[] a = planets[pair[0]];
         long[] b = planets[pair[1]];

         // x-axis
         if ( a[POS_X] != b[POS_X] ) {
            int d = a[POS_X] > b[POS_X] ? 1 : -1;
            a[VEL_X] -= d;
            b[VEL_X] += d;
         }

         // y-axis
         if ( a[POS_Y] != b[POS_Y] ) {
            int d = a[POS_Y] > b[POS_Y] ? 1 : -1;
            a[VEL_Y] -= d;
            b[VEL_Y] += d;
         }

         // z-axis
         if ( a[POS_Z] != b[POS_Z] ) {
            int d = a[POS_Z] > b[POS_Z] ? 1 : -1;
            a[VEL_Z] -= d;
            b[VEL_Z] += d;
         }
      }
   }

   public static void applyGravity( long[][] planets, int posIdx, int velIdx ) {
      for ( int[] pair : planetPairs ) {
         long[] a = planets[pair[0]];
         long[] b = planets[pair[1]];

         if ( a[posIdx] != b[posIdx] ) {
            int d = a[posIdx] > b[posIdx] ? 1 : -1;
            a[velIdx] -= d;
            b[velIdx] += d;
         }
      }
   }

   public static void applyVelocity( long[][] planets ) {
      for ( long[] p : planets ) {
         p[POS_X] += p[VEL_X];
         p[POS_Y] += p[VEL_Y];
         p[POS_Z] += p[VEL_Z];
      }
   }

   public static void applyVelocity( long[][] planets, int posIdx, int velIdx ) {
      for ( long[] p : planets ) {
         p[posIdx] += p[velIdx];
      }
   }

   private static long[][] getPlanets( List<String> inputs, long lastStop ) {
      long[][] planets = new long[4][6];

      for ( int i = 0; i < planets.length; i++ ) {
         String[] tokens = inputs.get( i ).split( "," );
         planets[i][POS_X] = Integer.parseInt( tokens[0].split( "=" )[1] );
         planets[i][POS_Y] = Integer.parseInt( tokens[1].split( "=" )[1] );
         planets[i][POS_Z] = Integer.parseInt( tokens[2].split( "=" )[1].replaceAll( ">", "" ) );

         if ( lastStop > 0 ) {
            planets[i][VEL_X] = Integer.parseInt( tokens[3].split( "=" )[1] );
            planets[i][VEL_Y] = Integer.parseInt( tokens[4].split( "=" )[1] );
            planets[i][VEL_Z] = Integer.parseInt( tokens[5].split( "=" )[1].replaceAll( ">", "" ) );
         }
         else {
            planets[i][VEL_X] = 0;
            planets[i][VEL_Y] = 0;
            planets[i][VEL_Z] = 0;
         }
      }

      return planets;
   }

   private static boolean areAllZeroVelocity( long[][] planets ) {
      for ( long[] planet : planets ) {
         if ( planet[VEL_X] != 0 || planet[VEL_Y] != 0 || planet[VEL_Z] != 0 ) {
            return false;
         }
      }
      return true;
   }

   private static boolean areAllZeroVelocity( long[][] planets, int velIdx ) {
      for ( long[] planet : planets ) {
         if ( planet[velIdx] != 0 ) {
            return false;
         }
      }
      return true;
   }

   private static long getEnergy( long[][] planets ) {
      long energy = 0;
      for ( long[] planet : planets ) {
         energy +=
               (Math.abs( planet[VEL_X] ) + Math.abs( planet[VEL_Y] ) + Math.abs( planet[VEL_Z] )) * (Math.abs( planet[POS_X] )
                     + Math.abs( planet[POS_Y] ) + Math.abs( planet[POS_Z] ));
      }
      return energy;
   }

   private static void printPlanets( long[][] planets ) {
      for ( long[] planet : planets ) {
         System.out.println(
               "<x=" + planet[POS_X] + ",y=" + planet[POS_Y] + ",z=" + planet[POS_Z] + ">,<x=" + planet[VEL_X] + ",y="
                     + planet[VEL_Y] + ",z=" + planet[VEL_Z] + ">" );
      }
   }

}
