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
         //System.out.println( 42 == orbitCount );
         System.out.println( 54 == orbitCount ); // 42 + YOU + SAN
      }
      else {
         System.out.println( 204521 == orbitCount );
      }
   }

   public static void solve2() {
      System.out.println( "Solving 2..." );

      Puzzle puzzle = new Puzzle();
      List<String> input = puzzle.readFile();

      Map<String, Orbiter> orbits = getOrbits( input );

      int distance = getDistanceFromYOUToSAN( orbits );

      System.out.println( "Distance is " + distance );
      if ( IS_TEST ) {
         System.out.println( 4 == distance );
      }
      else {
         System.out.println( 307 == distance );
      }
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

   private static List<String> getPathToCenter( String startOrbiterId, Map<String, Orbiter> orbits ) {
      List<String> path = new ArrayList<>();
      path.add( startOrbiterId );

      Orbiter thisOrbiter = orbits.get( startOrbiterId );
      Orbiter center = orbits.get( thisOrbiter.centerId );

      while ( center != null ) {
         path.add( center.id );
         center = orbits.get( center.centerId );
      }
      return path;
   }

   private static int getDistanceFromYOUToSAN( Map<String, Orbiter> orbits ) {
      List<String> pathFromYOUToCOM = getPathToCenter( "YOU", orbits );
      //      System.out.println( pathFromYOUToCOM );
      List<String> pathFromSANToCOM = getPathToCenter( "SAN", orbits );
      //      System.out.println( pathFromSANToCOM );

      String firstCommon = getFirstCommonBody( pathFromYOUToCOM, pathFromSANToCOM );
      return pathFromYOUToCOM.indexOf( firstCommon ) + pathFromSANToCOM.indexOf( firstCommon ) - 2;
   }

   private static String getFirstCommonBody( List<String> pathFromAToCOM, List<String> pathFromBToCOM ) {
      for ( String id : pathFromAToCOM ) {
         if ( pathFromBToCOM.contains( id ) ) {
            return id;
         }
      }
      return null;
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
