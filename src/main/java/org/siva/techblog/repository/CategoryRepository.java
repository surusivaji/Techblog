package org.siva.techblog.repository;

import java.util.List;

import org.siva.techblog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	List<Category> findByStatusTrue();

}
