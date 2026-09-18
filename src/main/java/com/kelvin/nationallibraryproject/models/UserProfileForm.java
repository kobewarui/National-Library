package com.kelvin.nationallibraryproject.models;

/**
 * Form backing object for the "Add User" screen (profileadmin.html).
 *
 * The screen is not bound to an entity directly because the category the form
 * offers decides which {@link User} subclass actually gets persisted.
 */
public class UserProfileForm {

	private String firstName;
	private String lastName;
	private String category = "Admin";
	private String email;
	private String password;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
