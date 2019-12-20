package day16;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = true;
    public static final int DAY = 16;

    public static final int[] basePattern = new int[]{ 0, 1, 0, -1 };

    public Puzzle() {
        super(IS_TEST, DAY);
    }

    public static void main(String... args) {
        solve1();
                solve2();
        //                        compareMethods();
    }

    public static void solve1() {
        System.out.println("Solving 1...");

        Puzzle puzzle = new Puzzle();
        String input = puzzle.readFile().get(0);
        String result = runPhases( input, 100, 1 );

        String expected = puzzle.getAnswer1String();
        result = result.substring(0, 8);

        System.out.println("Actual " + result);
        System.out.println(result.equals(expected));
    }

    private static String runPhases( String input, int numPhases, int duplicateInput ) {
        String result = input;

        for ( int i = 0; i < numPhases; i++ ) {
            result = runPhase( result.toCharArray(), duplicateInput );
        }
        return result;
    }

    private static String runPhase( char[] chars, int duplicateInput ) {

        int[] ints = new int[chars.length];
        for ( int i = 0; i < chars.length; i++ ) {
            ints[i] = Integer.parseInt( String.valueOf( chars[i] ) );
        }

        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < ints.length * duplicateInput; i++ ) {

            long sum = 0;
            for ( int j = i; j < ints.length; j++ ) {
                int left = ints[j % ints.length];
                int right = getMultiplier( j + 1, i + 1 );
                sum += left * right;
            }

            sb.append(Math.abs(sum) % 10);
            //            System.out.println( "After " + i + " " + sb );
        }
        return sb.toString();
    }

    public static void solve2() {
        System.out.println("Solving 2...");

        Puzzle puzzle = new Puzzle();
        String input = puzzle.readFile().get( 0 );

        String result = runPhases( input, 100, 2 );
        System.out.println( result );
    }

    public static int getMultiplier( int element, int step ) {
        int idx = (int) ((element) / ((double) step));
        int val = basePattern[idx % 4];
        return val;
    }

    public static List<Integer> getPatternForElement(int element) {
        List<Integer> pattern = new ArrayList<>();

        for ( int i = 0; i < basePattern.length; i++ ) {
            for (int j = 0; j < element; j++) {
                pattern.add( basePattern[i] );
            }
        }

        pattern.remove(0);
        pattern.add(pattern.size(), 0);
        return pattern;
    }

}

