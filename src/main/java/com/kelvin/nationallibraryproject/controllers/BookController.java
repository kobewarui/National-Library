package com.kelvin.nationallibraryproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.Author;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.services.AuthorService;
import com.kelvin.nationallibraryproject.services.BookService;
import com.kelvin.nationallibraryproject.services.AdminService;

@Controller
public class BookController {
	
AuthorService authorService;
BookService bookService;
AdminService adminService;


	
	@Autowired
	public BookController(AuthorService authorService,BookService bookService, AdminService adminService) {
		
		this.authorService = authorService;
		this.bookService = bookService;
		this.adminService=adminService;
	}
	
	@GetMapping("/newbook")
	public String getBookPage(Model model,Authentication authentication) {
		Book book = new Book();
		model.addAttribute("book", book);
		String email = authentication.getName(); // Get the logged-in user's email
        Admin userAdmin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
		List<Author> allAuthors = userAdmin.getListOfAuthors();
		model.addAttribute("allAuthors", allAuthors);
		
        System.out.println(userAdmin);
        model.addAttribute("userAdmin", userAdmin); //
		
		
		return"newbook";
	}
	
	@PostMapping("book/save")
	public String createBook(Book book,Model model,Authentication authentication) {
		
		Admin admin = adminService.findAdminByEmail(authentication.getName());
		book.setAdmin(admin);
		
		bookService.registerNewBook(book);
		
		
		return "redirect:/books";
		
	}
	
	@GetMapping("book/update")
	public String displayBookUpdateForm(@RequestParam("id") long theId, Model model,Authentication authentication) {
		
		Book book = bookService.findById(theId);
		model.addAttribute("book",book);
		List<Author> allAuthors = authorService.getAuthors();
		model.addAttribute("allAuthors", allAuthors);
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        System.out.println(admin);
        model.addAttribute("admin",admin); //
		
		
		return"newbook";
	}
	
	@GetMapping("book/delete")
	public String deleteBookFromDatabase(@RequestParam("id") long theId, Model model,Authentication authentication) {
		
		bookService.deleteBookById(theId);
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        System.out.println(admin);
        model.addAttribute("user",admin); //
		
		
		
		return "redirect:/books";
	}

	// Borrowing and returning now live in BorrowController (/newborrow and /borrows).
	// The old borrowbook.html / returnbook.html screens posted their date fields to
	// book/save bound to isbnNumber, which overwrote the book's ISBN, so they are gone.

}
