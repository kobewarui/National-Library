package com.kelvin.nationallibraryproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kelvin.nationallibraryproject.models.Author;
import com.kelvin.nationallibraryproject.models.Book;

public interface AuthorRepository extends CrudRepository<Author, Long>{
	
	Author findById(long theId);
	
	 void deleteById(long id);

}
