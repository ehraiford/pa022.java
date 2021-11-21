/*=============================================================================
| Assignment: pa02 - Calculating an 8, 16, or 32 bit
| checksum on an ASCII input file
|
| Author: Evan Raiford
| Language: Java
|
| To Compile: javac pa02.java
| gcc -o pa02 pa02.c
| g++ -o pa02 pa02.cpp
|
| To Execute: java -> java pa02 inputFile.txt 8
| or c++ -> ./pa02 inputFile.txt 8
| or c -> ./pa02 inputFile.txt 8
| where inputFile.txt is an ASCII input file
| and the number 8 could also be 16 or 32
| which are the valid checksum sizes, all
| other values are rejected with an error message
| and program termination
|
| Note: All input files are simple 8 bit ASCII input
|
| Class: CIS3360 - Security in Computing - Fall 2021
| Instructor: McAlpin
| Due Date: November 21st
|
+=============================================================================*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class pa02 {
    public static void main(String[] args) throws FileNotFoundException {
        File inText = new File(args[0]);
        int bitSize = Integer.parseInt(args[1]);
        if(bitSize != 32 && bitSize != 8 && bitSize != 16){
            System.err.println("Valid checksum sizes are 8, 16, or 32\n");
            return;
        }
        Scanner in = new Scanner(inText).useDelimiter("\\Z");
        String originalString = readData(in);

        print80(originalString);

        if(bitSize == 8)
            create8(originalString);
        else if(bitSize == 16)
            create16(originalString);
        else
            create32(originalString);
    }

    private static void create32(String originalString) {
        long checksum = 0;
        if(originalString.length()%4 != 0)
            originalString = addBuffer(4, originalString);
        for(int ticker = 0; ticker < originalString.length(); ticker = ticker + 4){
            checksum = checksum + create4charSum(originalString.charAt(ticker), originalString.charAt(ticker+1), originalString.charAt(ticker+2), originalString.charAt(ticker+3));

            if(checksum > 4294967295L)
                checksum = checksum - 32768;
        }
        System.out.println(Integer.toHexString((int) checksum));
    }

    private static void create16(String originalString) {
        int checksum = 0;
        if(originalString.length()%2 != 0)
           originalString = addBuffer(2, originalString);

        for(int ticker = 0; ticker < originalString.length(); ticker = ticker + 2){
            checksum = checksum + create2charSum(originalString.charAt(ticker), originalString.charAt(ticker+1));

            if(checksum > 65535)
                checksum = checksum - 32768;
        }
        System.out.println(Integer.toHexString(checksum));
    }

    private static int create2charSum(char charAt, char charAt1) {
        String binary = ensure8Bit(Integer.toBinaryString(charAt));
        binary = binary + ensure8Bit(Integer.toBinaryString(charAt1));

        return Integer.parseInt(binary, 2);
    }
    private static long create4charSum(char charAt, char charAt1, char charAt2, char charAt3) {
        String binary = ensure8Bit(Integer.toBinaryString(charAt));
        binary = binary + ensure8Bit(Integer.toBinaryString(charAt1));
        binary = binary + ensure8Bit(Integer.toBinaryString(charAt2));
        binary = binary + ensure8Bit(Integer.toBinaryString(charAt3));

        return Integer.parseInt(binary, 2);
    }

    private static String ensure8Bit(String toBinaryString) {
        String addingBuffer = "";
        while(addingBuffer.length() + toBinaryString.length() < 8)
            addingBuffer = addingBuffer + "0";
        return addingBuffer + toBinaryString;
    }

    private static void create8(String originalString) {
        int checksum = 0;
        for(int ticker = 0; ticker < originalString.length(); ticker++) {
            checksum = checksum + originalString.charAt(ticker);
            if(checksum > 255)
                checksum = checksum - 128;
        }
        System.out.println(Integer.toHexString(checksum));
    }

    private static String addBuffer(int i, String originalString) {
        while(originalString.length() % i != 0)
            originalString = originalString.concat("X");

        return originalString;
    }

    private static void print80(String originalString) {
        int ticker = 0;
        while(ticker < originalString.length()){
            for(int i = 0; i < 80 && ticker < originalString.length(); i++){
                System.out.print(originalString.charAt(ticker));
                ticker++;
            }
            System.out.println();
        }
    }

    private static String readData(Scanner in) {
        String data = "";
        while(in.hasNext()){
            data = data.concat(in.next());
        }
        return data;
    }
}
/*=============================================================================
| I, Evan Raiford (ev316149) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/