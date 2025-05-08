package org.siva.techblog.service;

import java.util.Optional;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Likes;
import org.siva.techblog.model.User;
import org.siva.techblog.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ILikeServiceImpl implements ILikeService {
	
	@Autowired
	private LikesRepository likeRepository;
	
	@Override
	public Likes saveLike(Likes like) {
		try {
			Likes save = likeRepository.save(like);
			return save;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Boolean listIsPresent(User user, Blog blog) {
		try {
			Boolean isPresent = likeRepository.existsByUserAndBlog(user, blog);
			if (isPresent) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Integer countLikesByBlog(Blog blog) {
		try {
			Integer count = likeRepository.countByBlog(blog);
			return count;
		} catch (Exception e) {
			return 0;
		}
	}
	
	@Override
	public Boolean deleteLike(User user, Blog blog) {
		try {
			Likes like = likeRepository.findByUserAndBlog(user, blog);
			likeRepository.delete(like);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Page<Likes> getAllLikes(int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 5);
			Page<Likes> page = likeRepository.findAll(pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Likes getLikesById(int id) {
		try {
			Optional<Likes> optional = likeRepository.findById(id);
			return optional.get();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean deleteLike(Likes like) {
		try {
			likeRepository.delete(like);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
