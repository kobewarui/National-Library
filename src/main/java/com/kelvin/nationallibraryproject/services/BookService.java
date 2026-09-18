package com.kelvin.nationallibraryproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelvin.nationallibraryproject.models.Author;
import com.kelvin.nationallibraryproject.models.Book;
import com.kelvin.nationallibraryproject.repositories.BookRepository;

@Service
public class BookService {
	
	BookRepository bookRepository;
	
	@Autowired
	public BookService(BookRepository bookRepository) {
		
		
		this.bookRepository = bookRepository;
		
	}

	public void registerNewBook(Book book) {
		bookRepository.save(book);
		
	}
	
public  List<Book> getBooks() {
		
		Iterable<Book> iter = bookRepository.findAll();
		
		return (List<Book>) iter;
	}

public Book findById(long theId) {

	return bookRepository.findById(theId);
}

public void deleteBookById(long theId) {
	bookRepository.deleteById(theId);
	
	
}

public Book findBookById(long theId) {
	
	
	return bookRepository.findBookById(theId);
	
}



}
