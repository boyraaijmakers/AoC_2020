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

        long timeStart = System.currentTimeMillis();
        System.out.println(
                main.runDay(
                        dayNumber,
                        pointNumber,
                        main.readInput(inputFileLocation)
                )
        );
        long timeEnd = System.currentTimeMillis();

        System.out.println("\nSolution time: " + (timeEnd - timeStart) + "ms");
    }

    public String runDay(int dayNumber, int pointNumber, String input) {
        SolutionTemplate[] days = {
                new One(), new Two(), new Three(), new Four(), new Five(), new Six(), new Seven(),
                new Eight(), new Nine(), new Ten(), new Eleven(), new Twelve(), new Thirteen(),
                new Fourteen(), new Fifteen(), new Sixteen(), new Seventeen(), new Eighteen(),
                new Nineteen(), new Twenty(), new Twentyone(), new Twentytwo(), new Twentythree(),
                new Twentyfour(), new Twentyfive()
        };

        return (pointNumber == 1) ? days[dayNumber - 1].pointOne(input) : days[dayNumber - 1].pointTwo(input);
    }

    public String readInput(String fileLocation) throws IOException {
        return Files.readString(Path.of(fileLocation));
    }
}
