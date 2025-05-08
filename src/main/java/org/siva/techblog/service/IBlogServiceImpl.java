package org.siva.techblog.service;

import java.util.List;
import java.util.Optional;

import org.siva.techblog.model.Admin;
import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.Likes;
import org.siva.techblog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IBlogServiceImpl implements IBlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Override
	public Blog saveBlog(Blog blog) {
		try {
			Blog save = blogRepository.save(blog);
			return save; 
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Page<Blog> getAllBlogs(int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 5);
			Page<Blog> page = blogRepository.findAll(pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Blog getBlogById(int id) {
		try {
			Optional<Blog> optional = blogRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	} 
	
	@Override
	public Boolean deleteBlog(Blog blog) {
		try {
			blogRepository.delete(blog);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public List<Blog> getAllBlogs() {
		try {
			List<Blog> blogs = blogRepository.findAll();
			return blogs;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Blog> getBlogsByCategory(String category) {
		try {
			List<Blog> blogs = blogRepository.findByCategory(category);
			return blogs;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Page<Blog> getBlogsByAuthor(Admin admin, int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 12);
			Page<Blog> page = blogRepository.findByAdmin(admin, pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Page<Comment> getCommentsByAuthor(Admin admin, int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 4);
			Page<Comment> page = blogRepository.findCommentsByAdmin(admin, pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Page<Likes> getLikesByAuthor(Admin admin, int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 4);
			Page<Likes> page = blogRepository.findLikesByAdmin(admin, pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}

}
