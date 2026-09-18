package com.kelvin.nationallibraryproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kelvin.nationallibraryproject.models.Book;

public interface BookRepository extends CrudRepository<Book, Long>{
    
	
	Book findById(long theId);

	
	 void deleteById(long id);


    Book findBookById(long theId);
		
	

	

}
