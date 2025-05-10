package org.siva.techblog.repository;

import java.util.List;

import org.siva.techblog.model.Admin;
import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
	
	 List<Blog> findByCategory(String category);
	
	 Page<Blog> findByAdmin(Admin admin, Pageable pageable);
	
	 @Query("SELECT c FROM Comment c WHERE c.blog.admin = :admin")
	 Page<Comment> findCommentsByAdmin(Admin admin, Pageable pageable);
	 
	 @Query("SELECT l FROM Likes l WHERE l.blog.admin = :admin")
	 Page<Likes> findLikesByAdmin(Admin admin, Pageable pageable);
	 
	 List<Blog> findByTitleContainingIgnoreCase(String ch1);
	 	 
}
