package org.boy.aoc.twentytwenty.exercises;

public class Eight extends SolutionTemplate {
    @Override
    public String pointOne(String input) {
        return String.valueOf(runProgram(input.split("\n"), false));
    }

    @Override
    public String pointTwo(String input) {
        String[] instructions = input.split("\n");
        int acc = 0;
        for(int i = 0; i < instructions.length; i++) {
            String op = instructions[i];

            if (op.startsWith("jmp")) {
                instructions[i] = op.replace("jmp", "nop");
                acc = runProgram(instructions.clone(), true);
                instructions[i] = op;
            } else if (op.startsWith("nop")) {
                instructions[i] = op.replace("nop", "jmp");
                acc = runProgram(instructions.clone(), true);
                instructions[i] = op;
            }

            if (acc > 0) break;
        }
        return String.valueOf(acc);
    }

    private int runProgram(String[] instructions, boolean loopDetection) {
        int accumulator = 0;
        int command = 0;

        while (command < instructions.length) {

            String line = instructions[command];
            if (line.equals("")) break;

            instructions[command] = "";

            String op = line.split(" ")[0];
            int arg = Integer.parseInt(line.split(" ")[1]);

            switch (op) {
                case "nop":
                    command++;
                    break;
                case "acc":
                    accumulator += arg;
                    command++;
                    break;
                case "jmp":
                    command += arg;
                    break;
            }
        }

        return (!loopDetection || command == instructions.length) ? accumulator : 0;
    }

}
