package org.siva.techblog.service;

import java.util.List;

import org.siva.techblog.model.Admin;
import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.Likes;
import org.springframework.data.domain.Page;

public interface IBlogService {
	
	Blog saveBlog(Blog blog);
	
	Page<Blog> getAllBlogs(int pageNo);
	
	Blog getBlogById(int id);
	
	Boolean deleteBlog(Blog blog);
	
	List<Blog> getAllBlogs();
	
	List<Blog> getBlogsByCategory(String category);
	
	Page<Blog> getBlogsByAuthor(Admin admin, int pageNo);
	
	Page<Comment> getCommentsByAuthor(Admin admin, int pageNo);
	
	Page<Likes> getLikesByAuthor(Admin admin, int pageNo);

}
