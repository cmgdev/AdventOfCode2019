package day13;

import base.AbstractPuzzle;
import day05.Intcode;

import java.util.Optional;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = false;
    public static final int DAY = 13;

    public static final int EMPTY_TILE = 0;
    public static final int WALL_TILE = 1;
    public static final int BLOCK_TILE = 2;
    public static final int PADDLE_TILE = 3;
    public static final int BALL_TILE = 4;

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

        Intcode arcade = new Intcode(instructions);
        arcade.runProgram();
        Optional<Long> output = arcade.getOutput();

        int i = 0;
        int blockTileCount = 0;
        while( output.isPresent() ){
            int res = output.get().intValue();

            if( i % 3 == 0 && res == BLOCK_TILE ){
                blockTileCount++;
            }
            i++;

            output = arcade.getOutput();
        }
        System.out.println("Block tile count is " + blockTileCount);
        System.out.println(427 == blockTileCount);
    }

    public static void solve2() {
        System.out.println("Solving 2...");
    }

}
