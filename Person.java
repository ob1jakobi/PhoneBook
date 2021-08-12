package phonebook;

import java.util.Objects;

/**
 * Class required for making entries in the PhoneBook as part of Claim Academy project #1 for Java Full-
 * Stack Developer program.  Key aspects to a identify a Person for a PhoneBook are captured, including
 * the Person's first name, middle name(s) (if applicable), last name(s), phone number, as well as an
 * address.
 * @author Jake McGrath
 *
 */
public class Person implements Comparable<Person> {
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String phoneNumber;
	private Address address;


	public Person(String firstName, String middleName, String lastName, String phoneNumber, Address address) {
		this.firstName = titleCase(firstName);
		this.middleName = titleCase(middleName);
		this.lastName = titleCase(lastName);
		this.setFullName();
		this.setPhoneNumber(phoneNumber);
		this.address = address;
	}
	

	public Person(String firstName, String lastName, String phoneNumber, Address address) {
		this.firstName = titleCase(firstName);
		this.lastName = titleCase(lastName);
		this.setFullName();
		this.setPhoneNumber(phoneNumber);
		this.address = address;
	}


	public Person(String fullName, String phoneNumber, Address address) {
		this.fullName = fullName;
		this.setFirstMiddleLast();		// Ensures firstName, middleName, and lastName are initialized
		this.setPhoneNumber(phoneNumber);
		this.address = address;
	}
	
	/**
	 * Initializes fullName if either firstName and lastName have been initialized, or if firstName, middleName,
	 * and lastName have been initialized.  This method also updates the fullName field if there has been
	 * a change to any of the other three name fields via one of their respective setters.
	 */
	private void setFullName() {
		if (this.middleName == null) {
			this.middleName = "";
		}
		this.fullName = titleCase(lastName) + ", " + titleCase(firstName) +
				(middleName.isBlank() ? middleName : " " + titleCase(middleName));
	}

	/**
	 * Initializes firstName, middleName, lastName fields if fullName is the only name field that's been
	 * initialized.  This method also updates the three name fields if there has been a change to this
	 * Person's fullName field.
	 */
	private void setFirstMiddleLast() {
		// fullName = "Doe, John Michael West";
		// temp = {"Doe,", "John", "Michael", "West"};
		String[] temp = this.fullName.split(" ");
		String lastName = temp[0].substring(0, temp[0].length() - 1);
		String firstName = temp[1];
		String middleName = "";
		if (temp.length == 3) {		// there's only one middleName
			middleName = temp[2];
		} else if (temp.length > 3) {	// there's more than one middleName
			for (int i = 2; i < temp.length; i++) {
				middleName += " " + temp[i];
			}
			middleName = middleName.strip();
		}
		this.firstName = titleCase(firstName);
		this.middleName = (middleName.isBlank() ? middleName : titleCase(middleName));
		this.lastName = titleCase(lastName);
	}
	
	/**
     * Helper function to parse entries from the {@code public void addEntry(String newEntry)} method.
     * @param name a {@code String} representation of a name.  Should be formatted as follows:<br>
     * "FirstName MiddleName(s) LastName".
     * @return a {@code String} representation of the full name, now formatted in a manner
     * that can be used as argument in a Person constructor that accepts the full name as a
     * parameter.
     */
    protected static String parseName(String name) {
        assert name != null;
        String firstName = null;
        StringBuilder middleName = new StringBuilder();
        String lastName = null;
        String[] temp = name.split(" ");
        for (int i = 0; i < temp.length; i++) {
            if (i == 0) {                   // first element is first name
                firstName = temp[0];
            } else if (i == temp.length - 1) {  // last element is the last name
                lastName = temp[i];
            } else if (temp.length == 2) {  // only first and last name
                middleName.append("");
            } else if (temp.length > 3) {   // more than one middle name
                middleName.append(" ").append(temp[i]);
            } else {                        // only one middle name
                middleName.append(" ").append(temp[i]);
            }
        }
        String mid = middleName.toString().strip();
        return titleCase(lastName) + ", " + titleCase(firstName) + (mid.isBlank() ? mid : " " + titleCase(mid));
    }
	
	/**
	 * Helper function that takes a name as a parameter, and returns the same name with the first letter
	 * capitalized.
	 * @param name a {@code String} representation of a Person's name (first, middle, or last).
	 * @return a {@code String} that is the same as the parameter, except the first letter is capitalized.
	 */
	protected static String titleCase(String name) {
		String result = "";
		char title = Character.toTitleCase(name.charAt(0));
		return result += title + name.substring(1);
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.setFullName();
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		this.setFullName();
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.setFullName();
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
		this.setFirstMiddleLast();
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
		assert phoneNumber != null;
		if (phoneNumber.length() != 10 && phoneNumber.length() != 14) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number");
		}
		String result = "(";
		for (int i = 0; i < phoneNumber.length(); i++) {
			char temp = phoneNumber.charAt(i);
			if (result.length() == 3 && Character.isDigit(temp)) {
				result += temp +")-";
			} else if (result.length() == 8 && Character.isDigit(temp)) {
				result += temp + "-";
			} else if (Character.isDigit(temp)) {
				result += temp;
			}
		}
		if (result.length() != 14) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number");
		}
		this.phoneNumber = result;
	}
	
	protected static String phoneNumberFormatter(String phoneNumber) throws InvalidPhoneNumberException {
		assert phoneNumber != null;
		if (phoneNumber.length() != 10 && phoneNumber.length() != 14) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number");
		}
		String result = "(";
		for (int i = 0; i < phoneNumber.length(); i++) {
			char temp = phoneNumber.charAt(i);
			if (result.length() == 3 && Character.isDigit(temp)) {
				result += temp +")-";
			} else if (result.length() == 8 && Character.isDigit(temp)) {
				result += temp + "-";
			} else if (Character.isDigit(temp)) {
				result += temp;
			}
		}
		if (result.length() != 14) {
			throw new InvalidPhoneNumberException("Please enter a valid phone number");
		}
		return result;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	@Override
	public int compareTo(Person o) {
		String name1 = this.fullName;
		String name2 = o.fullName;
		return name1.compareToIgnoreCase(name2);
	}

	@Override
	public String toString() {
		if (middleName.isBlank()) {
			return firstName + " " +
					lastName + ", " +
					address.getFullAddress() + ", " +
					phoneNumber;
		} else {
			return firstName + " " +
					middleName + " " +
					lastName + ", " +
					address.getFullAddress() + ", " +
					phoneNumber;
		}
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(fullName, phoneNumber);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(fullName, other.fullName) && Objects.equals(phoneNumber, other.phoneNumber);
	}

}

/**
 * Exception that is thrown whenever a phone number is too short, too long, or contains non-numeric digits
 * that are not parentheses or dashes.
 * @author Jake McGrath
 *
 */
class InvalidPhoneNumberException extends RuntimeException {

	private static final long serialVersionUID = -7545392567423781470L;
	
	public InvalidPhoneNumberException(String message) {
		super(message);
	}
}
