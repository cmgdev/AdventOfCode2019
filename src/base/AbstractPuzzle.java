package base;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPuzzle {
   public static final String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   public static final String ALPHABET_LOWER = ALPHABET_UPPER.toLowerCase();

   private boolean isTest;
   private int day;

   public AbstractPuzzle(boolean isTest, int day) {
      this.isTest = isTest;
      this.day = day;
   }

   public List<String> readFile() {
      return readFile( "#" );
   }

   public List<String> readFile( String comment ) {
      String fileName = isTest ? "example.txt" : "input.txt";
      String dayString = day < 10 ? "0" + day : Integer.toString( day );

      String inputFile = System.getProperty( "user.dir" ) + "/out/production/AdventOfCode2019/day" + dayString + "/" + fileName;

      List<String> input = new ArrayList<>();

      try {
         input = Files.readAllLines( new File( inputFile ).toPath() );
         return input.stream().filter( i -> !i.isEmpty() && !i.startsWith( comment ) )
               .collect( Collectors.toList() );
      }
      catch ( Exception e ) {
         System.out.println( "Oh shit! " + e );
      }
      return input;
   }

}