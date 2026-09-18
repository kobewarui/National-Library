package com.kelvin.nationallibraryproject.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Professional;
import com.kelvin.nationallibraryproject.models.Student;
import com.kelvin.nationallibraryproject.models.User;
import com.kelvin.nationallibraryproject.repositories.UserRepository;

@Service
public class UserService {
	
	
private final UserRepository userRepository;
	
	
	public UserService(UserRepository userRepository) {
		
		this.userRepository=userRepository;
		
		
	}
	
public User findUserByEmail(String email) {

		User user =  userRepository.findUserByEmail(email);

		return user;

	}

/** Only students and professionals borrow books; admins run the library. */
public List<User> getMembers() {

		return userRepository.findAll()
				.stream()
				.filter(user -> user instanceof Student || user instanceof Professional)
				.collect(Collectors.toList());

	}

}
