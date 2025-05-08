package org.siva.techblog.service;

import java.util.List;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.User;
import org.springframework.data.domain.Page;

public interface ICommentService {
	
	Comment saveComment(Comment comment);
	
	Boolean commentPresent(User user, Blog blog);
	
	List<Comment> getAllComments();
	
	List<Comment> getCommentsByBlog(Blog blog);
	
	Integer countCommentsByBlog(Blog blog);
	
	Page<Comment> getAllComments(int pageNo);
	
	Comment getCommentById(int id);
	
	Boolean deleteComment(Comment comment);

}
