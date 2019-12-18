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

        int oreUsed = 0;
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

    public static Optional<Reaction> getRunnableReaction(Map<String, Integer> onHand, Collection<Reaction> reactions) {
        return reactions.stream().sorted((a, b) -> a.timesRun > b.timesRun ? 1 : -1).filter(r -> enoughOnHand(onHand, r.input)).findFirst();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean enoughOnHand(Map<String, Integer> onHand, Collection<Substance> needs) {
        for (Substance need : needs) {
            if (!enoughOnHand(onHand, need)) {
                return false;
            }
        }
        return true;
    }

    public static boolean enoughOnHand(Map<String, Integer> onHand, Substance need) {
        for (Map.Entry<String, Integer> s : onHand.entrySet()) {
            if (s.getKey().equals(need.name) && s.getValue() >= need.quantity) {
                return true;
            }
        }
        return false;
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
        int timesRun = 0;

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

    //       Map<String, Integer> needs = new HashMap<>();
    //        needs.put(fuelReaction.output.name, fuelReaction.output.quantity);
    //
    //        Map<String, Integer> onHand = new HashMap<>();
    //
    //        while (!onHand.containsKey(fuelReaction.output.name)) {
    //            System.out.println("On hand now " + onHand);
    //            Optional<Reaction> runnableReaction = getRunnableReaction(onHand, reactions.values());
    //            if (runnableReaction.isPresent()) {
    //                Reaction r = runnableReaction.get();
    //                System.out.println("Can run reaction " + r);
    //                for( Substance in : r.input){
    //                    int existingQuantity = onHand.get(in.name);
    //                    onHand.put(in.name, existingQuantity - in.quantity);
    //                    if( in.name.equals("ORE") ){
    //                        oreUsed += in.quantity;
    //                        System.out.println("Ore used " + oreUsed);
    //                    }
    //                }
    //                onHand.put(r.output.name, r.output.quantity);
    //                r.timesRun++;
    //            }
    //
    //            int oreNeeded = needs.getOrDefault("ORE", 0);
    //            System.out.println("Ore Needed this cycle " + oreNeeded);
    //            if (oreNeeded > 0) {
    //                onHand.put("ORE", oreNeeded);
    //            }
    //
    //
    //            Map<String, Integer> needsToAdd = new HashMap<>();
    //            for (String need : needs.keySet()) {
    //                Reaction match = reactions.get(need);
    //                if (match != null) {
    //                    for (Substance input : match.input) {
    //                        int inMap = needsToAdd.getOrDefault(input.name, 0);
    //                        needsToAdd.put(input.name, inMap + input.quantity);
    //                    }
    //                }
    //            }
    //
    //            for (String need : needsToAdd.keySet()) {
    //                if (!onHand.containsKey(need) && !needs.containsKey(need)) {
    //                    needs.put(need, needsToAdd.get(need));
    //                }
    //            }
    //            System.out.println("Needs: " + needs);
    //
    //            sleep();
    //
    //        }
}
