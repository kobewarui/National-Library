package com.kelvin.nationallibraryproject.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.models.Borrow;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.repositories.BorrowRepository;

@Service
public class BorrowService {

	/** Rate the Add Borrow form starts on; the librarian can change it per loan. */
	public static final double DEFAULT_FINE_PER_DAY = 10.0;

	BorrowRepository borrowRepository;
	BookService bookService;

	@Autowired
	public BorrowService(BorrowRepository borrowRepository, BookService bookService) {

		this.borrowRepository = borrowRepository;
		this.bookService = bookService;

	}

	/**
	 * Saves the loan and marks the book as taken out. Returns false, and saves
	 * nothing, when no book was chosen or that copy is already on loan — the form
	 * hides unavailable books, but this is what actually enforces one loan per copy.
	 */
	public boolean registerNewBorrow(Borrow borrow) {

		Book book = borrow.getBook();

		if (book == null || book.isBorrowed()) return false;

		book.setBorrowed(true);
		bookService.registerNewBook(book);

		borrowRepository.save(borrow);

		return true;

	}

	public List<Borrow> getBorrows() {

		Iterable<Borrow> iter = borrowRepository.findAll();

		return (List<Borrow>) iter;
	}

	public Borrow findById(long theId) {

		return borrowRepository.findById(theId);
	}

	/** The books this member currently has out, for their own dashboard. */
	public List<Book> getBooksCurrentlyBorrowedBy(User member) {

		if (member == null) return List.of();

		return borrowRepository.findByMemberAndIsReturnedFalse(member)
				.stream()
				.map(Borrow::getBook)
				.filter(book -> book != null)
				.collect(Collectors.toList());
	}

	/** Closes the borrow record and puts the book back on the shelf. */
	public void returnBorrow(long theId) {

		Borrow borrow = borrowRepository.findById(theId);

		if (borrow == null) return;

		LocalDate today = LocalDate.now();

		borrow.setFine(calculateFine(borrow.getReturnDate(), today, borrow.getFinePerDay()));
		borrow.setReturned(true);
		borrow.setReturnDate(today);
		borrowRepository.save(borrow);

		Book book = borrow.getBook();

		if (book != null) {
			book.setBorrowed(false);
			bookService.registerNewBook(book);
		}

	}

	public void deleteBorrowById(long theId) {

		borrowRepository.deleteById(theId);

	}

	/** Nothing to pay until the due date has passed; after that it is this loan's rate per day. */
	public double calculateFine(LocalDate dueDate, LocalDate returnedOn, double finePerDay) {

		if (dueDate == null || returnedOn == null || !returnedOn.isAfter(dueDate)) return 0.0;

		long daysLate = ChronoUnit.DAYS.between(dueDate, returnedOn);

		return daysLate * finePerDay;
	}

	/** Loans where the book has not come back yet, newest due date last. */
	public List<Borrow> getOutstandingLoans() {

		return borrowRepository.findByIsReturnedFalse()
				.stream()
				.sorted((a, b) -> Long.compare(b.getDaysOverdue(), a.getDaysOverdue()))
				.collect(Collectors.toList());
	}

	/** Of the books still out, how many are past their due date. */
	public long countOverdueLoans() {

		return getOutstandingLoans().stream().filter(borrow -> borrow.getDaysOverdue() > 0).count();
	}

	/** Everything charged in fines so far, across all members. */
	public double getTotalFinesCharged() {

		return getBorrows().stream().mapToDouble(Borrow::getFine).sum();
	}

	/** What this member owes across every book they have returned late. */
	public double getTotalFinesFor(User member) {

		if (member == null) return 0.0;

		return borrowRepository.findByMember(member)
				.stream()
				.mapToDouble(Borrow::getFine)
				.sum();
	}

}
