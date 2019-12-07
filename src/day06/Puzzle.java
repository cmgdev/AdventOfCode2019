package day06;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

   public static final boolean IS_TEST = false;
   public static final int DAY = 6;

   public Puzzle() {
      super( IS_TEST, DAY );
   }

   public static void main( String[] args ) {
      solve1();
      solve2();
   }

   public static void solve1() {
      System.out.println( "Solving 1..." );

      Puzzle puzzle = new Puzzle();
      List<String> input = puzzle.readFile();

      Map<String, Orbiter> orbits = getOrbits( input );
      int orbitCount = getTotalOrbitCount( orbits );
      System.out.println( "Total orbits " + orbitCount );
      if ( IS_TEST ) {
         System.out.println( orbits );
         System.out.println( 42 == orbitCount );
      }
      else {
         System.out.println( 204521 == orbitCount );
      }
   }

   public static void solve2() {
      System.out.println( "Solving 2..." );

      Puzzle puzzle = new Puzzle();
      List<String> input = puzzle.readFile();

   }

   public static Map<String, Orbiter> getOrbits( List<String> input ) {
      Map<String, Orbiter> orbits = new HashMap<>();

      for ( String s : input ) {
         String[] bodies = s.split( "\\)" );
         String centerId = bodies[0];
         String orbiterId = bodies[1];

         Orbiter orbiter = orbits.getOrDefault( orbiterId, new Orbiter( orbiterId ) );
         orbiter.setCenterId( centerId );

         Orbiter center = orbits.getOrDefault( centerId, new Orbiter( centerId ) );
         center.addOrbiterId( orbiterId );

         orbits.put( orbiterId, orbiter );
         orbits.put( centerId, center );
      }

      return orbits;
   }

   public static int getTotalOrbitCount( Map<String, Orbiter> orbits ) {
      int count = 0;

      for ( Map.Entry<String, Orbiter> bodyWithOrbiters : orbits.entrySet() ) {
         count += getOrbitCount( bodyWithOrbiters.getKey(), orbits );
      }

      return count;
   }

   private static int getOrbitCount( String orbiterId, Map<String, Orbiter> orbits ) {
      Orbiter thisOrbiter = orbits.get( orbiterId );
      Orbiter center = orbits.get( thisOrbiter.centerId );
      if ( center != null ) {
         return 1 + getOrbitCount( center.id, orbits );
      }

      return 0;
   }

   public static class Orbiter {

      private String centerId;
      private String id;
      private List<String> orbiterIds = new ArrayList<>();

      public Orbiter( String id ) {
         this.id = id;
      }

      public void setCenterId( String centerId ) {
         this.centerId = centerId;
      }

      public void addOrbiterId( String id ) {
         orbiterIds.add( id );
      }

      @Override
      public String toString() {
         return "[" + orbiterIds.stream().map( String::toString ).collect( Collectors.joining( "," ) ) + "]";
      }

      @Override
      public boolean equals( Object obj ) {
         return this.id.equals( ((Orbiter) obj).id );
      }

      @Override
      public int hashCode() {
         return id.hashCode();
      }
   }
}
