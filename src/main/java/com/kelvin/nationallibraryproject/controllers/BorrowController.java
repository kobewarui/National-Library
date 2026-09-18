package com.kelvin.nationallibraryproject.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.models.Borrow;
import com.kelvin.nationallibraryproject.services.AdminService;
import com.kelvin.nationallibraryproject.services.BorrowService;
import com.kelvin.nationallibraryproject.services.UserService;

@Controller
public class BorrowController {

	BorrowService borrowService;
	AdminService adminService;
	UserService userService;

	@Autowired
	public BorrowController(BorrowService borrowService, AdminService adminService, UserService userService) {

		this.borrowService = borrowService;
		this.adminService = adminService;
		this.userService = userService;
	}

	@GetMapping("borrows")
	public String loadBorrowsPage(Model model, Authentication authentication) {

		Admin admin = adminService.findAdminByEmail(authentication.getName());

		model.addAttribute("borrowList", borrowService.getBorrows());
		model.addAttribute("admin", admin);

		return "borrows";
	}

	@GetMapping("/newborrow")
	public String getBorrowPage(@RequestParam(value = "bookId", required = false) Long bookId,
			Model model, Authentication authentication) {

		Admin admin = adminService.findAdminByEmail(authentication.getName());

		Borrow borrow = new Borrow();
		borrow.setBorrowDate(LocalDate.now());
		borrow.setReturnDate(LocalDate.now().plusWeeks(2));
		borrow.setFinePerDay(BorrowService.DEFAULT_FINE_PER_DAY);

		// Coming from the Borrow button on the books page, the book is already chosen.
		if (bookId != null) {
			borrow.setBook(adminBooks(admin).stream()
					.filter(book -> bookId.equals(book.getId()))
					.findFirst()
					.orElse(null));
		}

		model.addAttribute("borrow", borrow);
		model.addAttribute("allBooks", availableBooks(admin));
		model.addAttribute("allMembers", userService.getMembers());
		model.addAttribute("admin", admin);

		return "newborrow";
	}

	@PostMapping("borrow/save")
	public String createBorrow(Borrow borrow, Model model, Authentication authentication) {

		Admin admin = adminService.findAdminByEmail(authentication.getName());
		borrow.setAdmin(admin);

		// Rejected when the copy is already out, so a request that skips the form
		// cannot open a second loan on the same book.
		if (!borrowService.registerNewBorrow(borrow)) {

			model.addAttribute("borrow", borrow);
			model.addAttribute("allBooks", availableBooks(admin));
			model.addAttribute("allMembers", userService.getMembers());
			model.addAttribute("admin", admin);
			model.addAttribute("error", "That book is not available — it is already on loan.");

			return "newborrow";
		}

		return "redirect:/borrows";

	}

	@GetMapping("borrow/return")
	public String returnBorrowedBook(@RequestParam("id") long theId) {

		borrowService.returnBorrow(theId);

		return "redirect:/borrows";
	}

	@GetMapping("borrow/delete")
	public String deleteBorrowFromDatabase(@RequestParam("id") long theId) {

		borrowService.deleteBorrowById(theId);

		return "redirect:/borrows";
	}

	private List<Book> adminBooks(Admin admin) {

		return (admin == null || admin.getListOfBooks() == null) ? List.of() : admin.getListOfBooks();
	}

	/**
	 * Only books that are actually on the shelf can be lent. Without this the form
	 * would offer a book that is already out and create a second loan for it.
	 */
	private List<Book> availableBooks(Admin admin) {

		return adminBooks(admin).stream()
				.filter(book -> !book.isBorrowed())
				.collect(Collectors.toList());
	}

}
