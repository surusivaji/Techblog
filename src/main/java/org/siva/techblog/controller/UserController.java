package org.siva.techblog.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.Likes;
import org.siva.techblog.model.User;
import org.siva.techblog.service.IBlogService;
import org.siva.techblog.service.ICategoryService;
import org.siva.techblog.service.ICommentService;
import org.siva.techblog.service.ILikeService;
import org.siva.techblog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IBlogService blogService;
	
	@Autowired
	private ICommentService commentService;
	
	@Autowired
	private ILikeService likeService;
	
	@ModelAttribute
	public void commonData(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("categories", categoryService.getActiveCategories());
	}
	
	@PostMapping("/getUserData")
	public String getUserData(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
		User user = userService.getUserByEmailAndPassword(email, password);
		if (user!=null) {
			session.setAttribute("user", user);
			return "redirect:/user/home";
		}
		else {
			session.setAttribute("failMsg", "Invalid Credientials 😡😡😡");
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/home")
	public String homePage(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			return "User/Home";
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/posts")
	public String posts(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			List<Blog> blogs = blogService.getAllBlogs();
			model.addAttribute("blogs", blogs);			
			return "/User/Posts";
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/blogs")
	public String viewBlogByCategory(HttpSession session, @RequestParam("category") String category, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			List<Blog> blogs = blogService.getBlogsByCategory(category);
			model.addAttribute("blogs", blogs);
			return "/User/Posts";
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/post/{id}")
	public String viewPost(HttpSession session, @PathVariable("id") int id, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			Blog blog = blogService.getBlogById(id);
			if (blog!=null) {
				model.addAttribute("blog", blog);
				List<Comment> comments = commentService.getCommentsByBlog(blog);
				model.addAttribute("comments", comments);
				model.addAttribute("countCommentsByBlog", commentService.countCommentsByBlog(blog));
				model.addAttribute("countLikesByBlog", likeService.countLikesByBlog(blog));
				return "/User/ViewPost";
			}
			else {
				session.setAttribute("warningMsg", "Blog information not found 😡😡😡");
				return "redirect:/user/posts";
			}
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@PostMapping("/saveComment")
	public String saveComment(HttpSession session, @RequestParam("userId") int userId, @RequestParam("blogId") int blogId, @RequestParam("comment") String comment) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			Boolean commentPresent = commentService.commentPresent(userService.getUserById(userId), blogService.getBlogById(blogId));
			if (!commentPresent) {
				Comment comm = new Comment();
				comm.setComment(comment);
				comm.setUser(userService.getUserById(userId));
				comm.setBlog(blogService.getBlogById(blogId));
				comm.setDate(Date.valueOf(LocalDate.now()));
				Comment saveComment = commentService.saveComment(comm);
				if (saveComment!=null) {
					System.out.println("thanks for the compliment");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
			}
			else {
				session.setAttribute("warningMsg", "You already post the comment 😊😊😊");
			}
			return "redirect:/user/post/"+blogId;
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/likePost")
	public String postLike(HttpSession session, @RequestParam("userId") int userId, @RequestParam("blogId") int blogId) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			Boolean isPresent = likeService.listIsPresent(userService.getUserById(userId), blogService.getBlogById(blogId));
			if (!isPresent) {
				Likes like = new Likes();
				like.setUser(userService.getUserById(userId));
				like.setBlog(blogService.getBlogById(blogId));
				like.setDate(Date.valueOf(LocalDate.now()));
				Likes saveLike = likeService.saveLike(like);
				if (saveLike!=null) {
					System.out.println("hit the like...");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
			}
			else {
				Boolean deleteLike = likeService.deleteLike(userService.getUserById(userId), blogService.getBlogById(blogId));
				if (deleteLike) {
					System.out.println("like deleted");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
			}
			return "redirect:/user/post/"+blogId;
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/editProfile")
	public String editProfile(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			return "/User/EditProfile";
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@PostMapping("/updateUserInformation")
	public String updateUserInformation(HttpSession session, @ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) {
		User userInfo = (User) session.getAttribute("user");
		if (userInfo!=null) {
			try {				
				if (multipartFile.isEmpty()) {
					user.setProfileImageUrl(userInfo.getProfileImageUrl());
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"users"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					user.setProfileImageUrl(multipartFile.getOriginalFilename());
				}
				user.setRole("ROLE_USER");
				User save = userService.saveUser(userInfo);
				if (save!=null) {
					session.setAttribute("user", save);
					session.setAttribute("successMsg", "User information updated 😎😎😎");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
				return "redirect:/user/editProfile";
			} catch (Exception e) {
				session.setAttribute("warningMsg", "Something went wrong 🫡🫡🫡");
				return "redirect:/user/editProfile";
			}
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/search")
	public String searchTitle(HttpSession session, @RequestParam("ch") String ch, Model model) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			List<Blog> blogs = blogService.searchBlogsByTitle(ch);
			model.addAttribute("blogs", blogs);
			return "/User/Posts";
		}
		else {
			return "redirect:/signin";
		}
	}
	
	@GetMapping("/logout") 
	public String logoutPage(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user!=null) {
			session.removeAttribute("user");
			session.setAttribute("logoutInfo", "You have successfully logout 🥱🥱🥱");
			return "redirect:/signin";
		}
		else {
			return "redirect:/signin";
		}
	}

}
