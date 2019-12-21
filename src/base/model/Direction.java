package base.model;

public enum Direction {

   NORTH( 0, 1 ),
   SOUTH( 0, -1 ),
   WEST( -1, 0 ),
   EAST( 1, 0 );

   int relativeX;
   int relativeY;

   Direction( int relativeX, int relativeY ) {
      this.relativeX = relativeX;
      this.relativeY = relativeY;
   }

   public int getRelativeX() {
      return relativeX;
   }

   public int getRelativeY() {
      return relativeY;
   }
}
