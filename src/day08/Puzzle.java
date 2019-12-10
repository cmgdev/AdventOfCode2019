package day08;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 8;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String[] args ) {
      solve1();
      solve2();
   }

   private static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();
      String input = puzzle.readFile().get( 0 );

      int layerSize = IS_TEST ? 6 : 150;

      List<Map<Integer, Integer>> layers = buildLayers( input, layerSize );
      Map<Integer, Integer> layerWithFewestZeroes = layers.get( 0 );
      for ( Map<Integer, Integer> layer : layers ) {
         if ( layer.getOrDefault( 0, 0 ) < layerWithFewestZeroes.getOrDefault( 0, 0 ) ) {
            layerWithFewestZeroes = layer;
         }
      }

      int expected = IS_TEST ? 1 : 2480;
      int actual = layerWithFewestZeroes.get( 1 ) * layerWithFewestZeroes.get( 2 );
      System.out.println( layerWithFewestZeroes );
      System.out.println( actual );
      System.out.println( actual == expected );
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
   }

   public static List<Map<Integer, Integer>> buildLayers( String input, int layerSize ) {
      List<Map<Integer, Integer>> layers = new ArrayList<>();
      int start = 0;
      int end = start + layerSize;

      while ( end <= input.length() ) {
         Map<Integer, Integer> layer = new HashMap<>();
         char[] chars = input.substring( start, end ).toCharArray();
         for ( char c : chars ) {
            int charAsInt = Integer.parseInt( String.valueOf( c ) );
            int count = layer.getOrDefault( charAsInt, 0 ) + 1;
            layer.put( charAsInt, count );
         }
         layers.add( layer );
         start = end;
         end += layerSize;
      }
      return layers;
   }
}
