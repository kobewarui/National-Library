package com.kelvin.nationallibraryproject.services;

import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Student;
import com.kelvin.nationallibraryproject.repositories.UserRepository;

@Service
public class StudentService {
	
	private final UserRepository userRepository;
	
	
	public StudentService(UserRepository userRepository) {
		
		this.userRepository=userRepository;
		
		
	}
	
public Student findStudentByEmail(String email) {
		
		Student student = (Student) userRepository.findUserByEmail(email);
		
		return student;
		
	}

}
