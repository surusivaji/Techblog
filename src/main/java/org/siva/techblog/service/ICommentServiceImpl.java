package org.siva.techblog.service;

import java.util.List;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.User;
import org.siva.techblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ICommentServiceImpl implements ICommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public Boolean commentPresent(User user, Blog blog) {
		try {
			Boolean isPresent = commentRepository.existsByUserAndBlog(user, blog);
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
	public Comment saveComment(Comment comment) {
		try {
			Comment save = commentRepository.save(comment);
			return save;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Comment> getAllComments() {
		try {
			List<Comment> comments = commentRepository.findAll();
			return comments;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Comment> getCommentsByBlog(Blog blog) {
		try {
			List<Comment> comments = commentRepository.findByBlog(blog);
			return comments;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Integer countCommentsByBlog(Blog blog) {
		try {
			Integer count = commentRepository.countByBlog(blog);
			return count;
		} catch (Exception e) {
			return 0;
		}
	}
	
	@Override
	public Page<Comment> getAllComments(int pageNo) {
		try {
			Pageable pageable = PageRequest.of(pageNo, 5);
			Page<Comment> page = commentRepository.findAll(pageable);
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Comment getCommentById(int id) {
		try {
			Comment comment = commentRepository.findById(id).get();
			return comment;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Boolean deleteComment(Comment comment) {
		try {
			commentRepository.delete(comment);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

}
