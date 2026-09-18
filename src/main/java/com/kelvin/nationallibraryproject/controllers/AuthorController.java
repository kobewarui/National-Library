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
import com.kelvin.nationallibraryproject.services.AdminService;



@Controller
public class AuthorController {
	
	
	AuthorService authorService;
	AdminService adminService;
	
	@Autowired
	public AuthorController(AuthorService authorService, AdminService adminService) {
		
		this.authorService = authorService;
		this.adminService=adminService;
	}
	
	@GetMapping("/newauthor")
	public String getAddAuthorPage(Model model,Authentication authentication) {
		
		Author author = new Author();
		model.addAttribute("author", author);
		String email = authentication.getName(); // Get the logged-in user's email
        Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        System.out.println(admin);
        model.addAttribute("admin",admin); //
		
		return"newauthor";
	}

	@PostMapping("author/save")
	public String createUser(Author author,Model model,Authentication authentication) {
		
		Admin admin =  adminService.findAdminByEmail(authentication.getName());
		author.setAdmin(admin);
		authorService.registerNewAuthor(author);
		
		
		return "redirect:/authors";
		
	}
	
	@GetMapping("author/update")
	public String displayAuthorUpdateForm(@RequestParam("id") long theId, Model model,Authentication authentication) {
		
		Author author = authorService.findById(theId);
		model.addAttribute("author",author);
		String email = authentication.getName(); // Get the logged-in user's email
		Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        System.out.println(admin);
        model.addAttribute("admin",admin); //
		
		
		
		return"newauthor";
	}
	
	@GetMapping("author/delete")
	public String deleteBookFromDatabase(@RequestParam("id") long theId, Model model,Authentication authentication) {
		
		authorService.deleteAuthorById(theId);
		String email = authentication.getName(); // Get the logged-in user's email
		Admin admin =  adminService.findAdminByEmail(email); // Retrieve user data based on email
        System.out.println(admin);
        model.addAttribute("admin",admin); //
		
	
		
		
		return "redirect:/authors";
	}
	
	
}
