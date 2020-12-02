package org.boy.aoc.twentytwenty;

import org.boy.aoc.twentytwenty.exercises.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main Advent of Code class
 *
 */
public class AocMain
{
    public static void main( String[] args ) throws IOException {
        AocMain main = new AocMain();

        String inputFileLocation = args[0] + args[1] + ".txt";
        int dayNumber = Integer.parseInt(args[1]);
        int pointNumber = Integer.parseInt(args[2]);

        System.out.println(
                main.runDay(
                        dayNumber,
                        pointNumber,
                        main.readInput(inputFileLocation)
                )
        );
    }

    public String runDay(int dayNumber, int pointNumber, String input) {

        SolutionTemplate day;

        switch (dayNumber) {
            case 1:
                day = new One();
                break;
            case 2:
                day = new Two();
                break;
            default:
                return "dayNumber not recognized";
        }

        return (pointNumber == 1) ? day.pointOne(input) : day.pointTwo(input);
    }

    public String readInput(String fileLocation) throws IOException {
        return Files.readString(Path.of(fileLocation));
    }
}
