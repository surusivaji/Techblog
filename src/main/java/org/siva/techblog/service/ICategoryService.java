package org.siva.techblog.service;


import java.util.List;

import org.siva.techblog.model.Category;
import org.springframework.data.domain.Page;

public interface ICategoryService {
	
	Category saveCategory(Category category);
	
	Page<Category> getCategories(int pageNo);
	
	Category getCategoryById(int id);
	
	Boolean deleteCategory(Category category);
	
	List<Category> getActiveCategories();

}
