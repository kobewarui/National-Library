package com.kelvin.nationallibraryproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kelvin.nationallibraryproject.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findUserByEmail(String email);
	
	

}
