package day12;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 12;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
      solve1();
      solve2();
   }

   private static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();
      List<String> inputs = puzzle.readFile();

      List<Planet> planets = inputs.stream().map( s -> new Planet( s ) ).collect( Collectors.toList() );

      int iterations = IS_TEST ? 100 : 1000;
      for ( int i = 0; i < iterations; i++ ) {
         applyGravity( planets );
         applyVelocity( planets );
      }

      planets.forEach( p -> System.out.println( p ) );

      int totalEnergy = planets.stream().mapToInt( Planet::getEnergy ).sum();
      int expected = IS_TEST ? 1940 : 12644;
      System.out.println( "Total energy = " + totalEnergy );
      System.out.println( expected == totalEnergy );
   }

   private static void solve2() {
      System.out.println( "Solving 2..." );
   }

   public static void applyGravity( List<Planet> planets ) {
      Set<List<Planet>> planetPairs = getPairs( planets );

      for ( List<Planet> pair : planetPairs ) {
         Planet a = pair.get( 0 );
         Planet b = pair.get( 1 );

         // x-axis
         if ( a.posX > b.posX ) {
            a.velX--;
            b.velX++;
         }
         if ( a.posX < b.posX ) {
            a.velX++;
            b.velX--;
         }

         // y-axis
         if ( a.posY > b.posY ) {
            a.velY--;
            b.velY++;
         }
         if ( a.posY < b.posY ) {
            a.velY++;
            b.velY--;
         }

         // z-axis
         if ( a.posZ > b.posZ ) {
            a.velZ--;
            b.velZ++;
         }
         if ( a.posZ < b.posZ ) {
            a.velZ++;
            b.velZ--;
         }
      }

   }

   public static void applyVelocity( List<Planet> planets ) {
      for ( Planet p : planets ) {
         p.posX += p.velX;
         p.posY += p.velY;
         p.posZ += p.velZ;
      }
   }

   private static Set<List<Planet>> getPairs( List<Planet> planets ) {
      Set<List<Planet>> planetPairs = new HashSet<>();
      if ( planets.isEmpty() ) {
         return planetPairs;
      }

      List<Planet> planetList = new ArrayList<>( planets );

      while ( planetList.size() > 1 ) {
         Planet first = planetList.remove( 0 );
         for ( Planet other : planetList ) {
            planetPairs.add( Arrays.asList( first, other ) );
         }
      }

      return planetPairs;
   }

   private static class Planet {

      int posX;
      int posY;
      int posZ;
      int velX = 0;
      int velY = 0;
      int velZ = 0;

      public Planet( String positions ) {
         String[] tokens = positions.split( "," );
         posX = Integer.parseInt( tokens[0].split( "=" )[1] );
         posY = Integer.parseInt( tokens[1].split( "=" )[1] );
         posZ = Integer.parseInt( tokens[2].split( "=" )[1].replaceAll( ">", "" ) );
      }

      public int getEnergy() {
         int potential = Math.abs( posX ) + Math.abs( posY ) + Math.abs( posZ );
         int kinetic = Math.abs( velX ) + Math.abs( velY ) + Math.abs( velZ );
         return potential * kinetic;
      }

      @Override
      public String toString() {
         return "pos=<x=" + posX + ",\ty=" + posY + ",\tz=" + posZ + ">,\tvel=<x=" + velX + ",\ty=" + velY + ",\tz=" + velZ + ">";
      }

      @Override
      public boolean equals( Object o ) {
         if ( this == o ) { return true; }
         if ( o == null || getClass() != o.getClass() ) { return false; }

         Planet planet = (Planet) o;

         if ( posX != planet.posX ) { return false; }
         if ( posY != planet.posY ) { return false; }
         if ( posZ != planet.posZ ) { return false; }
         if ( velX != planet.velX ) { return false; }
         if ( velY != planet.velY ) { return false; }
         return velZ == planet.velZ;
      }

      @Override
      public int hashCode() {
         int result = posX;
         result = 31 * result + posY;
         result = 31 * result + posZ;
         result = 31 * result + velX;
         result = 31 * result + velY;
         result = 31 * result + velZ;
         return result;
      }
   }
}
