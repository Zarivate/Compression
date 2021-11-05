import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Project Description:
 * This program simulates compresses and decompresses files. It comes in two parts, one to handle compression
 * while the other handles decompression. Both programs utilize HashMaps but in the reverse order of one another.
 *
 *@Author Ivan Zarate
 *CSCI 340
 *Section 001*
 *Assignment 3 LZW
 *Known Bugs: None
 */

public class Compress {

    // Global Hashmap called "compressTime" to be used and updated later
    private static Map<String, Integer> compressTime = new HashMap<>();

    public static void main(String[] args) throws IOException {

        // Loop to fill up the first 127 places of the HashMap with ASCII characters
        for (int i = 0; i < 128; i++) {
            char temp = (char) i;
            compressTime.put(Character.toString(temp), i);
        }

        // Scanner to take user input
        Scanner input = new Scanner(System.in);

        // Prompt to ask use which file to compress
        System.out.println("Please enter the name of the file you wanna compress:");

        // String set equal to whatever the user put in, minus any extra white space at the end
        String readFile = input.nextLine().trim();

        // String to hold the new name of the file, which will just be called the same except with a ".lzw" at the end
        String outFileName = readFile + ".lzw";

        // File object created using the user input
        File inputFile = new File(readFile);

        // File object created to be used as the new file created at the end
        File outputFile = new File(outFileName);

        // Calls the compressFile method below using the files created above
        compressFile(inputFile, outputFile);

    }

    /**
     *
     * @param fileName is the parameter that takes the original file chosen to be decompressed
     * @param newFile is the parameter that takes in the file that will be output
     * @throws IOException is what's thrown when there is an error inputting or outputting the file
     */

    private static void compressFile(File fileName, File newFile) throws IOException {

        // String variable set to empty to be used for compression later
        String str = "";

        // Variable to hold the position something can have in the HasMap, as 0-127 are already filled we set it to 128
        int mapPosition = 128;

        // Variable to hold the position in a byte array for later
        int position = 0;

        // RandomAccessFile that reads the parameter "fileName" sent in, it's the original file chosen by the user in main
        RandomAccessFile file = new RandomAccessFile(fileName, "r");

        // RandomAccessFile that reads and writes to a new "compressedFile", will be called whatever was sent in as "newFile"
        RandomAccessFile compressedFile = new RandomAccessFile(newFile, "rw");

        try {

            // Convert the original file sent in to a byte array that holds every character
            byte[] array = Files.readAllBytes(Paths.get(String.valueOf(fileName)));

            // Loop to go through entire file
            while (true) {

                // Set an integer equal to the current byte
                int i = file.readByte();

                // String chr that holds the current character in the file
                String chr = Character.toString(array[position]);

                // String to hold the combined string of "str" and "chr"
                String combined = str + chr;

                // If statement that only processes if the HashMap contains the combined string
                if (compressTime.containsKey(combined)) {

                    // Set string "str" equal to the combined word
                    str = combined;

                // if Hashmap doesn't contain it then goes through this statement
                } else {

                    // First checks to make sure the HashMap size isn't greater than 256
                    if (mapPosition < 256) {

                        // Puts the combined word in the updated "mapPosition"
                        compressTime.put(combined, mapPosition++);
                    }

                    // Writes to the new file
                    compressedFile.writeByte(compressTime.get(str));

                    // Set "str" equal to "chr"
                    str = chr;
                }
                // Update the position in the byte array
                position++;
            }

        // Catch statement in case any errors are found
        } catch (EOFException eof) {

            // If end of file is reached it prints out the statement below
            System.out.println("End of File reached.");

            // Once end of file is reached the most recent "str" is written to the output file
            compressedFile.writeByte(compressTime.get(str));

        } catch (Exception e) {

            // If catches any other exception prints out statement below
            System.out.println("An Exception occurred. " + e);
        }
    }
}