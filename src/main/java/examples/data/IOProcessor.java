/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.data;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Dominik Olszewski
 */
public class IOProcessor {

    public static String nl;

    static {
        nl = System.lineSeparator();
    }

    /**
     * Reads a line from standard input, and returns it as a <code>String</code>
     * object.
     *
     * @return a <code>String</code> object read from the standard input
     */
    public static String readLine() {
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = in.readLine();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            line = "IOException has occurred";
        }
        return line;
    }

    public static String scanLine() {
        return new Scanner(System.in).nextLine();
    }

    public static int readInt() {
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = in.readLine();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            line = "IOException has occurred";
        }
        return Integer.parseInt(line);
    }

    public static int scanInt() {
        return new Scanner(System.in).nextInt();
    }

    public static void copyFile(String sourceFileNamePath, String destinationFileNamePath) {
        try {
            Scanner in = new Scanner(new File(sourceFileNamePath));
            PrintWriter out = new PrintWriter(new File(destinationFileNamePath));
            while (in.hasNextLine()) {
                out.println(in.nextLine());
            }
            in.close();
            out.close();
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
        }
    }

//    public static void copyFile(String sourceFileNamePath, String destinationFileNamePath) {
//        try (Scanner in = new Scanner(new File(sourceFileNamePath));
//                PrintWriter out = new PrintWriter(new File(destinationFileNamePath))) {
//            while (in.hasNextLine()) {
//                out.println(in.nextLine());
//            }
//        } catch (FileNotFoundException fnfe) {
//            System.err.println(fnfe.getMessage());
//        }
//    }
    
    public static String fileToString(String fileNamePath) {
        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new File(fileNamePath))) {
            while (in.hasNextLine()) {
                sb.append(in.nextLine() + nl);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
        }
        return sb.toString();
    }

//    public static void removeWordFromFile(String sourceFileNamePath, String destinationFileNamePath, String word) {
//        try (Scanner in = new Scanner(new File(sourceFileNamePath));
//                PrintWriter out = new PrintWriter(new File(destinationFileNamePath))) {
//            while (in.hasNext()) {
//                String s = in.next();
//                if (!s.equals(word)) {
//                    out.println(s);
//                }
//            }
//        } catch (FileNotFoundException fnfe) {
//            System.err.println(fnfe.getMessage());
//        }
//    }

    private static String removeWordFromLine(String line, String word) {
        String[] words = line.split(word);
        return String.join("", words);
    }

    private static String removeWordsFromLine(String line, String[] words) {
        String output = line;
        for (String s : words) {
            String[] tokens = output.split(s);
            output = String.join("", tokens);
        }
        return output;
    }

    public static void removeWordFromFile(String sourceFileNamePath,
            String destinationFileNamePath,
            String word) {
        try (Scanner in = new Scanner(new File(sourceFileNamePath));
                PrintWriter out = new PrintWriter(new File(destinationFileNamePath))) {
            while (in.hasNextLine()) {
                out.println(removeWordFromLine(in.nextLine(), word));
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
        }
    }
    
    public static void removeWordsFromFile(String sourceFileNamePath,
            String destinationFileNamePath,
            String[] words) {
        try (Scanner in = new Scanner(new File(sourceFileNamePath));
                PrintWriter out = new PrintWriter(new File(destinationFileNamePath))) {
            while (in.hasNextLine()) {
                out.println(removeWordsFromLine(in.nextLine(), words));
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
        }
    }
}
