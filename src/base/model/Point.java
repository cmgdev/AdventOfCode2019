package base.model;

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
   public String toString() {
      return "Point{" + "x=" + x + ", y=" + y + '}';
   }
}
