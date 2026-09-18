package com.kelvin.nationallibraryproject.models;

import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends User{
	
	 @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
				fetch = FetchType.LAZY, mappedBy = "admin")
	    private List<Book> listOfBooks;
	 
	 @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
				fetch = FetchType.LAZY, mappedBy = "admin")
	    private List<Author> listOfAuthors;
	 
	
	 
	 
	 public Admin(String firstName, String lastName, String email, String password) {
			
			super(firstName,lastName,email,password);
			
		}

	public Admin() {
		
		
	}
	 
	 
	public List<Book> getListOfBooks() {
		return listOfBooks;
	}
	public void setListOfBooks(List<Book> listOfBooks) {
		this.listOfBooks = listOfBooks;
	}
	public List<Author> getListOfAuthors() {
		return listOfAuthors;
	}
	public void setListOfAuthors(List<Author> listOfAuthors) {
		this.listOfAuthors = listOfAuthors;
	}
	
	

}
