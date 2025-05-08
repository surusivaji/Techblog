package org.siva.techblog.service;


import java.util.List;
import java.util.Optional;

import org.siva.techblog.model.Category;
import org.siva.techblog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ICategoryServiceImpl implements ICategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		try {
			Category save = categoryRepository.save(category);
			return save;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Page<Category> getCategories(int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 8);
			Page<Category> page = categoryRepository.findAll(pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Category getCategoryById(int id) {
		try {
			Optional<Category> optional = categoryRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean deleteCategory(Category category) {
		try {
			categoryRepository.delete(category);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public List<Category> getActiveCategories() {
		try {
			List<Category> categories = categoryRepository.findByStatusTrue();
			return categories;
		} catch (Exception e) {
			return null;
		}
	}

}
