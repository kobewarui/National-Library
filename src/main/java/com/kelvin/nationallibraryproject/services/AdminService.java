package com.kelvin.nationallibraryproject.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Admin;
import com.kelvin.nationallibraryproject.models.Professional;
import com.kelvin.nationallibraryproject.models.Role;
import com.kelvin.nationallibraryproject.models.Student;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.models.UserProfileForm;
import com.kelvin.nationallibraryproject.repositories.UserRepository;

@Service
public class AdminService implements UserDetailsService{

	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public AdminService(UserRepository userRepository) {

		this.userRepository =userRepository;

	}


	public boolean validateUser(User user) {



		return true;
	}

	public void registerNewUser(Admin admin) {


		Admin newAdmin = new Admin(admin.getFirstName(),admin.getLastName(),admin.getEmail(),
				passwordEncoder.encode(admin.getPassword()));

		newAdmin.setRole(Role.ADMIN);

		userRepository.save(newAdmin);

	}

	public boolean emailAlreadyRegistered(String email) {

		return userRepository.findUserByEmail(email) != null;
	}

	/**
	 * Creates the account described by the "Add User" form. The category chosen on
	 * the form decides which User subclass is persisted.
	 */
	public User registerNewUser(UserProfileForm form) {

		User user;
		String category = (form.getCategory() == null) ? "" : form.getCategory().trim();

		if ("Student".equalsIgnoreCase(category)) {
			user = new Student();
			user.setRole(Role.STUDENT);
		} else if ("Professional".equalsIgnoreCase(category)) {
			user = new Professional();
			user.setRole(Role.PROFESSIONAL);
		} else {
			user = new Admin();
			user.setRole(Role.ADMIN);
		}

		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));

		return userRepository.save(user);
	}

	public User saveUser(User user) {

		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(email);


		if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRole()));
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }


	}

	public Admin findAdminByEmail(String email) {

		User user = userRepository.findUserByEmail(email);

		return (user instanceof Admin) ? (Admin) user : null;

	}


	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {

		// Accounts created before the role column was populated default to ADMIN,
		// which is the only role the screens in this application are built around.
		Role effectiveRole = (role != null) ? role : Role.ADMIN;

		return List.of(new SimpleGrantedAuthority("ROLE_" + effectiveRole.name()));
	}

}
