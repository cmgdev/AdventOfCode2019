package base.model;

import day17.Puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Point {

   int x;
   int y;

   public Point( int x, int y ) {
      this.x = x;
      this.y = y;
   }

   public void setX( int x ) {
      this.x = x;
   }

   public void setY( int y ) {
      this.y = y;
   }

   public int getY() {
      return y;
   }

   public int getX() {
      return x;
   }

   @Override
   public boolean equals( Object o ) {
      if ( this == o ) { return true; }
      if ( o == null || getClass() != o.getClass() ) { return false; }

      Point point = (Point) o;

      if ( x != point.x ) { return false; }
      return y == point.y;
   }

   @Override
   public int hashCode() {
      int result = x;
      result = 31 * result + y;
      return result;
   }

   @Override
   public String toString() {
      return "Point{" + "x=" + x + ", y=" + y + '}';
   }

   public static <T> String pointMapToString( Map<Point, T> pointMap, BiFunction<Point, T, String> toStringFunction ) {
      int minX = 0, maxX = 0, minY = 0, maxY = 0;
      for ( Point p : pointMap.keySet() ) {
         minX = Math.min( minX, p.getX() );
         maxX = Math.max( maxX, p.getX() );
         minY = Math.min( minY, p.getY() );
         maxY = Math.max( maxY, p.getY() );
      }

      StringBuilder sb = new StringBuilder();
      for ( int y = maxY; y >= minY; y-- ) {
         for ( int x = minX; x <= maxX; x++ ) {
            Point current = new Point( x, y );
            T o = pointMap.get( current );
            sb.append( toStringFunction.apply( current, o ) );
         }

         sb.append( "\n" );
      }
      return sb.toString();
   }

   public List<Point> getAdjacentPoints() {
      List<Point> adjacentPoints = new ArrayList<>();
      for ( Direction direction : Direction.values() ) {
         adjacentPoints.add( getAdjacentPoint( direction ) );
      }
      return adjacentPoints;
   }

   public Point getAdjacentPoint( Direction direction ) {
      return new Point( this.getX() + direction.getRelativeX(), this.getY() + direction.getRelativeY() );
   }

}
