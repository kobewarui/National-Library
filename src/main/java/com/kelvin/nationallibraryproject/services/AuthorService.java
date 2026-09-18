package com.kelvin.nationallibraryproject.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Author;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.repositories.AuthorRepository;

@Service
public class AuthorService {
	
	AuthorRepository authorRepo;
	
	
	@Autowired
	public AuthorService(AuthorRepository authorRepo) {
		
		this.authorRepo = authorRepo;
		
	}


	public  void registerNewAuthor(Author author) {
	
		authorRepo.save(author);
	}
	
	public  List<Author> getAuthors() {
		
		Iterable<Author> iter = authorRepo.findAll();
		
		return (List<Author>) iter;
	}


	public Author findById(long theId) {
		
		return authorRepo.findById(theId);
	}


	public void deleteAuthorById(long theId) {
		
		authorRepo.deleteById(theId);
		
	}

}
