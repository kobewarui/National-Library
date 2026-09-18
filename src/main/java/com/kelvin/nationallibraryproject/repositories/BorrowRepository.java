package com.kelvin.nationallibraryproject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kelvin.nationallibraryproject.models.Borrow;
import com.kelvin.nationallibraryproject.models.User;

public interface BorrowRepository extends CrudRepository<Borrow, Long>{

	Borrow findById(long theId);

	void deleteById(long id);

	List<Borrow> findByMemberAndIsReturnedFalse(User member);

	List<Borrow> findByMember(User member);

	List<Borrow> findByIsReturnedFalse();

}
