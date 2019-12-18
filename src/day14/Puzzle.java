package day14;

import base.AbstractPuzzle;

import java.util.*;
import java.util.stream.Collectors;

public class Puzzle extends AbstractPuzzle {

    public static final boolean IS_TEST = false;
    public static final int DAY = 14;

    public static final String FUEL = "FUEL";
    public static final String ORE = "ORE";

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
        List<String> instructions = puzzle.readFile();

        Map<String, Reaction> reactions = new HashMap<>();
        for (String r : instructions) {
            Reaction reaction = new Reaction(r);
            reactions.put(reaction.output.name, reaction);
        }

//        System.out.println(reactions);

        Reaction fuelReaction = reactions.get(FUEL);

        Map<String, Integer> onHand = new HashMap<>();
        int oreNeeded = getOreNeeded(fuelReaction.output.name, fuelReaction.output.quantity, reactions, onHand);

        long expected = puzzle.getAnswer1();
        System.out.println("Ore Needed: " + oreNeeded);
        System.out.println(expected == oreNeeded);
    }

    public static int getOreNeeded(String substanceName, int substanceQuantity, Map<String, Reaction> reactions, Map<String, Integer> onHand) {
        Reaction reaction = reactions.get(substanceName);
        int alreadyHave = onHand.getOrDefault(substanceName, 0);
        int need = substanceQuantity - alreadyHave;
        int factor = (int) Math.ceil(Math.max(need, 0) / (double) reaction.output.quantity);
        int remaining = (reaction.output.quantity * factor) - need;
        onHand.put(substanceName, remaining);

        int ore = 0;
        for (Substance s : reaction.input) {
            if (s.name.equals(ORE)) {
                ore += s.quantity * factor;
            }
            else{
                ore += getOreNeeded( s.name, s.quantity * factor, reactions, onHand);
            }
        }
        return ore;
    }

    public static void solve2() {
        System.out.println("Solving 2...");
        Puzzle puzzle = new Puzzle();
        List<String> instructions = puzzle.readFile();
        long expected = puzzle.getAnswer2();
    }

    public static class Substance {
        String name;
        int quantity;

        public Substance(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return quantity + "-" + name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Substance substance = (Substance) o;
            return quantity == substance.quantity &&
                    Objects.equals(name, substance.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, quantity);
        }
    }

    public static class Reaction {
        Substance output;
        List<Substance> input = new ArrayList<>();

        public Reaction(String description) {
            String[] inAndOut = description.split("=>");
            String[] outs = inAndOut[1].trim().split(" ");
            this.output = new Substance(outs[1].trim(), Integer.parseInt(outs[0].trim()));

            String[] ins = inAndOut[0].trim().split(",");
            for (int i = 0; i < ins.length; i++) {
                String[] in = ins[i].trim().split(" ");
                this.input.add(new Substance(in[1].trim(), Integer.parseInt(in[0].trim())));
            }
        }

        @Override
        public String toString() {
            return input.stream().map(Substance::toString).collect(Collectors.joining(",")) + "=>" + this.output;
        }
    }

}
