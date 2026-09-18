package com.kelvin.nationallibraryproject.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.Author;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.models.Professional;
import com.kelvin.nationallibraryproject.models.Student;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.services.AuthorService;
import com.kelvin.nationallibraryproject.services.BookService;
import com.kelvin.nationallibraryproject.services.BorrowService;
import com.kelvin.nationallibraryproject.services.ProfessionalService;
import com.kelvin.nationallibraryproject.services.StudentService;
import com.kelvin.nationallibraryproject.services.UserService;
import com.kelvin.nationallibraryproject.services.AdminService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class HomeController {

	private final AuthorService authorService;
	private final BookService bookService;
	private final AdminService adminService;
	private final StudentService studentService;
	private final ProfessionalService professionalService;
	private final UserService userService;
	private final BorrowService borrowService;

	public HomeController(UserService userService, AuthorService authorService, ProfessionalService professionalService,
			StudentService studentService, BookService bookService, AdminService adminService,
			BorrowService borrowService) {

		this.borrowService = borrowService;

		this.authorService = authorService;
		this.bookService = bookService;
		this.adminService = adminService;
		this.studentService = studentService;
		this.professionalService = professionalService;
		this.userService = userService;
	}

//	       


	@GetMapping("authors")
	public String loadAuthorsPage(Model model, Authentication authentication) {
		
		
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
		List<Author>authorList = admin.getListOfAuthors();
		model.addAttribute("authorList",authorList);
		
        System.out.println(admin);
        model.addAttribute("admin", admin); //
				
		return "authors";
		
		
	}

	@GetMapping("books")
	public String loadBooksPage(Model model,Authentication authentication) {
		
	//	List<Book> bookList = bookService.getBooks();
		
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        List<Book>bookList = admin.getListOfBooks();
        model.addAttribute("bookList",bookList);
        model.addAttribute("admin",admin); //
				
		return "books";
		
		
	}

	@GetMapping("report")
	public String loadTablePage(Model model ,Authentication authentication) {
		
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        model.addAttribute("admin", admin); // the shared topbar fragment reads "admin"

        // Everything here is counted from the existing records; the report stores nothing of its own.
        List<Book> allBooks = bookService.getBooks();
        long onLoan = allBooks.stream().filter(Book::isBorrowed).count();

        model.addAttribute("totalBooks", allBooks.size());
        model.addAttribute("booksOnLoan", onLoan);
        model.addAttribute("overdueCount", borrowService.countOverdueLoans());
        model.addAttribute("finesCharged", borrowService.getTotalFinesCharged());

        model.addAttribute("outstandingLoans", borrowService.getOutstandingLoans());

		return "report";
		
		
	}

}
