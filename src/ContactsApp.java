import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Class ContactsApp
 * @param args are unused.  
 * There are many variables defined as public static since they are used between several methods.
 * Some of these could still be optimized to keep it more lean and to not rely too much on making
 * them public.
 * 
 * The public variables include:
 * @param contactsList arraylist is used as a backbone for the contact management
 * @param contacts creates the file for saving contacts into. It will be in the source directory
 * with the name "contacts.txt"
 * @param oos is the objectOutputStream used to write into the file
 * @param ois is the objetInputStream used to read from the file and loads it into the arraylist
 * @param lister is a ListIterator object used to browse through the contactsList
 * @param found is a boolean value used for searching, editing and deleting. This is related
 * to the select() method and the editContact and deleteContact method also needs to know
 * at which state it is in.
 * @param editIndex is an integer value used to keep track of which index should the editing methods
 * edit. It's found in the select method and exists solely to keep the editing and deleting informed
 */
//I've supressed the warnings for the time being. It complains about the checkfile method
//and unchecked casts.
@SuppressWarnings("unchecked")
public class ContactsApp implements Serializable {
    //File systems and OOS is private static so they will be the same for each method
    public static ArrayList<Contact> contactsList = new ArrayList<Contact>();//<--- This is where the unchecked casts warning is coming from
    public static File contacts = new File("contacts.txt");                  //"All of this 'just works' " -Todd Howard, 2016
    public static ObjectOutputStream oos;
    public static ObjectInputStream ois;
    public static ListIterator lister;
    public static boolean found;
    public static int editIndex;
    //There are plenty of variables defined globally, since they are used between methods.

    /**
     * Main method
     * @throws Exception in accordance with the checkFile method to handle runtime exceptions
     * @param console is the system console used to handle user inputs
     * @param shutDown is used to exit the main loop of the program
     * @param userChoice is used to take the user input for the selection
     */
    public static void main(String [] args) throws Exception {
        /**
         * The main method calls all respective methods for viewing, adding, editing and deleting contacts.
         * It uses a while loop and a boolean value to control the main loop and uses a switch case for the
         * user menu. The user will use the numerical keys to control the functions of the program, and they
         * in turn in the code will call the methods.
         */
        Console console = System.console();
        Boolean shutDown = false;
        checkFile();
        /**
         * The checkFile method is called within the main method to see if the file is intact and will load
         * the contents into the arraylist for saved contacts
         */
        System.out.println("Welcome!");
        System.out.println("Please input your desired choice and press enter");
        while (!shutDown) {
            System.out.println("1. View contacts");
            System.out.println("2. Add contacts");
            System.out.println("3. Edit contacts");
            System.out.println("4. Delete contacts");
            System.out.println("5. Exit");
            String userChoice = System.console().readLine();
            switch (userChoice) {
                case "1":
                    readContact();
                    break;
                case "2":
                    addContact();
                    break;
                case "3":
                    editContact();
                    break;
                case "4":
                    deleteContact();
                    break;
                case "5":
                    shutDown = true;
                    break;
                default:
                    System.out.println("Invalid input. Please try again");
                    break;
            }
        }
    }
    /**
     * checkFile method
     * @throws Exception for runtime exceptions
     * @param contacts is used here to check the file integrity and to load the contents
     * to the arraylist
     */
    public static void checkFile() throws Exception {
        /**
         * The checkFile will check if the file exists and will proceeed to load the file
         * into the arraylist
         */
        if (contacts.isFile()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(contacts));
                contactsList = (ArrayList<Contact>)ois.readObject();
                ois.close();
            } catch (IOException e) {
                /**
                 * It will catch any IOExceptions and will notify the user if their file is corrupt.
                 * This will often happen if the file is tampered with.
                 */
                //I replaced the stacktrace with a dialogue that just says 'you done goofed'
                System.out.println("Your file is corrupt. Your previously saved contacts are lost");
                System.out.println("To generate a new file, add a new contact");
                System.out.println("Press enter to continue");
                String userConf = System.console().readLine();
            }
        }
    }
    /**
     * writeToFile method
     * @param oos is used here to write to the file
     */
    public static void writeToFile() {
        /**
         * writeToFile method is used to compress the writing to the file by calling it within methods
         * when it is needed. It will use the predefined ObjectOutPut stream declared in the class.
         */
        //Compressess the code to not have OOS everywhere separately
        try {
            oos = new ObjectOutputStream(new FileOutputStream(contacts));
            oos.writeObject(contactsList);
            oos.close();
        } catch (IOException e) {
            /**
             * In the case of an IOException, the program will print the stack trace and also notify
             * the user that an error has occured
             */
            e.printStackTrace();
            System.out.println("An unexpected error has occurred");
            //Might be necessary to still have a stack trace here
            //I cleared out all the unnecessary throws when it can just be declared here and print
            //the stack trace
        }
    }
    /**
     * method addContact
     * @param id
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param address
     * @param email
     * The method contains all variables for creating a Contact object
     */
    public static void addContact() {
        /**
         * This method will use the input methods for user validation and then create
         * an object into the file using the writeToFile method.
         */
            String id = "";
            String firstName = "";
            String lastName = "";
            String phoneNumber = "";
            String address = "";
            String email = "";
            System.out.println("Give the ID of the contact");
            id = IdInput(id);
            System.out.println("Give the first name");
            firstName = firstNameInput(firstName);
            System.out.println("Give the last name");
            lastName = lastNameInput(lastName);
            System.out.println("Give the phone number");
            phoneNumber = phoneNumberInput(phoneNumber);
            System.out.println("(Opt.)Give the address");
            address = addressInput(address);
            System.out.println("(Opt.) Give the email");
            email = emailInput(email);
            System.out.println("Contact added!");
            contactsList.add(new Contact(id, firstName, lastName, phoneNumber, address, email));
            writeToFile();
            System.out.println("-----------------");
            System.out.println("Press enter to continue");
            String userConf = System.console().readLine();
    }
    /**
     * method readContact
     * @param exit is used to control the loop for displaying the read menu.
     * @param readChoice is used to control the switch for different options
     */
    public static void readContact() {
        //readContact method will read all the contacts within the file. 
        //However, not directly from the file and rather from the arraylist that works as an intermediary
        lister = contactsList.listIterator();
        boolean exit = false;
        while (!exit) {
            System.out.println("1. View all");
            System.out.println("2. Search");
            System.out.println("3. Back");
            String readChoice = System.console().readLine();
            switch (readChoice) {
                //In order to use the functionalities in the edit and delete method, I made them into separate methods
                //so they could be reused effectively.
                case "1":
                    //This will return the listIterator to the first index so it doesnt bug out of range
                    //I realized that if it was inside the method, it could possibly cause trouble for the editing and deleting functions
                    lister = contactsList.listIterator(0);
                    viewAll();
                    System.out.println("Press enter to continue");
                    String userConf = System.console().readLine();
                    break;
                case "2":
                    lister = contactsList.listIterator(0);
                    select();
                    System.out.println("Press enter to continue");
                    String userConf2 = System.console().readLine();
                    break;
                case "3":
                    exit = true;
                    break;
                default :
                    System.out.println("Invalid input");
                    break;
                
            }
        }
    }
    /**
     * Method viewAll
     * 
     * Used for viewing contacts loaded into the arraylist
     * @param capacity is the size taken from the arraylist for contacts. Used to determine if
     * the arraylist is empty or the file is missing completely
     * @param lister is used here to go through the whole contact arraylist and print them out
     * into the console. It is used togheter with the ListIterator classes hasNext method in a 
     * while loop.
     */
    public static void viewAll() {
        /**
         * The viewAll method will go through the arraylist where the contacts are saved and will
         * print them all out for the user to see
         */
        int capacity = contactsList.size();
        lister = contactsList.listIterator();
        /**
         * It uses a listIterator object predefined classwide for use in other methods as well.
         * It also keeps track of the capacity of the list and will notify the user if the list
         * is empty or missing.
         */
        System.out.println("-------------------");
        while (lister.hasNext()) {
            System.out.println(lister.next());
        }
        if (capacity == 0) {
            System.out.println("Your list is empty, or the file is missing");
        }
        System.out.println("--------------------");
    }
    /**
     * Method select
     * @param lister is used to go through the arraylist
     * @param capacity is used to check the size of the contactslist as in the viewAll method
     * @param searchName is used to search for the contact by its first name
     * @param searchInput is a Contact object that goes through the arraylist with the help
     * of listiterator and applies itself as the object the first name equals to in the list
     * @param exit is used to control the while loop for the console input
     * @param indexTrack is used to amount the index in which the target contact is and will
     * apply itself to the publically defined editIndex
     * 
     */
    public static void select() {
        //Why select instead of search as the name? Well, this method will also be used within the method for editing and deleting contacts
        int capacity = contactsList.size();
        String searchName = "";
        lister = contactsList.listIterator();
        //Index track will be used to see at which part of the arraylist the select method is at
        int indexTrack = 0;
        found = false;
        Boolean exit = false;
        while (!exit) {
            if (capacity == 0) {
                System.out.println("--------------------");
                System.out.println("Your list is empty, or the file is missing");
                exit = true;
            } else {
                System.out.println("Enter the first name of the contact");
                searchName = System.console().readLine();
                System.out.println("--------------------");
                exit = true;
            }
        }
        while (lister.hasNext()) {
            //It will use the getter method on the 'quasi' object that goes throgh the list
            //and compares it with the user given input.
            Contact searchInput = (Contact)lister.next();
            if (searchInput.getFirstName().equals(searchName)) {
                System.out.println(searchInput);
                found = true;
                editIndex = indexTrack;
                //Edit index is used for the edit and delete functions to delete or edit objects stored
                //at the specific index
            }
            indexTrack++;
        }
        if (!found) {
            System.out.println("Contact not found.");
        }
        System.out.println("--------------------");
    }
    /**
     * @param lister is reset here to the first index of the arraylist in order to not get it stuck
     * to a specific index from search and viewing methods
     * @param exit is used to control the while loop that asks the user which contact to delete
     */
    public static void deleteContact() {
        //Added the view all method so the user can see all available contacts for deletion
        lister = contactsList.listIterator(0);
        boolean exit = false;
        viewAll();
        if (contactsList.size() == 0) {
            System.out.println("No contacts to delete. Press enter to continue");
            String userConfExit = System.console().readLine();
            exit = true;
        }
        while (!exit) {
            select();
            if (found) {
                System.out.println("Are you sure you want to delete this contact? y/n?");
                String userChoice = System.console().readLine();
                switch (userChoice.toLowerCase()) {
                    case "y":
                        //Removed the lambda and made it more consistent with the editing function
                        contactsList.remove(editIndex);
                        writeToFile();
                        System.out.println("Contact deleted");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf2 = System.console().readLine();
                        exit = true;
                        break;
                    case "n":
                        System.out.println("Deletion aborted");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf3 = System.console().readLine();
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            } else {
                System.out.println("The contact you entered did not exist. Press enter to continue");
                String userConf = System.console().readLine();
                exit = true;
            }
        }
    }
    public static void editContact() {
        //Zeroes the listIterator index in order to not get stuck into one contact
        lister = contactsList.listIterator(0);
        boolean exit = false;
        //ditto
        viewAll();
        if (contactsList.size() == 0) {
            System.out.println("No contacts to edit. Press enter to continue");
            String userConfExit = System.console().readLine();
            exit = true;
        } else {
            select();
        }
        while (!exit) {
            if (found) {
                System.out.println("How do you want to edit this contact?");
                System.out.println(contactsList.get(editIndex));
                System.out.println("---------------");
                System.out.println("1. ID, 2. First name, 3. Last name, 4. Phone number, 5. Address, 6. Email 7. Exit");
                
                String userChoice = System.console().readLine();
                switch (userChoice) {
                    case "1":
                        String newId = "";
                        System.out.println("Enter the new ID");
                        newId = IdInput(newId);
                        contactsList.get(editIndex).setId(newId);
                        writeToFile();
                        System.out.println("ID updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf = System.console().readLine();
                        break;
                    case "2":
                        String newFirst = "";
                        System.out.println("Enter the new first name");
                        newFirst = firstNameInput(newFirst);
                        contactsList.get(editIndex).setFirstName(newFirst);
                        writeToFile();
                        System.out.println("First name updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf1 = System.console().readLine();
                        break;
                    case "3":
                        String newLast = "";
                        System.out.println("Enter the new last name");
                        newLast = lastNameInput(newLast);
                        contactsList.get(editIndex).setLastName(newLast);
                        writeToFile();
                        System.out.println("Last name updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf2 = System.console().readLine();
                        break;
                    case "4":
                        String newPhone = "";
                        System.out.println("Enter the new Phone number");
                        newPhone = phoneNumberInput(newPhone);
                        contactsList.get(editIndex).setPhoneNumber(newPhone);
                        writeToFile();
                        System.out.println("Phone number updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf3 = System.console().readLine();
                        break;
                    case "5":
                        String newAddress = "";
                        System.out.println("Enter the new Address");
                        newAddress = addressInput(newAddress);
                        contactsList.get(editIndex).setAddress(newAddress);
                        writeToFile();
                        System.out.println("Address updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf4 = System.console().readLine();
                        break;
                    case "6":
                        String newEmail = "";
                        System.out.println("Enter the new email");
                        newEmail = emailInput(newEmail);
                        contactsList.get(editIndex).setEmail(newEmail);
                        writeToFile();
                        System.out.println("Email updated!");
                        System.out.println("---------------");
                        System.out.println("Press enter to continue");
                        String userConf5 = System.console().readLine();
                        break;
                    case "7":
                        exit = true;
                        break;
                    default :
                        System.out.println("Invalid input");
                        break;
                }
            } else {
                System.out.println("The contact you entered did not exist. Press enter to continue");
                String userConf = System.console().readLine();
                exit = true;
            }
        }
    }
    public static String IdInput(String id) {
        Boolean validInput = false;
        Pattern idPattern = Pattern.compile("[0-3]{1}[1-9]{1}[0-9]{2}+[-A][0-9]{3}[A-Z]{1}");
        while (!validInput) {
            id = System.console().readLine();
            Matcher idMatch = idPattern.matcher(id);
            if (idMatch.matches()) {
                validInput = true;
                return id;
            } else {
                System.out.println("Invalid ID. Give a legitimate Finnish SSN");
            }
        }
        return id;
    }
    public static String firstNameInput(String first) {
        Boolean validInput = false;
        Pattern firstNamePattern = Pattern.compile("[A-Z]{1}[a-z]{1,11}");
        while (!validInput) {
            first = System.console().readLine();
            Matcher firstNameMatch = firstNamePattern.matcher(first);
            if (firstNameMatch.matches()) {
                validInput = true;
                return first;
            } else {
                System.out.println("Invalid first name. Capitalize the first letter and keep the size between 2-12 letters");
            }
        }
        return first;
    }
    public static String lastNameInput(String last) {
        Boolean validInput = false;
        Pattern lastNamePattern = Pattern.compile("[A-Z]{1}[a-z]{1,20}");
        while (!validInput) {
            last = System.console().readLine();
            Matcher lastNameMatch = lastNamePattern.matcher(last);
            if (lastNameMatch.matches()) {
                validInput = true;
                return last;
            } else {
                System.out.println("Invalid last name. Capitalize the first letter and keep the size between 2-20 letters");
            }
        }
        return last;
    }
    public static String phoneNumberInput(String phone) {
        Boolean validInput = false;
        Pattern phonePattern = Pattern.compile("[A-Z]{1}[a-z]{1,20}");
        while (!validInput) {
            phone = System.console().readLine();
            Matcher phoneMatch = phonePattern.matcher(phone);
            if (phoneMatch.matches()) {
                validInput = true;
                return phone;
            } else {
                System.out.println("Invalid phone number, use the +358 format and proper length");
            }
        }
        return phone;
    }
    public static String addressInput(String address) {
        Boolean validInput = false;
        Pattern addressPattern = Pattern.compile("[A-Z]{1}[a-z]{1,20}");
        while (!validInput) {
            address = System.console().readLine();
            Matcher addressMatch = addressPattern.matcher(address);
            if (addressMatch.matches()) {
                validInput = true;
                return address;
            } else {
                System.out.println("Invalid address");
            }
        }
        return address;
    }
    public static String emailInput(String email) {
        Boolean validInput = false;
        Pattern emailPattern = Pattern.compile("[A-Z]{1}[a-z]{1,20}");
        while (!validInput) {
            email = System.console().readLine();
            Matcher emailMatch = emailPattern.matcher(email);
            if (emailMatch.matches()) {
                validInput = true;
                return email;
            } else {
                System.out.println("Invalid e-mail.");
            }
        }
        return email;
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

//5.12.2023
//After so much fighting with the code, and very shoddy construction, I decided to start the build again
//from the starting draft. I renamed all files, classes, etc. more clearly to help make it more readable
//and moved all tenous functionality to the contactsApp class in order to make it easier to compile and
//execute the code.
//
//
//
//6.12.2023
//Great success! I finally have a good gameplan for the whole project and made major strides. The only points
//of concern and refining now is further control over the contacts and getting user validation done.
//I highly doubt I can make it for every single input, but at least the ID and both first and lastnames
//