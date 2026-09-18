package com.kelvin.nationallibraryproject.controllers;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.models.UserProfileForm;
import com.kelvin.nationallibraryproject.services.AdminService;
import com.kelvin.nationallibraryproject.utils.FileUploadUtil;

@Controller
public class ProfileController {

	private final AdminService adminService;

	public ProfileController(AdminService adminService) {

		this.adminService = adminService;
	}

	@GetMapping("profile")
	public String loadProfilePage(Model model, Authentication authentication) {

		model.addAttribute("profile", new UserProfileForm());

		Admin admin = adminService.findAdminByEmail(authentication.getName());
		model.addAttribute("admin", admin);

		return "profileadmin";
	}

	@PostMapping("profile/save")
	public String saveProfile(UserProfileForm profile,
			@RequestParam(value = "file", required = false) MultipartFile file,
			Model model, Authentication authentication) throws IOException {

		// The email column is unique, so a duplicate has to be caught here.
		// Letting it reach the database throws a raw ConstraintViolationException.
		if (adminService.emailAlreadyRegistered(profile.getEmail())) {

			model.addAttribute("profile", profile);
			model.addAttribute("admin", adminService.findAdminByEmail(authentication.getName()));
			model.addAttribute("error", "That email address is already registered. Please use a different one.");

			return "profileadmin";
		}

		User savedUser = adminService.registerNewUser(profile);

		if (file != null && !file.isEmpty()) {

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.saveFile(uploadDir, fileName, file);

			savedUser.setProfileImage(fileName);
			adminService.saveUser(savedUser);
		}

		return "redirect:/home";
	}

}
