package org.siva.techblog.repository;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Likes;
import org.siva.techblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
	
	Boolean existsByUserAndBlog(User user, Blog blog);
	
	Likes findByUserAndBlog(User user, Blog blog);
	
	Integer countByBlog(Blog blog);

}
