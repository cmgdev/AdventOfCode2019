package base.model;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class Point {

   int x;
   int y;
   Point parent;
   List<Point> children = new ArrayList<>();

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

   public void setParent( Point parent ) {
      this.parent = parent;
   }

   public Point getParent() {
      return parent;
   }

   public List<Point> getChildren() {
      return children;
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
            T t = pointMap.get( current );
            sb.append( toStringFunction.apply( current, t ) );
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

   public static <T> void breadthFirstSearch( Map<Point, T> points, Point startingPoint,
         BiFunction<Point, T, Boolean> isParentFunction ) {

      List<Point> seen = new ArrayList<>();
      List<Point> queue = new ArrayList<>();
      queue.add( startingPoint );

      while ( !queue.isEmpty() ) {
         Point next = queue.remove( queue.size() - 1 );
         for ( Point p : next.getAdjacentPoints() ) {
            Point finalP = p;
            Optional<Point> fromMap = points.keySet().stream().filter( k -> k.equals( finalP ) ).findFirst();
            if ( fromMap.isPresent() ) {
               p = fromMap.get();
               next.children.add( p );
               T t = points.get( p );
               if ( isParentFunction.apply( p, t ) && !seen.contains( p ) ) {
                  p.setParent( next );
                  queue.add( p );
                  seen.add( next );
               }
            }
         }
      }
   }

}
