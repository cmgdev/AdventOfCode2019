package base;

import javax.swing.text.html.Option;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public abstract class AbstractPuzzle {
    public static final String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET_LOWER = ALPHABET_UPPER.toLowerCase();
    public static final int ASCII_OFFSET = 48;

    private boolean isTest;
    private int day;
    private String answer1;
    private String answer2;

    public AbstractPuzzle(boolean isTest, int day) {
        this.isTest = isTest;
        this.day = day;
    }

    public List<String> readFile() {
        return readFile("#");
    }

    public List<String> readFile(String comment) {
        String fileName = isTest ? "example.txt" : "input.txt";
        String dayString = day < 10 ? "0" + day : Integer.toString(day);

        String inputFile = System.getProperty("user.dir") + "/out/production/AdventOfCode2019/day" + dayString + "/" + fileName;

        List<String> input = new ArrayList<>();

        try {
            input = Files.readAllLines(new File(inputFile).toPath());
            input = input.stream().filter(i -> !i.isEmpty() && !i.startsWith(comment))
                    .collect(Collectors.toList());
            input = setAnswers(input);
        } catch (Exception e) {
            System.out.println("Oh shit! " + e);
        }
        return input;
    }

    private List<String> setAnswers(List<String> inputs) {
        for (int i = 0; i < inputs.size(); i++) {
            String s = inputs.get(i);
            if (s.startsWith("answer1:")) {
                answer1 = s.split("answer1:")[1].trim();
            } else if (s.startsWith("answer2:")) {
                answer2 = s.split("answer2:")[1].trim();
            }
        }
        return inputs.stream().filter(i -> !i.startsWith("answer")).collect(Collectors.toList());
    }

    public long getAnswer1() {
        return Long.valueOf(answer1);
    }

    public long getAnswer2() {
        return Long.valueOf(answer2);
    }

    public String getAnswer1String() {
        return answer1;
    }

    public String getAnswer2String() {
        return answer2;
    }
}