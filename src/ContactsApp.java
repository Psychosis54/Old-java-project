import java.io.*;
import java.util.ArrayList;

public class ContactsApp {
    public static void main(String [] args) {
        Console console = System.console();
        Boolean shutDown = false;
        Info newContact = new Info(null, null, null, null, null, null);
        //Start of the main method calls all essential methods from all other classes 
        
        System.out.println("Welcome!");
        System.out.println("Please input your desired choice with a number");
        //A simple while loop I don't foresee changing soon.
        while(!shutDown) {
            System.out.println("1. View contacts");
            System.out.println("2. Add contacts");
            System.out.println("3. Edit contacts");
            System.out.println("4. Delete contacts");
            System.out.println("5. Exit");
            String userChoice = System.console().readLine();
            switch (userChoice) {
                case "1":
                    Info.readContact();
                    break;
                case "2":
                    newContact.addContact();
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    shutDown = true;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}
//20.11.2023
//The main hub of the code which will call all classes and methods and execute them for the user
//Possible changes coming to the structure and potential expansions for classes when necessary
//The basis of the user selection will be done with a switch case that will run until user decides
//to stop. The empty selections will call the respective classes and methods once done.
//Decided to use string instead of int for the user input to avoid making too much of a mess with exception
//catching. If it gets hairy down the line, I will probably change things around.

//29.11.2023
//I managed to figure out how to create files and made a method that creates it if not present
//However, getting the contacts added there is a new hiccup since the object referred here
//doesn't change depending on how many people were saved on the file, so there effectively is just one
//contact slot. (And unreadable at that)
//I need to make a method that scans the file for objects and creates the contact in a way that
//increments numbers into the creation
//