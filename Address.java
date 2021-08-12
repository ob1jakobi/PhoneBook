package phonebook;

public class Address {
	
	// non-static fields
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	// static fields
	private static String states = "Alabama, Alaska, Arizona, Arkansas, California, Colorado, Connecticut, Delaware," +
            " District of Columbia, Florida, Georgia, Hawaii, Idaho, Illinois, Indiana, Iowa, Kansas, Kentucky," +
            " Louisiana, Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, Montana, Nebraska," +
            " Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina, North Dakota, Ohio, Oklahoma," +
            " Oregon, Pennsylvania, Rhode Island, South Carolina, South Dakota, Tennessee, Texas, Utah, Vermont," +
            " Virginia, Washington, West Virginia, Wisconsin, Wyoming";
    private static String[] allStates = states.split(", ");
    private static String statesAbb = "AL AK AZ AR CA CO CT DE DC FL GA HI ID IL IN IA KS KY LA ME MD MA MI MN MS MO" +
            " MT NE NV NH NJ NM NY NC ND OH OK OR PA RI SC SD TN TX UT VT VA WA WV WI WY";
    private static String[] stateAbbreviations = statesAbb.split(" ");

    
    
	public Address(String streetAddress, String city, String state, String zipCode) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.setState(state);
		this.setZipCode(zipCode);
	}
	
	
	
	public String getStreetAddress() {
		return streetAddress;
	}



	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = Person.titleCase(city);
	}



	public String getState() {
		return state;
	}


	public void setState(String state) throws InvalidStateException {
		state = state.toUpperCase();
		int index = -1;
		if (state.length() == 2 && statesAbb.contains(state)) {	// valid two-letter state abbreviation
			this.state = state;
			return;
		} else if (states.toUpperCase().contains(state)) {	// valid full state name
			for (int i = 0; i < allStates.length; i++) {
				String temp = allStates[i];
				if (temp.equalsIgnoreCase(state)) {
					index = i;							// convert full state name to two-letter abbreviation
				}
			}
		}
		if (index == -1) {
			throw new InvalidStateException("Please enter a valid state");
		}
		this.state = stateAbbreviations[index];
	}
	
	
	protected static String stateFormatter(String state) {
		state = state.toUpperCase();
		int index = -1;
		if (state.length() == 2 && statesAbb.contains(state)) {	// valid two-letter state abbreviation
			return state;
		} else if (states.toUpperCase().contains(state)) {	// valid full state name
			for (int i = 0; i < allStates.length; i++) {
				String temp = allStates[i];
				if (temp.equalsIgnoreCase(state)) {
					index = i;							// convert full state name to two-letter abbreviation
				}
			}
		}
		if (index == -1) {
			throw new InvalidStateException("Please enter a valid state");
		}
		return stateAbbreviations[index];
	}
	
	
	public String getZipCode() {
		return zipCode;
	}
	
	
	public void setZipCode(String zipCode) throws InvalidZipCodeException {
		if (zipCode.length() != 5) {
			throw new InvalidZipCodeException("Please enter a valid 5-digit zip code");
		}
		for (int i = 0; i < zipCode.length(); i++) {
			char temp = zipCode.charAt(i);
			if (!Character.isDigit(temp)) {
				throw new InvalidZipCodeException("Please enter a valid 5-digit zip code");
			}
		}
		this.zipCode = zipCode;
	}

	public String getFullAddress() {
		return streetAddress + ", " +
				city + ", " +
				state + ", " +
				zipCode;
	}

	public static void main(String[] args) {
		Address a1 = new Address("120 E. Cullerton St.", "Chicago", "illinois", "60616");
		System.out.println(a1.getFullAddress());
	}

}

class InvalidStateException extends RuntimeException {

	private static final long serialVersionUID = 32774640278969576L;

	public InvalidStateException(String message) {
		super(message);
	}
}

class InvalidZipCodeException extends RuntimeException {

	private static final long serialVersionUID = 3985645826221931361L;

	public InvalidZipCodeException(String message) {
		super(message);
	}	
}