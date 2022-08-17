package org.springframework.sample.batch.dto;

/**
 * Contains the information that's read from the CSV file.
 *
 */
public class VetsDTO {

	private String firstName;
	private String lastName;

	public VetsDTO() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
    @Override
    public String toString() {
        return "VetsDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
