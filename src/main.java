/**
 Adam Pei
 10th January 2024
 Vigenere's Cipher
 Help given by Ms Nahar, Mr Tam, GeeksForGeeks
 This program encrypts and decrypts text files based on a shift key.
 **/


import java.io.*;
import java.util.Scanner;
import java.util.Arrays;


public class main {


    public static void main(String[] args) throws Exception {
        int gameStatus = 1;


        Scanner scanner = new Scanner(System.in);


        System.out.println("Hello there! This is a program that uses Vigenere's Cipher to encrypt and decrypt data." + "\n");


        String[] fileChoices = new String[99];
        fileChoices[0] = "poem.txt";
        fileChoices[1] = "story.txt";
        fileChoices[2] = "speech.txt";



        while (gameStatus == 1) //while the user has not quit
        {
            //Ask user for their file
            String chosenFile = chooseFile(fileChoices);


            //Ask user whether they want to encrypt or decrypt
            System.out.println("\n" + "Would you like to encrypt or decrypt this data? Respond with 1 for encrypt. Respond with 2 for decrypt.");
            int crypType = scanner.nextInt();
            if (crypType != 1 && crypType != 2) {
                System.out.println("Please respond with 1 or 2.");
                crypType = scanner.nextInt();
            }


            System.out.println("Enter the shift key you would like to use.");
            String shift = scanner.next();


            //Encrypt/decrypt the text and display it
            String[] finalArray = convert(crypType, chosenFile, shift);
            System.out.println("\n" + "Below is your encrypted/decrypted data");
            for (String i : finalArray) {
                System.out.println(i);
            }


            //Ask user whether they want to store the converted data into a text file
            System.out.println("\n" + "Would you like to store this encrypted/decrypted data into a text file? Respond with 1 for yes. Respond with 2 for no.");
            int storeResponse = scanner.nextInt();
            while (storeResponse != 1 && storeResponse != 2) {
                System.out.println("\n" + "Please respond with 1 or 2.");
                storeResponse = scanner.nextInt();
            }
            if (storeResponse == 1)
            {
                storeFile(finalArray, fileChoices);
            }
            else
            {
                System.out.println("\n" + "Noted. The encrypted/decrypted data will not be stored in a text file");
            }


            //Ask user whether they want to continue or quit
            System.out.println("\n" + "Would you like to encrypt/decrypt another file? Respond with 1 for yes. Response with 2 to quit.");
            gameStatus = scanner.nextInt();
            while (gameStatus != 1 && gameStatus != 2)
            {
                System.out.println("\n" + "Please respond with 1 or 2.");
                gameStatus = scanner.nextInt();
            }
            if (gameStatus == 2)
            {
                System.out.println("Thanks for using this program. Goodbye.");
            }
        }




    }


    public static String chooseFile(String[] fileChoices) throws FileNotFoundException {
        boolean validResponse = false;
        String fileChoice = ""; //Will be used to store the name of the user's chosen file
        Scanner scanner = new Scanner(System.in);


        //Display the names of existing files
        System.out.println("The current existing text files are:");
        for (String i : fileChoices)
        {
            if(i != null)
            {
                System.out.println(i);
            }
        }


        System.out.println("\n" + "Would you like to encrypt/decrypt an existing file or create your own? Respond with 1 to choose an existing file. Respond with 2 to create your own.");
        int choice = scanner.nextInt();


        //Check that the user's response is valid
        while (choice != 1 && choice !=2)
        {
            System.out.println("Invalid response. Please respond with 1 or 2.");
            choice = scanner.nextInt();
        }


        if (choice == 1)   //If the user chooses to use an existing file
        {
            System.out.println("\n" + "Enter the name of the file you want to choose");
            fileChoice = scanner.next();


            while(!validResponse)
            {
                //Check if the inputted file name exists
                for (int i = 0; i < fileChoices.length; i++) {
                    if (fileChoices[i] != null && fileChoices[i].equals(fileChoice)) {
                        validResponse = true;
                    }
                }


                //Prompt the user again for a file name if the file name doesn't exist
                if (!validResponse)
                {
                    System.out.println("File does not exist. Please enter the name of a file that does exist.");
                    fileChoice = scanner.next();
                }
            }
        }


        else if (choice == 2)    //If the user chooses to create their own file
        {
            System.out.print("\n" + "Please enter the name of your file: ");
            fileChoice = scanner.next();


            System.out.println("\n" + "Input the text you want in your file. Type END on a line of its own to finish.");


            //Lets the user input their text until they type END on a line of its own and presses enter
            PrintStream output = new PrintStream(fileChoice);
            String line = scanner.nextLine();
            while (!line.equals("END")) {
                output.println(line);
                line = scanner.nextLine();
            }
            output.close();


            //Add the new file to the array of existing file names
            for (int i = 0; i < fileChoices.length; i++)
            {
                if (fileChoices[i] == null)
                {
                    fileChoices[i] = fileChoice;
                    break;
                }


            }
        }
        return(fileChoice);
    }



    public static String[] convert(int crypType, String filename, String shift) throws Exception {
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; //add the big letters later
        char[] bigLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] originalArray = readFileIntoArray(filename); //the original text file will be stored in this array
        String[] finalArray = new String[originalArray.length]; //the encrypted/decrypted text will be stored in this array
        int index = 0; //used to represent the index of finalArray
        int shiftIndex = 0; //index of the shift array
        int tempNum = 0; //used to store the index of the character in the letters[] or bigLetters[] array after being incremented


        //convert each character in the shift key to their integer value and then put those values in an array
        int[] shiftArray = new int[shift.length()];
        for (int i = 0; i < shift.length(); i++)
        {
            shiftArray[i] = shift.charAt(i);
        }

        for (int i = 0; i < shift.length(); i++){
            tempNum = 0;
            for (int p = 0; p < letters.length; p++)
            {
                if (shiftArray[i] == letters[p])
                {
                    tempNum = p;
                }
            }
            shiftArray[i] = tempNum;
        }




        index = 0;
        for (String line : originalArray){
            String temp = ""; //used to store an encrypted/decrypted line
            String tempLetter = ""; //used to store an encrypted/decrypted character


            for (int j = 0; j < line.length(); j++) //for each letter in the line
            {
                boolean isLetter = false;




                for (int f = 0; f < letters.length; f++)
                {
                    if (line.charAt(j) == letters[f] || line.charAt(j) == bigLetters[f])   //check if the character is a letter of the alphabet
                    {
                        isLetter = true;


                        if (crypType == 1) //if the user chose to encrypt
                        {
                            tempNum = f + shiftArray[shiftIndex];
                        }


                        else //if the user chose to decrypt
                        {
                            tempNum = f - shiftArray[shiftIndex];
                        }


                        //if a character is incremented past 'z' or 'Z', it will continue incrementing from 'a' or 'A'
                        if (tempNum > 25)
                        {
                            tempNum -= 26;
                        }


                        //if a character is decremented past 'a' or 'A', it will continue decrementing from 'z' or 'z'
                        if (tempNum < 0)
                        {
                            tempNum += 26;
                        }


                        //increment the character based on the shift key
                        if (line.charAt(j) == letters[f]) //if the character is lower case
                        {
                            tempLetter = String.valueOf(letters[tempNum]);
                        }
                        else //if the character is upper case
                        {
                            tempLetter = String.valueOf(bigLetters[tempNum]);
                        }


                        //increment to the next character of the shift key
                        if (shiftIndex == shiftArray.length-1) //go back to the first character if the current character is the last character
                        {
                            shiftIndex = 0;
                        }
                        else
                        {
                            shiftIndex++;
                        }
                    }


                }


                //replace the original character with the encrypted/decrypted letter if the character is a letter of the alphabet
                if (isLetter)
                {
                    temp += tempLetter;
                }


                //use the original character if the character is not a latter of the alphabet
                else
                {
                    temp += line.charAt(j);
                }
            }


            finalArray[index] = temp; //after one line is encrypted/decrypted, add it to the final array
            index+=1; //incremenet the index so the next line is added to the next index of the final array
        }


        return finalArray;
    }


    public static void storeFile(String[] finalArray, String[] fileChoices) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);


        System.out.print("\n" + "Please enter the name of the new file: ");
        String fileName = scanner.next();


        //Store the elements of the encrypted/decrypted array into a file
        PrintStream store = new PrintStream(fileName);
        for (String i : finalArray)
        {
            store.println(i);
        }
        store.close();


        //Store the name of the new file into the array of existing file names
        for (int i = 0; i < fileChoices.length; i++)
        {
            if (fileChoices[i] == null)
            {
                fileChoices[i] = fileName;
                break;
            }


        }
    }


    public static int countLinesInFile(String nameOfFile) throws FileNotFoundException {


        File file = new File(nameOfFile);
        Scanner scanner = new Scanner(file);


        int lineCount = 0;
        while (scanner.hasNextLine()) {
            lineCount++;
            scanner.nextLine();
        }
        return lineCount;
    }


    public static String[] readFileIntoArray(String nameOfFile) throws Exception {


        int linesInFile = countLinesInFile(nameOfFile);
        String[] array = new String[linesInFile];


        File file = new File(nameOfFile);
        Scanner scanner = new Scanner(file);


        int index = 0;
        while (scanner.hasNextLine()) //iterate to the end of the file
        {
            array[index++] = scanner.nextLine(); //put each line of the file into an element in the array
        }
        return array;
    }


}

