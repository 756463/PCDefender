import java.util.*;
import java.io.*;

//Premade class with methods related to scanner and file information.
public class Prompt{
    private static Scanner input = new Scanner(System.in);
    
    public static int getInt(String prompt, int min, int max){
        while(true){
            System.out.print(prompt);
            int i = input.nextInt();
            input.nextLine();
            if(i <= max && i >= min){
                return i;
            }
            System.out.println("Error: Enter a number between " + min + " and " + max);
        }
    }
    
    public static double getDouble(String prompt, double min, double max){
        while(true){
            System.out.print(prompt);
            double i = input.nextDouble();
            input.nextLine();
            if(i <= max && i >= min){
                return i;
            }
            System.out.println("Error: Enter a number between " + min + " and " + max);
        }
    }
    
    public static String getString(String prompt){
        System.out.print(prompt);
        return input.nextLine();
    }

    public static File getInputFile(String prompt){
        try{
            System.out.print(prompt);
            String filename = input.nextLine();
            File file = new File(filename);
            if(file.exists() && file.canRead()){
                return file;
            } else{
                System.out.println("Error: File does not exist or cannot be read.");
                return null;
            }
        } catch(Exception e){
            System.out.println("Error: Invalid file.");
            return null;
        }
    }

    public static Scanner getInputScanner(String prompt){
        try{
            File file = getInputFile(prompt);
            return new Scanner(file);
        } catch(Exception e){
            System.out.println("Error: Could not open file.");
            return null;
        }
    }
    
    public static Scanner getInputScannerPromptless(String filename){
        try{
            File file = new File(filename);
            return new Scanner(file);
        } catch(Exception e){
            System.out.println("Error: Could not open file.");
            return null;
        }
    }

    public static PrintWriter getPrintWriter(String prompt, boolean append){
        try{
            System.out.print(prompt);
            String filename = input.nextLine();
            return new PrintWriter(new FileWriter(filename, append));
        } catch(Exception e){
            System.out.println("Error: Could not create file.");
            return null;
        }
    }
    
    public static void getPrintWriterPromptless(String filename, String output, boolean append){
        try{
            PrintWriter outFile = new PrintWriter(new FileWriter(filename, append));
            outFile.println(output);
            outFile.close();
        } catch(Exception e){
            System.out.println("Error: Could not create file.");
        }
    }
}