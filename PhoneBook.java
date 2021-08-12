package phonebook;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class PhoneBook {
	
	private Person[] entries;
	private int size;
	
	private static Comparator<Person> fullNameComp = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {	// "Doe, John" -> "Doe, John E"
			String p1 = o1.getFullName();
			String p2 = o2.getFullName();
			return p1.compareToIgnoreCase(p2);
		}
	};
	
	private static Comparator<Person> firstNameComp = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {
			String p1 = o1.getFirstName();
			String p2 = o2.getFirstName();
			return p1.compareToIgnoreCase(p2);
		}
	};
	
	private static Comparator<Person> phoneComp = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {
			String p1 = o1.getPhoneNumber();
			String p2 = o2.getPhoneNumber();
			return p1.compareTo(p2);
		}
	};
	
	private static Comparator<Person> cityComp = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {
			String p1 = o1.getAddress().getCity();
			String p2 = o2.getAddress().getCity();
			return p1.compareToIgnoreCase(p2);
		}
	};
	
	private static Comparator<Person> stateComp = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {
			String p1 = o1.getAddress().getState();
			String p2 = o2.getAddress().getState();
			return p1.compareTo(p2);
		}
	};
	
	
	
	public PhoneBook() {
		entries = new Person[2];	// entries = {null, null};	int[] nums = {0, 0}; 1 + 2 + 3 + 4 n(n+1)/2
		size = 0;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	private void resize(int capacity) {
		assert capacity >= size;
		entries = Arrays.copyOf(entries, capacity);
	}
	
	public void addEntry(Person newEntry) {
		assert newEntry != null;			// ensure the newEntry isn't null
		if (size == entries.length) {		// PhoneBook is full, so double its size
			resize(size * 2);
		}
		entries[size] = newEntry;
		size++;
		System.out.printf("Entry '%s' successfully added to this PhoneBook%n", newEntry.getFullName());
	}
	
	public void deleteEntry(Person unwantedPerson) throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("Cannot delete entries from an empty PhoneBook");
		}
		Arrays.sort(entries, 0, size, fullNameComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, unwantedPerson, fullNameComp);
			deleteEntryHandler(index);
			System.out.printf("%nEntry '%s' successfully deleted from this PhoneBook%n", unwantedPerson.getFullName());
		} catch (Exception e) {
			System.out.println("No such entry in this PhoneBook");
		}
	}
	
	private void deleteEntryHandler(int index) {
		entries[index] = entries[size - 1];		// swap Person to be deleted with last non-null Person
		entries[size - 1] = null;				// removes reference to swapped Person to avoid loitering
		size--;									// decrement size to account for deleted Person
		if (size > 0 && size == entries.length / 4) {	// shrinks PhoneBook, if needed
			resize(entries.length / 2);
		}
	}
	
	/**
	 * Performs an <em>exact</em> search of this PhoneBook for the Person object passed as a parameter.
	 * @param query a {@code Person} object.
	 * @return the Person object that matches the parameter {@code query}.  If no match is found in this
	 * PhoneBook, null is returned.
	 */
	public Person searchByPerson(Person query) {
		assert query != null;
		Arrays.sort(entries, 0, size, fullNameComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, query, fullNameComp);
			return entries[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Person searchByFullName(String fullNameQuery) {
		assert fullNameQuery != null;
		Person temp = new Person(fullNameQuery, "0123456789", null);
		return this.searchByPerson(temp);
	}
	
	public Person searchByPhoneNumber(String phoneNumberQuery) throws InvalidPhoneNumberException {
		assert phoneNumberQuery != null;
		phoneNumberQuery = Person.phoneNumberFormatter(phoneNumberQuery);
		Person temp = new Person("Dole, Bob", phoneNumberQuery, null);
		Arrays.sort(entries, 0, size, phoneComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, temp, phoneComp);
			return entries[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Person[] searchByFirstName(String firstNameQuery) {
		assert firstNameQuery != null;
		Person temp = new Person(firstNameQuery, "blah", "blah", "0123456789", null);
		Arrays.sort(entries, 0, size, firstNameComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, temp, firstNameComp);
			int startIndex = findFirstPersonIndex(index, firstNameQuery);
			return personMatchCrawler(startIndex, firstNameQuery);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Person[] searchByLastName(String lastNameQuery) {
		assert lastNameQuery != null;
		Person temp = new Person("Superman", lastNameQuery, "0123456789", null);
		Arrays.sort(entries, 0, size, fullNameComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, temp, fullNameComp);
			int startIndex = findFirstPersonIndex(index, lastNameQuery);
			return personMatchCrawler(startIndex, lastNameQuery);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Person[] searchByCity(String cityQuery) {
		assert cityQuery != null;
		Address a1 = new Address("Street", cityQuery, "WY", "00000");
		Person temp = new Person("first", "middle", "last", "0123456789", a1);
		Arrays.sort(entries, 0, size, cityComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, temp, cityComp);
			int startIndex = findFirstPersonIndex(index, cityQuery);
			return personMatchCrawler(startIndex, cityQuery);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Person[] searchByState(String stateQuery) {
		assert stateQuery != null;
		Address a1 = new Address("street", "city", stateQuery, "00000");
		Person temp = new Person("last, first", "0123456789", a1);
		Arrays.sort(entries, 0, size, stateComp);
		try {
			int index = Arrays.binarySearch(entries, 0, size, temp, stateComp);
			int startIndex = findFirstPersonStateIndex(index, stateQuery);
			return personMatchCrawlerState(startIndex, stateQuery);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private int findFirstPersonStateIndex(int index, String state) {
		int result = 0;
		if (index == -1) {
			return index;
		}
		for (int i = index; i >= 0; i--) {
			String test = entries[i].getAddress().getState();
			if (!test.contains(state)) {
				result = i + 1;
				break;
			}
		}
		return result;
	}
	
	private Person[] personMatchCrawlerState(int startIndex, String state) {
		Person[] result = new Person[2];
		int resultSize = 0;
		for (int i = startIndex; i < size; i++) {
			String test = entries[i].getAddress().getState();
			if (test.contains(state)) {
				result[resultSize] = entries[i];
				resultSize++;
			} else if (!test.contains(state)) {
				break;
			}
			if (resultSize == result.length) {
				result = resize(result, resultSize * 2);
			}
		}
		if (resultSize == 0) {
			System.out.println("No such entries exist in this PhoneBook.");
			return null;
		} else {
			result = resize(result, resultSize);
			return result;
		}
	}
	
	private int findFirstPersonIndex(int index, String query) {
		int result = 0;
		if (index == -1) {
			return index;
		}
		for (int i = index; i >= 0; i--) {
			String test = entries[i].toString().toUpperCase();
			if (!test.contains(query.toUpperCase())) {
				result = i + 1;
				break;
			}
		}
		return result;
	}
	
	private Person[] personMatchCrawler(int startIndex, String query) {
		Person[] result = new Person[2];
		int resultSize = 0;
		for (int i = startIndex; i < size; i++) {
			String test = entries[i].toString().toUpperCase();
			if (test.contains(query.toUpperCase())) {
				result[resultSize] = entries[i];
				resultSize++;
			} else if (!test.contains(query)) {
				break;
			}
			if (resultSize == result.length) {
				result = resize(result, resultSize * 2);
			}
		}
		if (resultSize == 0) {
			System.out.println("No such entries exist in this PhoneBook.");
			return null;
		} else {
			result = resize(result, resultSize);
			return result;
		}
	}
	
	private static Person[] resize(Person[] arr, int capacity) {
		assert capacity >= arr.length;
		return Arrays.copyOf(arr, capacity);
	}
	
	public void printAllEntries() {
		Arrays.sort(entries, 0, size, fullNameComp);
		for (int i = 0; i < size; i++) {
			System.out.println(entries[i]);
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Person p: entries) {
			if (p != null) {
				result.append(p + "\n");
			}
		}
		return result.toString();
	}
	
	protected void entryLoader(String newEntry) {
		String[] temp = newEntry.split(", ");
		String fullName = Person.parseName(temp[0]);
		String streetAddress = temp[1];
		String city = temp[2];
		String state = temp[3];
		String zipCode = temp[4];
		String phoneNumber = temp[5];
		Address a1 = new Address(streetAddress, city, state, zipCode);
		Person p1 = new Person(fullName, phoneNumber, a1);
		// add new entry
		if (size == entries.length) {		// PhoneBook is full, so double its size
			resize(size * 2);
		}
		entries[size] = p1;
		size++;
	}

	public static void main(String[] args) {
//		PhoneBook test = new PhoneBook();
//		
//		Address a1 = new Address("120 E. Cullerton St.", "Chicago", "IL", "60616");
//		Address a2 = new Address("114 Market St", "St Louis", "MO", "63403");
//		Address a3 = new Address("324 Main St", "St Charles", "MO", "63303");
//		Address a4 = new Address("574 Pole Ave", "St Peters", "MO", "63333");
//		Address a5 = new Address("120 E. Cullerton St.", "Chicago", "IL", "60616");
//		Address a6 = new Address("1600 Lilly Ave.", "Montrose", "AK", "59846");
//		
//		Person p1 = new Person("McGrath, Jake Kimball", "7089164236", a1);
//		Person p2 = new Person("Doe, John", "6366435698", a2);
//		Person p3 = new Person("Doe, John E", "8475390126", a3);
//		Person p4 = new Person("Doe, John Michael West", "5628592375", a4);
//		Person p5 = new Person("McGrath, Hannah Marie", "7086699976", a5);
//		Person p6 = new Person("Craft, Paul Jared Daniel", "9706880123", a6);
//		
//		test.addEntry(p1);
//		test.addEntry(p2);
//		test.addEntry(p3);
//		test.addEntry(p4);
//		test.addEntry(p5);
//		test.addEntry(p6);
		
//		System.out.println(test.searchByFullName("McGrath, Jake Kimball"));
//		System.out.println(test.searchByPerson(p1));
//		System.out.println(test.searchByPhoneNumber("7089164236"));
//		System.out.println(Arrays.toString(test.searchByFirstName("Jake")));
//		System.out.println(Arrays.toString(test.searchByLastName("Doe")));
//		System.out.println(Arrays.toString(test.searchByCity("St Louis")));
//		System.out.println(Arrays.toString(test.searchByState("IL")));
//		test.printAllEntries();
//		System.out.println(test.entries.length);
	}

}
