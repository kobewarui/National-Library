package com.kelvin.nationallibraryproject.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professional")
public class Professional extends User {
	@Column(name = "professional_list_borrowed_books")
	@OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
	fetch = FetchType.LAZY, mappedBy = "admin")
	private List<Book> listOfBorrowedBooks;
	private  double FINE = 0.0;
	
	
	
	

	public List<Book> getListOfBorrowedBooks() {
		return listOfBorrowedBooks;
	}
	public void setListOfBorrowedBooks(List<Book> listOfBorrowedBooks) {
		this.listOfBorrowedBooks = listOfBorrowedBooks;
	}
	
	public double getFINE() {
		return FINE;
	}
	public void setFINE(double fINE) {
		FINE = fINE;
	}
	
	

}
