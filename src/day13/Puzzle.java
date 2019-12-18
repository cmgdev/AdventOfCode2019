package day13;

import base.AbstractPuzzle;
import base.model.Intcode;

import java.util.*;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = false;
    public static final int DAY = 13;

    private static final String SEP = ",";

    private static long STAY = 0;
    private static long LEFT = -1;
    private static long RIGHT = 1;

    public enum Tile {
        EMPTY(0, " "),
        WALL(1, "|"),
        BLOCK(2, "#"),
        PADDLE(3, "_"),
        BALL(4, "O");

        int id;
        String character;

        Tile(int id, String character) {
            this.id = id;
            this.character = character;
        }

        static Tile getById(int id) {
            for (Tile t : Tile.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }

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
        String instructions = puzzle.readFile().get(0);
        long expected = puzzle.getAnswer1();

        Intcode arcade = new Intcode(instructions);
        arcade.runProgram();
        Optional<Long> output = arcade.getOutput();

        int i = 0;
        int blockTileCount = 0;
        while (output.isPresent()) {
            int res = output.get().intValue();

            if (i % 3 == 0 && Tile.BLOCK.equals(Tile.getById(res))) {
                blockTileCount++;
            }
            i++;

            output = arcade.getOutput();
        }
        System.out.println("Block tile count is " + blockTileCount);
        System.out.println(427 == expected);
    }

    public static void solve2() {
        System.out.println("Solving 2...");

        Puzzle puzzle = new Puzzle();
        String instructions = puzzle.readFile().get(0);
        long expected = puzzle.getAnswer2();

        Intcode arcade = new Intcode(instructions);
        arcade.replace(0, 2); // put in a quarter

        int finalScore = playGame(arcade);
        System.out.println( "Final score " + finalScore);
        System.out.println(finalScore == expected);
    }

    private static int playGame(Intcode arcade) {
        int score = 0, minX = 100, maxX = 0, minY = 100, maxY = 0, ballX = 0, paddleX = 0;
        String scoreIndicator = -1 + SEP + 0;

        Map<String, Tile> screen = new HashMap<>();
        Intcode.ExitCondition exitCondition = Intcode.ExitCondition.CONTINUE;
        while (exitCondition != Intcode.ExitCondition.OK) {
            exitCondition = arcade.runProgram();
            List<Integer> result = new ArrayList<>();
            Optional<Long> output = arcade.getOutput();
            while (output.isPresent()) {
                result.add(output.get().intValue());
                output = arcade.getOutput();
            }

            Collections.reverse(result);
            for (int i = 0; i < result.size() - 2; i += 3) {
                int x = result.get(i);
                int y = result.get(i + 1);
                String xy = x + SEP + y;

                if (scoreIndicator.equals(xy)) {
                    score = result.get(i + 2);
                } else {
                    Tile t = Tile.getById(result.get(i + 2));
                    screen.put(xy, t);

                    if (Tile.BALL == t) {
                        ballX = x;
                    }
                    if (Tile.PADDLE == t) {
                        paddleX = x;
                    }

                    minX = Math.min(x, minX);
                    maxX = Math.max(x, maxX);
                    minY = Math.min(y, minY);
                    maxY = Math.max(y, maxY);
                }
            }

            System.out.println();
            System.out.println("SCORE " + score);

            for (int y = 0; y <= maxY; y++) {
                for (int x = 0; x <= maxX; x++) {
                    System.out.print(screen.getOrDefault(x + SEP + y, Tile.EMPTY).character);
                }
                System.out.println();
            }


            arcade.addInput(ballX - paddleX);
        }
        return score;
    }

}
