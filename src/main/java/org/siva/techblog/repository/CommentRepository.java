package org.siva.techblog.repository;

import java.util.List;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	Boolean existsByUserAndBlog(User user, Blog blog);
	
	List<Comment> findByBlog(Blog blog);
	
	Integer countByBlog(Blog blog);

}
