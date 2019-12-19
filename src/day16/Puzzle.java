package day16;

import base.AbstractPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = false;
    public static final int DAY = 16;

    public Puzzle() {
        super(IS_TEST, DAY);
    }

    public static void main(String... args) {
        solve1();
        solve2();
    }

    public static void solve1() {
        System.out.println("Solving 1...");

        Puzzle puzzle = new Puzzle();
        String input = puzzle.readFile().get(0);
        int numPhases = 100;

        String result = input;
        for (int i = 0; i < numPhases; i++) {
            result = runPhase(result);
        }

        String expected = puzzle.getAnswer1String();
        result = result.substring(0, 8);

        System.out.println("Actual " + result);
        System.out.println(result.equals(expected));
    }

    private static String runPhase(String input) {
        char[] chars = input.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            List<Integer> pattern = getPatternForElement(i + 1);

            long sum = 0;
            for (int j = 0; j < input.length(); j++) {
                int left = Integer.parseInt(String.valueOf(chars[j]));
                int right = pattern.get(j % pattern.size());
                sum += left * right;
            }

            sb.append(Math.abs(sum) % 10);
        }
        return sb.toString();
    }

    public static void solve2() {
        System.out.println("Solving 2...");
    }

    public static List<Integer> getPatternForElement(int element) {
        List<Integer> basePattern = Arrays.asList(0, 1, 0, -1);
        List<Integer> pattern = new ArrayList<>();

        for (int i = 0; i < basePattern.size(); i++) {
            for (int j = 0; j < element; j++) {
                pattern.add(basePattern.get(i));
            }
        }

        pattern.remove(0);
        pattern.add(pattern.size(), 0);
        return pattern;
    }
}

