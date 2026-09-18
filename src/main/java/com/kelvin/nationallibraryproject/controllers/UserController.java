package com.kelvin.nationallibraryproject.controllers;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.models.Professional;
import com.kelvin.nationallibraryproject.models.Student;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.services.AdminService;
import com.kelvin.nationallibraryproject.services.BorrowService;
import com.kelvin.nationallibraryproject.services.ProfessionalService;
import com.kelvin.nationallibraryproject.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;



@Controller

public class UserController {
	@Autowired
	private AdminService adminService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private ProfessionalService professionalService;

	@Autowired
	private BorrowService borrowService;
	
	
	
	

	
	@GetMapping("user/register")
	public String loadRegisterPage(Model model)   {
		
		Admin admin = new Admin();
		model.addAttribute("admin", admin);
		
	    return "register";
		
	}
	
	@PostMapping("user/save")
	public String createUser(Admin admin,Model model) {

		// The email column is unique, so a duplicate has to be caught here.
		// Letting it reach the database throws a raw ConstraintViolationException.
		if (adminService.emailAlreadyRegistered(admin.getEmail())) {

			model.addAttribute("admin", admin);
			model.addAttribute("error", "That email address is already registered. Please log in instead.");

			return "register";
		}

		adminService.registerNewUser(admin);



		return "redirect:/user/login";

	}
	
	@GetMapping("user/login")
	public String loadLoginPage() {
		
		
		return"login";
		
	}
	
	/** Sends whoever just signed in to the dashboard that matches their role. */
	@GetMapping("/home")
	public String loadHomePage(Authentication authentication) {

		if (hasRole(authentication, "ROLE_STUDENT")) return "redirect:/home/student";
		if (hasRole(authentication, "ROLE_PROFESSIONAL")) return "redirect:/home/professional";

		return "redirect:/home/admin";

	}

	@GetMapping("/home/admin")
	public String loadAdminHomePage(Model model, Authentication authentication) {

		// The shared topbar/sidebar fragments read the logged-in account from "admin",
		// so every page rendering them has to put it in the model.
		Admin admin = adminService.findAdminByEmail(authentication.getName());
		model.addAttribute("admin", admin);

		return"home";

	}

	@GetMapping("/home/student")
	public String loadStudentHomePage(Model model, Authentication authentication) {

		// homestudent.html is built on the layoutstwo fragments, which read "user".
		Student student = studentService.findStudentByEmail(authentication.getName());
		model.addAttribute("user", student);
		addBorrowedBooks(model, student);

		return "homestudent";

	}

	@GetMapping("/home/professional")
	public String loadProfessionalHomePage(Model model, Authentication authentication) {

		Professional professional = professionalService.findProfessionalByEmail(authentication.getName());
		model.addAttribute("user", professional);
		addBorrowedBooks(model, professional);

		return "homeprofessional";

	}

	/** The member dashboards list whatever that member currently has on loan. */
	private void addBorrowedBooks(Model model, User member) {

		List<Book> borrowedBooks = borrowService.getBooksCurrentlyBorrowedBy(member);

		model.addAttribute("bookList", borrowedBooks);
		model.addAttribute("bookCount", borrowedBooks.size());
		model.addAttribute("totalFine", borrowService.getTotalFinesFor(member));

	}

	private boolean hasRole(Authentication authentication, String role) {

		return authentication != null && authentication.getAuthorities()
				.stream()
				.anyMatch(granted -> role.equals(granted.getAuthority()));
	}
	
	


	

}
