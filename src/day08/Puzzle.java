package day08;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

      List<Map<Integer, Integer>> layers = buildLayerCounts( input, layerSize );
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
      Puzzle puzzle = new Puzzle();
      String input = IS_TEST ? "0222112222120000" : puzzle.readFile().get( 0 );
      int height = IS_TEST ? 2 : 6;
      int width = IS_TEST ? 2 : 25;

      int layerSize = height * width;

      List<Integer> image = buildImage( input, layerSize );
      List<Integer> expected = IS_TEST ?
            Arrays.asList( 0, 1, 1, 0 ) :
            Arrays.asList( 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0,
                  1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1,
                  1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1,
                  0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0,
                  1, 0, 0, 1, 0 );

      printImage( image, height );
      System.out.println( image.equals( expected ) );

   }

   public static List<Integer> buildImage( String input, int layerSize ) {
      List<Integer> image = new ArrayList<>();

      int numLayers = input.length() / layerSize;

      int firstLayerStartIndex = 0;
      int firstLayerEndIndex = firstLayerStartIndex + layerSize;

      for ( int firstLayerPixelIndex = firstLayerStartIndex; firstLayerPixelIndex < firstLayerEndIndex; firstLayerPixelIndex++ ) {
         int pixelToAdd = getPixel( firstLayerPixelIndex, input );
         for ( int layer = 0; layer < numLayers; layer++ ) {
            int thisPixel = getPixel( firstLayerPixelIndex + (layer * layerSize), input );
            if ( isTransparent( pixelToAdd ) && !isTransparent( thisPixel ) ) {
               pixelToAdd = thisPixel;
            }
         }
         image.add( pixelToAdd );
      }
      return image;
   }

   private static void printImage( List<Integer> image, int height ) {
      int width = image.size() / height;
      int startIndex = 0;
      int endIndex = width;

      for ( int h = 0; h < height; h++ ) {
         for ( int w = startIndex; w < endIndex; w++ ) {
            System.out.print( image.get( w ) == 1 ? 1 : " " );
         }
         startIndex = endIndex;
         endIndex += width;
         System.out.println();
      }
   }

   private static boolean isTransparent( int pixel ) {
      return pixel == 2;
   }

   private static int getPixel( int index, String image ) {
      return Integer.parseInt( String.valueOf( image.charAt( index ) ) );
   }

   public static List<Map<Integer, Integer>> buildLayerCounts( String input, int layerSize ) {
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
