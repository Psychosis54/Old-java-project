import java.io.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Class ContactsApp is the main class used for the functionalities of the app
 * @author Henri Nieminen
 * @param contactsList arraylist is used as a backbone for the contact management
 * @param contacts creates the file for saving contacts into. It will be created in the source directory.
 * @param oos is the objectOutputStream used to write into the file
 * @param ois is the objetInputStream used to read from the file and loads it into the arraylist
 * @param lister is a ListIterator object used to browse through the contactsList
 * @param found is a boolean value used for searching, editing and deleting. 
 * @param editIndex is an integer value used to keep track of which index should the editing methods edit. 
 * It's found in the select method and exists solely to keep the editing and deleting informed
 */
//I've supressed the warnings for the single unchecked operation
@SuppressWarnings("unchecked")
public final class ContactsApp implements Serializable {
    public static ArrayList<Contact> contactsList = new ArrayList<Contact>();//<--- This is where the unchecked casts warning is coming from
    public static File contacts = new File("contacts.txt");                  //"All of this 'just works' " -Todd Howard, 2016
    public static ObjectOutputStream oos;
    public static ObjectInputStream ois;
    public static ListIterator lister;
    public static boolean found;
    public static int editIndex;

    /**
     * main method is used for calling methods to activate different functions
     * @param args are unused.  
     * @throws Exception in accordance with the checkFile method to handle exceptions
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
     * userConfirm has a blank console readline only used to pace the program for the user
     */
    public static void userConfirm() {
        System.out.println("Press enter to continue");
        System.console().readLine();
    }
    /**
     * checkFile method is used to check through the saved file
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
                userConfirm();
            }
        }
    }
    /**
     * writeToFile method is used in editing and adding a contact
     * @param oos is used to write to the file
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
        }
    }
    /**
     * addContact is the method for adding contacts
     * @param id the id of the created Contact object
     * @param firstName the first name of the created Contact object
     * @param lastName the last name of the created Contact object
     * @param phoneNumber the phone number of the created Contact object
     * @param address the address of the created Contact object
     * @param email the email address of the created Contact object
     * @param contactsList is written here with the new added contact
     */
    public static void addContact() {
        /**
         * This method will use the input methods for user validation and then create
         * an object into the file using the writeToFile method.
         */
            boolean abortion = false;
            String id = "";
            String firstName = "";
            String lastName = "";
            String phoneNumber = "";
            String address = "";
            String email = "";

            while(!abortion) {
                System.out.println("Give the ID of the contact");
                id = userInput(id,1);
                if (checkExit(id)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("Give the first name");
                firstName = userInput(firstName,2);
                if (checkExit(firstName)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("Give the last name");
                lastName = userInput(lastName,3);
                if (checkExit(lastName)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("Give the phone number");
                phoneNumber = userInput(phoneNumber,4);
                if (checkExit(phoneNumber)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("(Opt.)Give the address");
                address = userInput(address,5);
                if (checkExit(address)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("(Opt.) Give the email");
                email = userInput(email,6);
                if (checkExit(email)) {
                    System.out.println("Addition aborted");
                    userConfirm();
                    break;
                }
                System.out.println("Contact added!");
                contactsList.add(new Contact(id, firstName, lastName, phoneNumber, address, email));
                writeToFile();
                System.out.println("-----------------");
                userConfirm();
                abortion = true;
            }
    }
    /**
     * readContact method is used to read from the filled arraylist
     * @param exit is used to control the loop for displaying the read menu.
     * @param readChoice is used to control the switch for different options
     * @param lister is reset to the first index here in order to function properly
     * @param contactsList is referred in listIterator objects method
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
                    userConfirm();
                    break;
                case "2":
                    lister = contactsList.listIterator(0);
                    select();
                    userConfirm();
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
     * viewAll method is used for viewing contacts loaded into the arraylist
     * 
     * @param capacity is the size taken from the arraylist for contacts. 
     * Used to determine if the arraylist is empty or the file is missing completely
     * @param lister is used here to go through the whole contact arraylist and print contacts out.
     * @param conactsList is used in conjunction with the listIterator object lister
     */
    public static void viewAll() {
        /**
         * The viewAll method will go through the arraylist where the contacts are saved and will
         * print them all out for the user to see
         */
        int capacity = contactsList.size();
        int indexTrack = 0;
        /**
         * It uses a listIterator object predefined classwide for use in other methods as well.
         * It also keeps track of the capacity of the list and will notify the user if the list
         * is empty or missing.
         */
        System.out.println("-------------------");
        while (lister.hasNext()) {
            System.out.println("Slot " + indexTrack + ": " + lister.next());
            indexTrack++;
        }
        if (capacity == 0) {
            System.out.println("Your list is empty, or the file is missing");
        }
        System.out.println("--------------------");
    }
    /**
     * select method is used for the user to search from the list
     * @param lister is used to go through the arraylist
     * @param capacity is used to check the size of the contactslist as in the viewAll method
     * @param searchName is used to search for the contact by its first name
     * @param searchInput is a Contact object that goes through the arraylist
     * @param exit is used to control the while loop for the console input
     * @param indexTrack is used to amount the index where the target contact is
     * @param countTrack is used to check how many contacts were logged into the editIndex
     * @param editIndex indexTrack is applied to this value
     */
    public static void select() {
        lister = contactsList.listIterator();
        int userSelect = 0;
        Console console = System.console();
        int capacity = contactsList.size();
        String searchName = "";
        //Index track will be used to see at which part of the arraylist the select method is at
        int indexTrack = 0;
        int countTrack = 0;
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
                System.out.println("Slot " + indexTrack + ": " + searchInput);
                editIndex = indexTrack;
                found = true;
                countTrack++;
                //countTrack keeps count of how many contacts were logged into the editIndex.
            }
            indexTrack++;
        }
        if (countTrack > 1) {
            boolean selected = false;
            System.out.println("--------------------");
            System.out.println("Which " + searchName + " do you want to select? Enter their slot on the list");
            while (!selected) {
                try {
                    editIndex = Integer.parseInt(console.readLine());
                    if (editIndex >= 0 && editIndex < capacity) {
                        selected = true;
                    } else {
                        System.out.println("That slot is out of range");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Give the slot number of which contact to select");
                }
            }
        }
        if (!found) {
            System.out.println("Contact not found.");
        }
        System.out.println("--------------------");
    }
    /**
     * deleteContact method is used for the user to delete something from the arraylist and file
     * @param Console is there to read the integer of the index given by the user
     * @param lister is reset to the first index of the arraylist
     * @param exit is used to control the while loop that asks the user which contact to delete
     * @param contactsList uses the remove method with the editIndex as a parameter
     */
    public static void deleteContact() {
        lister = contactsList.listIterator(0);
        boolean exit = false;
        viewAll();
        if (contactsList.size() == 0) {
            System.out.println("No contacts to delete. Press enter to continue");
            String userConfExit = System.console().readLine();
            exit = true;
        }
        select();
        while (!exit) {
            if (found) {
                System.out.println("Are you sure you want to delete this contact? y/n?");
                System.out.println(contactsList.get(editIndex));
                System.out.println("---------------");
                String userChoice = System.console().readLine();
                switch (userChoice.toLowerCase()) {
                    case "y":
                        contactsList.remove(editIndex);
                        writeToFile();
                        System.out.println("Contact deleted");
                        System.out.println("---------------");
                        userConfirm();
                        exit = true;
                        break;
                    case "n":
                        System.out.println("Deletion aborted");
                        System.out.println("---------------");
                        userConfirm();
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
    /**
     * editContact method is used for editing saved contacts in the arraylist and file
     * @param exit used to control the loop for editing
     * @param newId for inputting a new ID number
     * @param newFirst for inputting a new first name
     * @param newLast for inputting a new last name
     * @param newPhone for inputting a new phone number
     * @param newAddress for inputting a new address
     * @param newEmail for inputting a new email address
     */
    public static void editContact() {
        //Zeroes the listIterator index in order to not get stuck into one contact
        lister = contactsList.listIterator(0);
        boolean exit = false;
        viewAll();
        if (contactsList.size() == 0) {
            System.out.println("No contacts to edit. Press enter to continue");
            String userConfExit = System.console().readLine();
            exit = true;
        } else {
            select();
        }
        while (!exit) {
            //Excuse me while I puke. This looks gnarly
            if (found) {
                System.out.println("How do you want to edit this contact?");
                System.out.println(contactsList.get(editIndex));
                System.out.println("---------------");
                System.out.println("1. ID, 2. First name, 3. Last name," 
                + " 4. Phone number, 5. Address, 6. Email 7. Exit");
                
                String userChoice = System.console().readLine();
                switch (userChoice) {
                    case "1":
                        String newId = "";
                        System.out.println("Enter the new ID");
                        newId = userInput(newId,1);
                        if (checkExit(newId)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setId(newId);
                        writeToFile();
                        System.out.println("ID updated!");
                        System.out.println("---------------");
                        userConfirm();
                        break;
                    case "2":
                        String newFirst = "";
                        System.out.println("Enter the new first name");
                        newFirst = userInput(newFirst,2);
                        if (checkExit(newFirst)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setFirstName(newFirst);
                        writeToFile();
                        System.out.println("First name updated!");
                        System.out.println("---------------");
                        userConfirm();
                        break;
                    case "3":
                        String newLast = "";
                        System.out.println("Enter the new last name");
                        newLast = userInput(newLast,3);
                        if (checkExit(newLast)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setLastName(newLast);
                        writeToFile();
                        System.out.println("Last name updated!");
                        System.out.println("---------------");
                        userConfirm();
                        break;
                    case "4":
                        String newPhone = "";
                        System.out.println("Enter the new Phone number");
                        newPhone = userInput(newPhone,4);
                        if (checkExit(newPhone)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setPhoneNumber(newPhone);
                        writeToFile();
                        System.out.println("Phone number updated!");
                        System.out.println("---------------");
                        userConfirm();
                        break;
                    case "5":
                        String newAddress = "";
                        System.out.println("Enter the new Address");
                        newAddress = userInput(newAddress,5);
                        if (checkExit(newAddress)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setAddress(newAddress);
                        writeToFile();
                        System.out.println("Address updated!");
                        System.out.println("---------------");
                        userConfirm();
                        break;
                    case "6":
                        String newEmail = "";
                        System.out.println("Enter the new email");
                        newEmail = userInput(newEmail,6);
                        if (checkExit(newEmail)) {
                            System.out.println("Edit aborted");
                            userConfirm();
                            break;
                        }
                        contactsList.get(editIndex).setEmail(newEmail);
                        writeToFile();
                        System.out.println("Email updated!");
                        System.out.println("---------------");
                        userConfirm();
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
    //This looks really fucking stupid and is very stupid. I would have done this a lot better now with more experience
    public static String userInput(String anything, int selector) {
        Boolean validInput = false;
        Pattern idPattern = Pattern.compile("(?:[0-2]{1}[0-9]{1}|[3]{1}[0-1]{1})(?:[0]{1}[0-9]{1}|[1][0-2]{1})[0-9]"
        + "{2}[\\+|\\-|A|Y|X|W|V|U|B|C|D|E|F]{1}[0-9]{3}[A-Z0-9]{1}");
        Pattern firstNamePattern = Pattern.compile("[A-Z]{1}[a-zA-Z\\- ]{1,11}");
        Pattern lastNamePattern = Pattern.compile("[A-Z]{1}[a-zA-Z\\- ]{1,20}");
        Pattern phonePattern = Pattern.compile("\\+358(?:40|41|42|43|44|45|46|49|50)[0-9]{7}");
        Pattern addressPattern = Pattern.compile("[A-Z]{1}[a-zA-Z0-9 ]{1,40}|");
        Pattern emailPattern = Pattern.compile("[\\.a-z0-9]{1,30}+[\\@]{1}[a-z0-9]{2,8}[\\.]{1}[a-z]{1,6}|");

        switch (selector) {
                //Case one is for ID checking
            case 1:
                while (!validInput) {
                    anything = System.console().readLine();
                    Matcher idMatch = idPattern.matcher(anything);
                    if (idMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                        System.out.println("Invalid ID. Give a legitimate Finnish SSN");
                    }
                }
                break;
                //Case two is for first names
            case 2:
                while (!validInput) {
                    anything = System.console().readLine();
                    Matcher firstNameMatch = firstNamePattern.matcher(anything);
                    if (firstNameMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                        System.out.println("Invalid first name. Capitalize the first letter and keep the size between 2-12 letters");
                    }
                }
                break;
                //Case three is for last names
            case 3:
                while (!validInput) {
                    anything = System.console().readLine();
                    Matcher lastNameMatch = lastNamePattern.matcher(anything);
                    if (lastNameMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                        System.out.println("Invalid last name. Capitalize the first letter and keep the size between 2-20 letters");
                    }
                }
                break;
                //Case four is for phones
            case 4:
                while (!validInput) {
                    System.out.print(anything);
                    anything += System.console().readLine();
                    Matcher phoneMatch = phonePattern.matcher(anything);
                    if (phoneMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                        System.out.println("Invalid phone number, make sure it is the right length");
                        anything = "+358";
                    }
                }
                break;
                //Case five is for addresses
            case 5:
                while (!validInput) {
                    anything = System.console().readLine();
                    Matcher addressMatch = addressPattern.matcher(anything);
                    if (addressMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                        System.out.println("Invalid address. Please give a legitimate one, or leave it empty");
                    }
                }
                break;
                //Case six is for emails
            case 6:
                while (!validInput) {
                    anything = System.console().readLine();
                    Matcher emailMatch = emailPattern.matcher(anything);
                    if (emailMatch.matches()) {
                        validInput = true;
                        return anything;
                    } else if (checkExit(anything)) {
                        selector = 7;
                        break;
                    } else {
                    System.out.println("Invalid e-mail address. Type it in lowercase, or leave it empty");
                    }
                }
                //This looks like an empty case. No clue why it is there and afraid of deleting it in case it breaks
            case 7:
                break;
            default:
                System.out.println("How did you even get here?");
                break;
        }
        return anything;
    }
    //I don't even know what's the point of this. You can just check it where you need to XD
    public static boolean checkExit(String input) {
        if (input.equalsIgnoreCase("exit")) {
            return true;
        } else {
            return false;
        }
    }
}
