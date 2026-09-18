package com.kelvin.nationallibraryproject.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="borrow")
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
			fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	// The borrower is one of the registered users from the Add User screen.
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
			fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private User member;

	// ISO format is what <input type="date"> sends and expects back.
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name="borrow_date")
	private LocalDate borrowDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name="return_date")
	private LocalDate returnDate;

	@Column(name="is_returned")
	private boolean isReturned = false;

	/** Charged when the book comes back after its due date. */
	private double fine = 0.0;

	/** The rate this particular loan is fined at, set by the librarian when lending. */
	@Column(name="fine_per_day")
	private double finePerDay = 10.0;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Admin admin;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getMember() {
		return member;
	}
	public void setMember(User member) {
		this.member = member;
	}
	public LocalDate getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	public boolean isReturned() {
		return isReturned;
	}
	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
	public double getFine() {
		return fine;
	}
	public void setFine(double fine) {
		this.fine = fine;
	}
	public double getFinePerDay() {
		return finePerDay;
	}
	public void setFinePerDay(double finePerDay) {
		this.finePerDay = finePerDay;
	}

	/**
	 * How late a book that is still out has become. While a loan is open,
	 * returnDate holds the date the book is due back.
	 */
	@Transient
	public long getDaysOverdue() {

		if (isReturned || returnDate == null) return 0;

		return Math.max(ChronoUnit.DAYS.between(returnDate, LocalDate.now()), 0);
	}

	/** What the borrower would owe if the book came back today. */
	@Transient
	public double getFineSoFar() {

		return getDaysOverdue() * finePerDay;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}
