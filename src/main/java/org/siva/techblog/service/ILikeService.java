package org.siva.techblog.service;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Likes;
import org.siva.techblog.model.User;
import org.springframework.data.domain.Page;

public interface ILikeService {
	
	Likes saveLike(Likes like);
	
	Boolean listIsPresent(User user, Blog blog);
	
	Integer countLikesByBlog(Blog blog);
	
	Boolean deleteLike(User user, Blog blog);
	
	Page<Likes> getAllLikes(int pageNo);
	
	Likes getLikesById(int id);
	
	Boolean deleteLike(Likes like);

}
