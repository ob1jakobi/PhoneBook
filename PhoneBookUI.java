package phonebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Client for the PhoneBook.  This program operates on the console, and has menus that the user can
 * navigate to add entries to the PhoneBook, delete entries from the PhoneBook, search for entries, and
 * update contacts in the PhoneBook.  This program also allows the user the option to save their current
 * PhoneBook, as well as to load a previously saved PhoneBook.
 * @author Jake McGrath
 *
 */
public class PhoneBookUI {
	
	private PhoneBook phoneBook;	// the PhoneBook
	
	// No-arg default constructor for instantiating the PhoneBookClient/program.
	public PhoneBookUI() {
		phoneBook = new PhoneBook();
		this.run();
	}
	
	/**
	 * Starts the program, and provides the user with the option to load a previously saved PhoneBook.
	 * If a previous save exists, and the user chooses to load it, then all of the previously saved
	 * entries will be added to this PhoneBook.  Otherwise, the user will navigate to the Main Menu
	 */
	public void run() {
		Scanner in = new Scanner(System.in);
		System.out.println("**********************************************************");
		System.out.println("\t\tWelcome to the PhoneBook");
		System.out.println("**********************************************************");
		System.out.println("\nWould you like to open a previously previously saved PhoneBook?");
		try {
			System.out.print("\nEnter 'Yes' to open a saved PhoneBook or 'No' to proceed to the\n"
					+ "Main Menu: ");
			String openSave = in.nextLine().strip();
			if (openSave.equalsIgnoreCase("yes") || openSave.equalsIgnoreCase("y")) {
				this.loadPhoneBook();
			} else {
				this.mainMenu();
			}
		} catch (InputMismatchException e) {
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid response following the prompt");
            System.out.println("**********************************************************");
            this.mainMenu();
		}
		in.close();
	}
	
	/**
	 * Checks if a previously saved PhoneBook exists in the user's default home directory.  If a
	 * previous save exists, this function will incorporate the entries in the save file into this
	 * PhoneBook.  If no save exists, then a printed message will notify the user that no previous save
	 * exists.  Regardless as to whether a previous save exists or not, the user will then be navigated
	 * to the Main Menu.
	 */
	private void loadPhoneBook() {
		// Create directory
		String directory = System.getProperty("user.home");
		// Create fileName
		String fileName = "PhoneBook.txt";
		// Create absolute path to file
		String absolutePath = directory + File.separator + fileName;
		File pb = new File(absolutePath);
		// create temporary PhoneBook
		PhoneBook temp = new PhoneBook();
		if (pb.exists()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath));
				String entry = bufferedReader.readLine();
				while (entry != null) {
					temp.entryLoader(entry);
					entry = bufferedReader.readLine();
				}
				phoneBook = temp;
				System.out.println("\n**********************************************************");
	            System.out.println("\t\tPhoneBook successfully loaded");
	            System.out.println("**********************************************************");
			} catch (IOException e) {
				System.out.println("Something went wrong loading PhoneBook...");
			}
		}
		this.mainMenu();
	}
	
	/**
	 * Saves the content of this PhoneBook to the user's default home directory by creating a .txt file
	 * named "PhoneBook.txt".
	 */
	private void savePhoneBook() {
		// Create directory
		String directory = System.getProperty("user.home");
		// Create fileName
		String fileName = "PhoneBook.txt";
		// Create absolute path to file
		String absolutePath = directory + File.separator + fileName;
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath));
			String contents = this.phoneBook.toString();
			bufferedWriter.write(contents);
			bufferedWriter.close();
			System.out.println("\n**********************************************************");
            System.out.println("\t\tPhoneBook successfully saved");
            System.out.println("**********************************************************");
		} catch (IOException e) {
			System.out.println("Something went wrong saving PhoneBook...");
		}
	}
	
	/**
	 * The Main Menu for the PhoneBookClient/program.
	 */
	public void mainMenu() {
		Scanner in = new Scanner(System.in);
        System.out.println("\nPhoneBook Main Menu:");
        System.out.println("Please review the menu options and enter your selection:");
        System.out.println("Option 1:  Add a contact");
        System.out.println("Option 2:  Remove a contact");
        System.out.println("Option 3:  Update a contact");
        System.out.println("Option 4:  Search contacts");
        System.out.println("Option 5:  See all contacts");
        System.out.println("Option 6:  Exit the PhoneBook");
        System.out.printf("%nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.addContactMenu();
                case 2 -> this.deleteContactMenu();
                case 3 -> this.updateContactMenu();
                case 4 -> this.searchContactsMenu();
                case 5 -> this.printAllContacts();
                default -> this.exit();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.mainMenu();
        }
        in.close();
	}
	
	/**
	 * A sub-menu of the Main Menu; this menu allows the user the option to add contacts to this
	 * PhoneBook by (a) filling out each of the required fields individually to instantiate a Person; or
	 * (b) provide a single entry that allows the user to enter all of the required fields for
	 * instantiating a Person at one time.  If the user navigated to this sub-menu by accident, an option
	 * for returning to the Main Menu is also available.
	 */
	public void addContactMenu() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nAdd a new contact:");
        System.out.println("Please review the menu options and enter your selection");
        System.out.println("Option 1:  Add contact by entry fields");
        System.out.println("Option 2:  Add contact by full contact details");
        System.out.println("Option 3:  Back to main menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.addContactByFields();
                case 2 -> this.addContactFullDetails();
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.addContactMenu();
        }
        in.close();
    }
	
	/**
	 * One of two ways the user can add a contact to this PhoneBook.  Requires the user to enter all
	 * fields individually in order to instantiate a Person before adding them to this PhoneBook.
	 */
	public void addContactByFields() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nAdd a new contact -> add contact by entry fields");
		try {
			// initialize variables for Person and Address instantiation
			System.out.print("Enter the new contact's first name: ");
			String firstName = in.nextLine().replace(',', '\u0000').strip();	// remove unwanted commas and whitespace
			System.out.print("\nEnter the new contact's middle name (if none, leave blank): ");
			String middleName = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's last name: ");
			String lastName = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's phone number\n"
					+ "(0123456789 or (012)-345-6789): ");
			String phoneNumber = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's street address: ");
			String streetAddress = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's city of residence: ");
			String city = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's state: ");
			String state = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the new contact's zip code: ");
			String zipCode = in.nextLine().replace(',', '\u0000').strip();
			// instantiate Person and Address objects from variables above
			Address a1 = new Address(streetAddress, city, state, zipCode);
			Person p1 = new Person(firstName, middleName, lastName, phoneNumber, a1);
			System.out.println("\nIs this correct:");
			System.out.println(p1);
			System.out.print("\nEnter 'Yes' if correct, or 'No' to start over: ");
			String isCorrectPerson = in.nextLine();
			if (isCorrectPerson.equalsIgnoreCase("yes") || isCorrectPerson.equalsIgnoreCase("y")) {
				// Search phoneBook to prevent duplicate entries
				System.out.println("Searching for duplicate entries...");
				String fullName = lastName + ", " + firstName + (middleName.isBlank() ? "" : " " + middleName);
				Person[] duplicates = duplicateContactFinder(fullName, phoneNumber);
				duplicateContactHandler(duplicates, p1);
			} else {
				this.addContactMenu();
			}
		} catch (InputMismatchException e1) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter only letters, parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (IllegalArgumentException e2) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a value for each entry (exception for middle name).");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidPhoneNumberException e3) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidStateException e4) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid state (full name or two-letter abbrviation).");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidZipCodeException e5) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid 5-digit zip code.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		}
		in.close();
	}
	
	/**
	 * The alternative option for entering a new contact into this PhoneBook.  The user must provide all
	 * of the information required to instantiate a Person object in one entry field.
	 */
	public void addContactFullDetails() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nAdd New Contact -> Add Contact by Full Contact Details");
		System.out.println("Please enter contact information as follows:");
		System.out.println("'FirstName MiddleName LastName, Street Address, City, State, Zip Code, "
				+ "Phone Number'");
		System.out.println("Example: 'John Elmer Doe, 324 Main St, St Charles, MO, 63303, 8475390126'");
		System.out.println("(to return to the Add Contact menu, leave blank and hit 'Enter')");
		System.out.print("\nEnter Contact Details: ");
		try {
			String newContact = in.nextLine();
			if (!newContact.isBlank()) {
				String[] temp = newContact.split(", ");
				if (temp.length != 6) {						// ensure formatting is correct
					throw new IndexOutOfBoundsException();
				}
				// parse name
				String fullName = Person.parseName(temp[0]);
				// parse address
				String streetAddress = temp[1];
				String city = temp[2];
				String state = temp[3];
				String zipCode = temp[4];
				// parse phone number
				String phoneNumber = Person.phoneNumberFormatter(temp[5]);
				// Instantiate Person and Address objects from parsed information above
				Address a1 = new Address(streetAddress, city, state, zipCode);
				Person p1 = new Person(fullName, phoneNumber, a1);
				// Search for duplicate entries in this PhoneBook
				Person[] duplicates = duplicateContactFinder(fullName, phoneNumber);
				duplicateContactHandler(duplicates, p1);
			} else {
				this.addContactMenu();
			}
		} catch (InputMismatchException e1) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter only letters, parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (IllegalArgumentException e2) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a value for each entry (exception for middle name).");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidPhoneNumberException e3) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidStateException e4) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid state (full name or two-letter abbrviation).");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (InvalidZipCodeException e5) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid 5-digit zip code.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		} catch (IndexOutOfBoundsException e6) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter only letters, parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.addContactMenu();
		}
		in.close();
	}
	
	/**
	 * Helper function either of the two add entry options.  This function helps to prevent duplicate
	 * entries by searching this PhoneBook for a match based on the full name of the prospective new
	 * contact, as well as by searching this PhoneBook by phone number.  The result of this function is
	 * then passed to a separate helper function {@code duplicateContactHandler()}.
	 * @param fullName the {@code String} representation of the prospective contact's full name.
	 * @param phoneNumber the {@code String} representation of the prospective contact's
	 * @return
	 */
	private Person[] duplicateContactFinder(String fullName, String phoneNumber) {
		Person[] result = new Person[2];
        try {
            Person nameMatch = phoneBook.searchByFullName(fullName);
            result[0] = nameMatch;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No matching entries by full name");
        }
        try {
            Person numberMatch = phoneBook.searchByPhoneNumber(phoneNumber);
            result[1] = numberMatch;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No matching entries by phone number");
        }
        if (result[0] == null && result[1] == null) {
            return null;
        } else if (result[0] == result[1]) {
            result[0] = null;
            return result;
        } else {
            return result;
        }
	}
	
	/**
	 * This helper function receives the return result from the helper function {@code duplicateContactFinder()},
	 * then the user has the option to overwrite any current entry that is a match, or add the new contact despite
	 * there being a possible match.  If there is no duplicate entries found, then this function will add
	 * the new contact to this PhoneBook and prompt the user to see if they would like to enter another
	 * new contact or return to the Main Menu.
	 * @param duplicates an array of {@code Person} objects that are the result of the helper function
	 * {@code duplicateContactFinder()}.
	 * @param newPerson the prospective new contact to be added to this PhoneBook.
	 */
	private void duplicateContactHandler(Person[] duplicates, Person newPerson) {
		Scanner in = new Scanner(System.in);
		if (duplicates != null) {
			System.out.println("\nIt looks like there might already ba a contact(s) in this PhoneBook that"
					+ "match your entry: ");
			for (int i = 0; i < duplicates.length; i++) {
				if (duplicates[i] != null) {
					System.out.printf("Option %d: %s%n", i, duplicates[i]);
				}
			}
			System.out.print("\nWould you like to overwrite an entry?\nEnter 'Yes' to overwrite or 'No'"
					+ " to discard changes: ");
			try {
				String response = in.nextLine();
				if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
					System.out.print("\nEnter the option number next to the contact above to overwrite: ");
					int deleteContact = in.nextInt();
					overwritePerson(newPerson, duplicates[deleteContact]);
				}
			} catch (InputMismatchException e) {
				System.out.println("\n**********************************************************");
				System.out.println("Please enter a valid response following the prompt.");
				System.out.println("**********************************************************");
			}
		} else {
			System.out.println("\n**********************************************************");
            phoneBook.addEntry(newPerson);
            System.out.println("**********************************************************");
		}
		try {
			System.out.println("\nAdd another contact?  Enter 'Yes' to add a new contact, or 'No' to return to the main menu: ");
			String doAgain = in.nextLine();
	        if (doAgain.equalsIgnoreCase("yes") || doAgain.equalsIgnoreCase("y")) {
	            this.addContactMenu();
	        } else {
	            this.mainMenu();
	        }
		} catch (InputMismatchException e) {
			System.out.println("\n**********************************************************");
            phoneBook.addEntry(newPerson);
            System.out.println("**********************************************************");
		}
		in.close();
	}
	
	/**
	 * Small helper function for "overwriting" a duplicate contact with a more current/up-to-date contact.
	 * @param newPerson the new contact to be added to this PhoneBook.
	 * @param oldPerson the old/duplicate contact to be removed from this PhoneBook.
	 */
	private void overwritePerson(Person newPerson, Person oldPerson) {
		phoneBook.deleteEntry(oldPerson);
		phoneBook.addEntry(newPerson);
	}
	
	/**
	 * A sub-menu of the Main Menu; this menu allows the user to decide if they'd like to delete a current
	 * entry/contact in this PhoneBook based on either the contact's name or phone number.  If the user
	 * navigated to this sub-menu on accident, there is an option to return to the Main Menu.
	 */
	public void deleteContactMenu() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nDelete Contact:");
        System.out.println("Please review the menu options and enter your selection:");
        System.out.println("Option 1:  Delete contact by phone number");
        System.out.println("Option 2:  Delete contact by name");
        System.out.println("Option 3:  Return to main menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.deleteContactByPhoneNumber();
                case 2 -> this.deleteContactByNameMenu();
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.deleteContactMenu();
        } 
        in.close();
    }
	
	/**
	 * Searches this PhoneBook for an entry that matches the 10-digit phone number entered by the user.
	 * If a match is found, the user can delete the contact.  If no match is found, the user is notified,
	 * and they are returned to the Delete Contact sub-menu.
	 */
	private void deleteContactByPhoneNumber() {
		Scanner in = new Scanner(System.in);
        System.out.println("\nDelete Contact -> Delete Contact by Phone Number");
        System.out.print("\nPlease enter the 10-digit phone number of the contact\n" +
                "you would like to delete (e.g., 0123456789 or (012)-345-6789): ");
        try {
        	String phoneNumber = in.nextLine();
        	phoneNumber = Person.phoneNumberFormatter(phoneNumber);
        	Person match = phoneBook.searchByPhoneNumber(phoneNumber);
        	deleteContactHandler(match);
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter only parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
        } catch (InvalidPhoneNumberException e3) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
		}
        in.close();
	}
	
	/**
	 * A sub-menu of the Delete contact sub-menu.  The user is given the option to search for the 
	 * contact's full name by entering each field individually, or by entering the contact's full name.
	 * If the user navigated to this sub-menu accidentally, the user can choose to navigate back to the
	 * Main Menu.
	 */
	private void deleteContactByNameMenu() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nDelete a Contact -> Delete Contact by Name");
        System.out.println("Please review the menu options and enter your selection:");
        System.out.println("Option 1:  Enter each name field individually");
        System.out.println("Option 2:  Enter full name");
        System.out.println("Option 3:  Return to Main Menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.deleteContactNameFields();
                case 2 -> this.deleteContactByFullName();
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {		// incorrect input type
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.addContactMenu();
        }
        in.close();
	}
	
	/**
	 * One of the two options for deleting a current contact by name.  This function requires the user to
	 * enter each of the name fields of the contact to be deleted from this PhoneBook in order to search
	 * this PhoneBook for a match.
	 */
	private void deleteContactNameFields() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nDelete Contact -> Delete Contact by Name -> Enter Fields Individually");
		System.out.print("\nEnter the Person's first name: ");
		try {
			String firstName = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("\nEnter the Person's middle name (leave blank and\n"
					+ "hit 'Enter' if no middle name): ");
			String middleName = in.nextLine().replace(',', '\u0000').strip();
			System.out.print("Enter the Person's last name: ");
			String lastName = in.nextLine().replace(',', '\u0000').strip();
			if (firstName.isBlank() || lastName.isBlank()) {	// incorrect/empty input
				throw new IllegalArgumentException();
			} else {
				String fullName = Person.titleCase(lastName) + ", " + Person.titleCase(lastName) + 
						(middleName.isBlank() ? "" : " " + Person.titleCase(middleName));
				Person match = phoneBook.searchByFullName(fullName);
				deleteContactHandler(match);
			}
		} catch (InputMismatchException e1) {		// incorrect input type
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompts.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompts.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
		}
		in.close();
	}
	
	/**
	 * The alternative name option for deleting a contact from this PhoneBook.  The user must enter the
	 * contact's full name in order to search this PhoneBook for the contact prior to deletion.
	 */
	private void deleteContactByFullName() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nDelete Contact -> Delete Contact by Name -> Delete by Full Name");
		System.out.println("Please enter the full name of the contact you'd like to delete.");
		System.out.println("Example: 'John Robert Doe'");
		System.out.print("\nEnter contact's full name: ");
		try {
			String fullName = in.nextLine().replace(',', '\u0000').strip();	// remove unwanted commas and whitespace
			if (fullName.isBlank()) {
				throw new IllegalArgumentException();
			} else {
				fullName = Person.parseName(fullName);
				Person match = phoneBook.searchByFullName(fullName);
				deleteContactHandler(match);
			}
		} catch (InputMismatchException e1) {		// incorrect input type
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompts.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompts.");
			System.out.println("**********************************************************");
			this.deleteContactMenu();
		}
		in.close();
	}
	
	/**
	 * Helper function for handling entries that are to be deleted from this PhoneBook.  The user must
	 * enter the word "delete" to ensure they are aware that they are deleting a contact from this
	 * PhoneBook permanently.
	 * @param match
	 */
	private void deleteContactHandler(Person match) {
		Scanner in = new Scanner(System.in);
		if (match == null) {
			System.out.println("\nThere are no matching entries with that name.");
			System.out.println("Would you like to try again with different contact info?");
			System.out.println("Enter 'Yes' to try again, or 'No' to return to main menu.");
			System.out.print("\nTry again? ");
			try {
				String tryAgain = in.nextLine();
				if (tryAgain.equalsIgnoreCase("yes") || tryAgain.equalsIgnoreCase("y")) {
					this.deleteContactMenu();
				} else {
					this.mainMenu();
				}
			} catch (InputMismatchException e) {
				System.out.println("\n**********************************************************");
				System.out.println("Please enter a valid response following the prompt.");
				System.out.println("**********************************************************");
				this.deleteContactMenu();
			}
		} else {
			System.out.println("\nA matching entry was found.");
			System.out.println("Would you like to delete the entry below?");
			System.out.println(match);
			System.out.println("\nTo delete the above entry type 'Delete', otherwise hit 'Enter'");
			System.out.print("to return to the main menu: ");
			try {
				String confirmDelete = in.nextLine();
				if (confirmDelete.equalsIgnoreCase("delete")) {
					System.out.println("\n**********************************************************");
	                phoneBook.deleteEntry(match);
	                System.out.println("**********************************************************");
	                deleteAgainOrNotHandler();
				} else {
					this.mainMenu();
				}
			} catch (InputMismatchException e) {
				System.out.println("\n**********************************************************");
				System.out.println("Please enter a valid response following the prompt.");
				System.out.println("**********************************************************");
				this.deleteContactMenu();
			}
		}
		in.close();
	}
	
	/**
	 * A helper function that asks the user if they would like to delete another entry from this
	 * PhoneBook.
	 */
	private void deleteAgainOrNotHandler() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nWould you like to delete another entry?");
		System.out.print("\nEnter 'Yes' to return to Delete Contact Menu or 'No'\n"
				+ "to return to the Main Menu: ");
		try {
            String response = in.nextLine();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                this.deleteContactMenu();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompt.");
			System.out.println("**********************************************************");
			this.mainMenu();
        }
		in.close();
	}
	
	/**
	 * A sub-menu of the Main Menu.  This menu allows the user to choose whether or not they would like
	 * to update a current contact in this PhoneBook by the contact's name or by their phone number. If
	 * the user had navigated to this sub-menu by accident, they have the option to return to the Main
	 * Menu.
	 */
	public void updateContactMenu() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nUpdate Contact Menu:");
        System.out.println("Please review the menu options and enter your selection:");
        System.out.println("Option 1:  Update contact by full name");
        System.out.println("Option 2:  Update contact by phone number");
        System.out.println("Option 3:  Return to main menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.updateContactByFullName();
                case 2 -> this.updateContactByPhone();
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.updateContactMenu();
        }
	}
	
	private void updateContactByFullName() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nUpdate Contact -> Update Contact by Full Name");
        System.out.println("Please enter the full name of the contact you'd like to update:");
        System.out.println("Example:  'John Smith Doe'");
        System.out.print("\nContact's Full Name: ");
        try {
        	String fullName = in.nextLine().replace(',', '\u0000').strip();
        	if (fullName.isBlank()) {
        		throw new IllegalArgumentException();
        	} else {
        		fullName = Person.parseName(fullName);
        		Person match = phoneBook.searchByFullName(fullName);
        		updateContactHandler(match);
        	}
        } catch (Exception e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompt.");
			System.out.println("**********************************************************");
			this.updateContactMenu();
        } 
        in.close();
	}
	
	private void updateContactByPhone() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nUpdate Contact -> Update by Phone Number");
        System.out.print("\nEnter the 10-digit phone number of the contact you'd like to update\n" +
                "(Example: '9706881234' or '(970)-688-1234'): ");
        try {
            String phoneNumber = in.nextLine();
            phoneNumber = Person.phoneNumberFormatter(phoneNumber);		// ensures number formatted correctly
            Person match = phoneBook.searchByPhoneNumber(phoneNumber);
            updateContactHandler(match);
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter only parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.updateContactByPhone();
        } catch (InvalidPhoneNumberException e1) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.updateContactByPhone();
        }
        in.close();
    }
	
	private void updateContactHandler(Person match) {
		Scanner in = new Scanner(System.in);
		if (match == null) {
            System.out.printf("%nThere are no matching entries with that name%n");
            System.out.println("Would you like to try again with a different name?");
            System.out.print("\nEnter 'Yes' to try again, or 'No' to return to the main menu: ");
            try {
                String response = in.nextLine();
                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                    this.updateContactMenu();
                } else {
                    this.mainMenu();
                }
            } catch (InputMismatchException e) {
            	System.out.println("\n**********************************************************");
    			System.out.println("Please enter a valid response following the prompt.");
    			System.out.println("**********************************************************");
                this.updateContactMenu();
            }
        } else {
            System.out.println("\nA matching entry was found:");
            System.out.println(match);
            System.out.println("\nWould you like to update the contact above?");
            System.out.print("\nEnter 'Yes' to update or 'No' to return to the main menu: ");
            try {
                String response2 = in.nextLine();
                if (response2.equalsIgnoreCase("yes") || response2.equalsIgnoreCase("y")) {
                    this.updateContactDetailsMenu(match);
                } else {
                    this.mainMenu();
                }
            } catch (InputMismatchException ex) {
            	System.out.println("\n**********************************************************");
    			System.out.println("Please enter a valid response following the prompt.");
    			System.out.println("**********************************************************");
                this.updateContactMenu();
            }
        }
		in.close();
	}
	
	private void updateContactDetailsMenu(Person match) {
		Scanner in = new Scanner(System.in);
        System.out.println("\nUpdate Contact -> Update Contact Details Menu");
        System.out.println("Please review the menu options and enter your selection");
        System.out.println("Option 1:  Update contact name");
        System.out.println("Option 2:  Update contact address");
        System.out.println("Option 3:  Update contact phone number");
        System.out.println("Option 4:  Return to main menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.updateContactName(match);
                case 2 -> this.updateContactAddress(match);
                case 3 -> this.updateContactPhone(match);
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            this.updateContactDetailsMenu(match);
        }
        in.close();
	}
	
	private void updateContactName(Person match) {
		Scanner in = new Scanner(System.in);
		System.out.println("\nUpdate Contact -> Update Contact Details Menu -> Update Name");
        System.out.println("Please follow the prompts below");
        System.out.print("\nPlease enter the contact's first name: ");
        try {
        	String firstName = in.nextLine().replace(',', '\u0000').strip();
        	System.out.print("\nPlease enter the contact's middle name (or leave blank and\n"
        			+ "hit 'Enter' if none): ");
        	String middleName = in.nextLine().replace(',', '\u0000').strip();
        	System.out.print("\nPlease enter the contact's last name: ");
        	String lastName = in.nextLine().replace(',', '\u0000').strip();
        	if (firstName.isBlank() || lastName.isBlank()) {
        		throw new IllegalArgumentException();
        	}
        	String fullName = Person.titleCase(lastName) + ", " + Person.titleCase(firstName) + 
        			(middleName.isBlank() ? "" : " " + Person.titleCase(middleName));
        	System.out.println("\nIs the name below correct?");
        	System.out.println(fullName);
        	System.out.print("\nEnter 'Yes' if correct or 'No' to try again. ");
        	String isCorrect = in.nextLine().strip();
        	if (isCorrect.equalsIgnoreCase("yes") || isCorrect.equalsIgnoreCase("y")) {
        		System.out.println("\n**********************************************************");
                match.setFullName(fullName);
                System.out.println("Name update completed successfully");
                System.out.println("**********************************************************");
                this.mainMenu();
        	} else {
        		this.updateContactName(match);
        	}
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompt.");
			System.out.println("**********************************************************");
			this.updateContactName(match);
        } catch (IllegalArgumentException e1) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a value for each entry (exception for middle name).");
			System.out.println("**********************************************************");
			this.updateContactName(match);
        }
        in.close();
	}
	
	private void updateContactAddress(Person match) {
        Scanner in = new Scanner(System.in);
        System.out.println("\nUpdate Contact -> Update Contact Details Menu -> Update Address");
        System.out.println("Please follow the prompts below:");
        try {
            System.out.print("\nPlease enter the contact's street address\n" +
                    "(Example: '123 State Street'): ");
            String streetAddress = in.nextLine().strip().replace(',', '\u0000');
            System.out.print("\nPlease enter the contact's city\n" +
                    "(Example: 'Chicago'): ");
            String city = in.nextLine().strip().replace(',', '\u0000');
            System.out.print("\nPlease enter the contact's state, using the two-letter abbreviation\n" +
                    "(Example: 'IL'): ");
            String state = in.nextLine().strip().replace(',', '\u0000');
            System.out.print("\nPlease enter the contact's 5-digit zip code\n" +
                    "(Example: '90210'): ");
            String zipCode = in.nextLine().strip().replace(',', '\u0000');
            Address newAddress = new Address(streetAddress, city, state, zipCode);
            System.out.println("\nIs the address below correct?");
            System.out.println(newAddress.getFullAddress());
            System.out.print("\nEnter 'Yes' if it's correct, or 'No' to try again: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                System.out.println("\n**********************************************************");
                match.setAddress(newAddress);
                System.out.println("Update address successful");
                System.out.println("**********************************************************");
                this.mainMenu();
            } else {
                this.updateContactAddress(match);
            }
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid response following the prompt.");
			System.out.println("**********************************************************");
			this.updateContactAddress(match);
        } catch (InvalidStateException e1) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid state (full name or two-letter abbrviation).");
			System.out.println("**********************************************************");
			this.updateContactAddress(match);
		} catch (InvalidZipCodeException e2) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid 5-digit zip code.");
			System.out.println("**********************************************************");
			this.updateContactAddress(match);
		}
        in.close();
    }
	
	private void updateContactPhone(Person match) {
        Scanner in = new Scanner(System.in);
        System.out.println("\nUpdate Contact -> Update Contact Details Menu -> Update Phone");
        System.out.println("Please follow the prompts below:");
        try {
            System.out.print("\nEnter a new 10-digit phone number\n" +
                    "(Example: '9706881234' or '(970)-688-1234'): ");
            String phoneNumber = in.nextLine();
            phoneNumber = Person.phoneNumberFormatter(phoneNumber);
            System.out.println("\nIs the phone number below correct?");
            System.out.println(phoneNumber);
            System.out.print("\nEnter 'Yes' if it's correct, or 'No' to try again: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                System.out.println("\n**********************************************************");
                match.setPhoneNumber(phoneNumber);
                System.out.println("Update phone number completed successfully");
                System.out.println("**********************************************************");
                this.mainMenu();
            } else {
                this.updateContactPhone(match);
            }
        } catch (InputMismatchException e) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter only parentheses, dashes, or numbers, as required.");
			System.out.println("**********************************************************");
			this.updateContactPhone(match);
        } catch (InvalidPhoneNumberException e1) {
        	System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.updateContactPhone(match);
        }
        in.close();
    }
	
	public void searchContactsMenu() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nSearch Contacts Menu:");
        System.out.println("Please review the menu options and enter your selection:");
        System.out.println("Option 1:  Search by first name");
        System.out.println("Option 2:  Search by last name");
        System.out.println("Option 3:  Search by full name");
        System.out.println("Option 4:  Search by telephone number");
        System.out.println("Option 5:  Search by city");
        System.out.println("Option 6:  Search by state");
        System.out.println("Option 7:  Return to main menu");
        System.out.print("\nPlease enter the option number to continue: ");
        try {
            int selection = in.nextInt();
            switch (selection) {
                case 1 -> this.searchByFirstName();
                case 2 -> this.searchByLastName();
                case 3 -> this.searchByFullName();
                case 4 -> this.searchByTelephoneNumber();
                case 5 -> this.searchByCity();
                case 6 -> this.searchByState();
                default -> this.mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n**********************************************************");
            System.out.println("Please enter a number corresponding to one of the options");
            System.out.println("**********************************************************");
            searchContactsMenu();
        }
        in.close();
    }
	
	private void searchByFirstName() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nSearch Contacts Menu -> Search by First Name");
		System.out.print("\nEnter the contact's first name: ");
		try {
			String firstName = in.nextLine().replace(',', '\u0000').strip();
			if (firstName.isBlank()) {
				throw new IllegalArgumentException();
			}
			firstName = Person.titleCase(firstName);
			Person[] matches = phoneBook.searchByFirstName(firstName);
			if (matches != null) {
                System.out.println("\n**********************************************************");
                for (Person p: matches) {
                    System.out.println(p);
                }
                System.out.println("**********************************************************");
            } else {
            	System.out.println("\n**********************************************************");
                System.out.printf("There were no matches for %s in this PhoneBook.%n", firstName);
                System.out.println("**********************************************************");
            }
            System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by first name again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByFirstName();
            } else {
                this.mainMenu();
            }
		} catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchByFirstName();
		} catch (ArrayIndexOutOfBoundsException e1) {		// out of bounds because Person.titleCase(firstName)
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchByFirstName();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchByFirstName();
		}
		in.close();
	}
	
	private void searchByLastName() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nSearch Contacts Menu -> Search by Last Name");
        System.out.print("\nEnter the contact's last name: ");
        try {
        	String lastName = in.nextLine().replace(',', '\u0000').strip();
        	lastName = Person.titleCase(lastName);
        	if (lastName.isBlank()) {
        		throw new IllegalArgumentException();
        	}
        	Person[] matches = phoneBook.searchByLastName(lastName);
        	if (matches != null) {
                System.out.println("\n**********************************************************");
                for (Person p: matches) {
                    System.out.println(p);
                }
                System.out.println("**********************************************************");
            } else {
            	System.out.println("\n**********************************************************");
                System.out.printf("There were no matches for %s in this PhoneBook.%n", lastName);
                System.out.println("**********************************************************");
            }
        	System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by last name again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByLastName();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchByLastName();
		} catch (ArrayIndexOutOfBoundsException e1) {		// out of bounds because Person.titleCase(lastName)
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchByLastName();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchByLastName();
		}
        in.close();
	}
	
	private void searchByFullName() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nSearch Contacts Menu -> Search by Full Name");
        System.out.println("Enter the contact's full name; if no middle name, just use first and last name.\n" +
                "(Example: 'FirstName MiddleName(s) LastName' or 'John Robert Doe')");
        System.out.print("Enter contact's full name: ");
        try {
            String fullName = in.nextLine();
            fullName = Person.parseName(fullName);
            if (fullName.isBlank()) {
            	throw new IllegalArgumentException();
            }
            Person match = phoneBook.searchByFullName(fullName);
            if (match != null) {
                System.out.println("\n**********************************************************");
                System.out.println("There's a match:");
                System.out.println(match);
                System.out.println("**********************************************************");
            } else {
            	System.out.println("\n**********************************************************");
                System.out.printf("It doesn't look like there's a match for '%s' in this PhoneBook...%n", fullName);
                System.out.println("**********************************************************");
            }
            System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by full name again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByLastName();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (ArrayIndexOutOfBoundsException e1) {		// out of bounds because Person.titleCase(fullName)
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		}
        in.close();
    }
	
	private void searchByTelephoneNumber() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nSearch Contacts Menu -> Search by Telephone Number");
        System.out.print("\nEnter the contact's 10-digit phone number\n" +
                "(Example: '0123456789' or '(012)-345-6789)'): ");
        try {
            String phoneNumber = in.nextLine();
            if (phoneNumber.isBlank()) {
            	throw new IllegalArgumentException();
            }
            phoneNumber = Person.phoneNumberFormatter(phoneNumber);
            Person match = phoneBook.searchByPhoneNumber(phoneNumber);
            if (match != null) {
                System.out.println("\n**********************************************************");
                System.out.println("There's a match:");
                System.out.println(match);
                System.out.println("**********************************************************");
            } else {
                System.out.println("\n**********************************************************");
                System.out.printf("It doesn't look like there's a match for '%s' in this PhoneBook...%n", phoneNumber);
                System.out.println("**********************************************************");
            }
            System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by phone number again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByTelephoneNumber();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (InvalidPhoneNumberException e1) {		// incorrect phone number (formatting or digits)
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid phone number, formatted as shown in prompt.");
			System.out.println("**********************************************************");
			this.searchContactsMenu();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		}
        in.close();
    }
	
	private void searchByCity() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nSearch Contacts Menu -> Search by City");
        System.out.print("\nEnter the contact's city\n" +
                "(Example: 'Saint Louis' or 'St. Louis)'): ");
        try {
            String city = in.nextLine().replace(',', '\u0000').strip();
            city = Person.titleCase(city);
            if (city.isBlank()) {
            	throw new IllegalArgumentException();
            }
            Person[] match = phoneBook.searchByCity(city);
            if (match != null) {
                System.out.println("\n**********************************************************");
                for (Person p: match) {
                    System.out.println(p);
                }
                System.out.println("**********************************************************");
            } else {
                System.out.println("\n**********************************************************");
                System.out.printf("It doesn't look like there's a match for '%s' in this PhoneBook...%n", city);
                System.out.println("**********************************************************");
            }
            System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by city again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByCity();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (ArrayIndexOutOfBoundsException e1) {		// out of bounds because Person.titleCase(city)
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (IllegalArgumentException e2) {		// empty input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a valid name following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		}
        in.close();
    }
	
	private void searchByState() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nSearch Contacts Menu -> Search by State");
        System.out.print("\nEnter the contact's State of residence\n" +
                "(Example: 'IL' or 'Illinois'): ");
        try {
            String state = in.nextLine().replace(',', '\u0000').strip();
            if (state.isBlank()) {
            	throw new IllegalArgumentException();
            }
            state = Address.stateFormatter(state);
            Person[] match = phoneBook.searchByState(state);
            if (match != null) {
                System.out.println("\n**********************************************************");
                for (Person p: match) {
                    System.out.println(p);
                }
                System.out.println("**********************************************************");
            } else {
                System.out.println("\n**********************************************************");
                System.out.printf("It doesn't look like there's a match for '%s' in this PhoneBook...%n", state);
                System.out.println("**********************************************************");
            }
            System.out.println("\nWould you like to search for a different contact?");
            System.out.print("\nEnter 'Yes' to search by State again, or 'No' to return\n" +
                    "to the main menu: ");
            String response = in.nextLine();
            if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("y")) {
                this.searchByState();
            } else {
                this.mainMenu();
            }
        } catch (InputMismatchException e) {	// invalid input
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		} catch (InvalidStateException e1) {
			System.out.println("\n**********************************************************");
			System.out.println("Please enter a valid state (full name or two-letter abbrviation).");
			System.out.println("**********************************************************");
			this.searchContactsMenu();
		} catch (IllegalArgumentException e3) {
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
            this.searchContactsMenu();
		}
        in.close();
    }
	
	public void printAllContacts() {
        if (phoneBook.isEmpty()) {
            System.out.println("\n**********************************************************");
            System.out.println("This PhoneBook is currently empty.");
            System.out.println("**********************************************************");
        } else {
            System.out.println("\n**********************************************************");
            phoneBook.printAllEntries();
            System.out.println("**********************************************************");
        }
        this.mainMenu();
    }
	
	public void exit() {
		Scanner in = new Scanner(System.in);
		System.out.println("\n**********************************************************");
		System.out.println("Before you leave, would you like to save this PhoneBook?");
		System.out.print("\nEnter 'Yes' to save or 'No' to exit: ");
		try {
			String saveBeforeExit = in.nextLine().strip();
			in.close();
			if (saveBeforeExit.equalsIgnoreCase("yes") || saveBeforeExit.equalsIgnoreCase("y")) {
				this.savePhoneBook();
			}
		} catch (InputMismatchException e) {
			System.out.println("\n**********************************************************");
            System.out.println("Please enter a a valid response following the prompt");
            System.out.println("**********************************************************");
		}
		System.out.println("\nThank you for using the PhoneBook.");
	}
	

	public static void main(String[] args) {
		PhoneBookUI test = new PhoneBookUI();
//		String directory = System.getProperty("user.home");
//		String fileName = "test.txt";
//		String absolutePath = directory + File.separator + fileName;
//		Address a1 = new Address("120 E. Cullerton St", "Chicago", "IL", "60616");
//		Address a2 = new Address("114 Market St", "St Louis", "MO", "63403");
//		Address a3 = new Address("324 Main St", "St Charles", "MO", "63303");
//		Address a4 = new Address("574 Pole Ave", "St Peters", "MO", "63333");
//		
//		Person p1 = new Person("McGrath, Jake Kimball", "7089164236", a1);
//		Person p2 = new Person("Doe, John", "6366435698", a2);
//		Person p3 = new Person("Doe, John E", "8475390126", a3);
//		Person p4 = new Person("Doe, John Michael West", "5628592375", a4);
//		
//		PhoneBook pb = new PhoneBook();
//		pb.addEntry(p1);
//		pb.addEntry(p2);
//		pb.addEntry(p3);
//		pb.addEntry(p4);
//		
//		try {
//			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(absolutePath));
//			String contents = pb.toString();
//			fileWriter.write(contents);
//			fileWriter.close();
//		} catch (IOException e) {
//			System.out.println(e.getLocalizedMessage());
//		}
//		
//		PhoneBook pb1 = new PhoneBook();
//		try {
//			BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath));
//			String entry = bufferedReader.readLine();
//			while (entry != null) {
//				pb1.entryLoader(entry);
//				entry = bufferedReader.readLine();
//			}
//		} catch (IOException e) {
//			System.out.println(e.getLocalizedMessage());
//		}
//		
//		System.out.println("Original PhoneBook: ");
//		pb.printAllEntries();
//		System.out.println("\nLoaded PhoneBook: ");
//		pb1.printAllEntries();
	}

}
