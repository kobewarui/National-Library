package com.kelvin.nationallibraryproject.services;

import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Professional;
import com.kelvin.nationallibraryproject.repositories.UserRepository;



@Service
public class ProfessionalService {
	
	private final UserRepository userRepository;
	
	public ProfessionalService(UserRepository userRepository) {
		
		this.userRepository=userRepository;
	}
	
	

	public Professional findProfessionalByEmail(String email) {
		
		return (Professional) userRepository.findUserByEmail(email);
	}

}
