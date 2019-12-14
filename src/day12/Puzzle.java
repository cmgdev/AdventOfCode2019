package day12;

import base.AbstractPuzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 12;

   public static final List<String> planetNames = Arrays.asList( "Io", "Europa", "Ganymede", "Callisto" );

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String... args ) {
            solve1();
   }

   private static void solve1() {
      System.out.println( "Solving 1..." );
      Puzzle puzzle = new Puzzle();
      List<String> inputs = puzzle.readFile();

      List<Planet> planets = getPlanets( inputs );
      Set<List<Planet>> planetPairs = getPairs( planets );

      int iterations = IS_TEST ? 100 : 1000;
      for ( int i = 0; i < iterations; i++ ) {
         applyGravity( planetPairs );
         applyVelocity( planets );
      }

      planets.forEach( p -> System.out.println( p ) );

      long totalEnergy = planets.stream().mapToLong( Planet::getEnergy ).sum();
      long expected = IS_TEST ? 1940 : 12644;
      System.out.println( "Total energy = " + totalEnergy );
      System.out.println( expected == totalEnergy );
   }

   public static void applyGravity( Set<List<Planet>> planetPairs ) {
      for ( List<Planet> pair : planetPairs ) {
         Planet a = pair.get( 0 );
         Planet b = pair.get( 1 );

         // x-axis
         if ( a.posX > b.posX ) {
            a.velX--;
            b.velX++;
         }
         else if ( a.posX < b.posX ) {
            a.velX++;
            b.velX--;
         }

         // y-axis
         if ( a.posY > b.posY ) {
            a.velY--;
            b.velY++;
         }
         else if ( a.posY < b.posY ) {
            a.velY++;
            b.velY--;
         }

         // z-axis
         if ( a.posZ > b.posZ ) {
            a.velZ--;
            b.velZ++;
         }
         else if ( a.posZ < b.posZ ) {
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

   private static List<Planet> getPlanets( List<String> inputs ) {
      List<Planet> planets = inputs.stream().map( s -> new Planet( s ) ).collect( Collectors.toList() );

      for ( int i = 0; i < planets.size(); i++ ) {
         Planet p = planets.get( i );
         p.name = planetNames.get( i );
      }

      return planets;
   }

   private static Set<List<Planet>> getPairs( List<Planet> planets ) {
      Planet a = planets.get( 0 );
      Planet b = planets.get( 1 );
      Planet c = planets.get( 2 );
      Planet d = planets.get( 3 );

      Set<List<Planet>> planetPairs = new HashSet<>();
      planetPairs.add( Arrays.asList( a, b ) );
      planetPairs.add( Arrays.asList( a, c ) );
      planetPairs.add( Arrays.asList( a, d ) );
      planetPairs.add( Arrays.asList( b, c ) );
      planetPairs.add( Arrays.asList( b, d ) );
      planetPairs.add( Arrays.asList( c, d ) );

      return planetPairs;
   }

   private static class Planet implements Comparable<Planet> {

      String name;
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

      public long getEnergy() {
         return getPotentialEnergy() * getKineticEnergy();
      }

      public long getKineticEnergy() {
         return Math.abs( velX ) + Math.abs( velY ) + Math.abs( velZ );
      }

      public long getPotentialEnergy() {
         return Math.abs( posX ) + Math.abs( posY ) + Math.abs( posZ );
      }

      @Override
      public String toString() {
         return name + "=pos=<x=" + posX + ",\ty=" + posY + ",\tz=" + posZ + ">,\tvel=<x=" + velX + ",\ty=" + velY + ",\tz="
               + velZ + ">";
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

      @Override
      public int compareTo( Planet o ) {
         return this.name.compareTo( o.name );
      }
   }
}
