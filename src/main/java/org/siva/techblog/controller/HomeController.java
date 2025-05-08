package org.siva.techblog.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.siva.techblog.model.Blog;
import org.siva.techblog.model.User;
import org.siva.techblog.service.IBlogService;
import org.siva.techblog.service.ICategoryService;
import org.siva.techblog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IBlogService blogService;
	
	@ModelAttribute
	public void commonData(Model model) {
		model.addAttribute("categories", categoryService.getActiveCategories());
	}
	
	@GetMapping("/")
	public String indexPage() {
		return "Index";
	}
	
	@GetMapping("/signup")
	public String signupPage() {
		return "Register";
	}
	
	@GetMapping("/signin")
	public String singinPage() {
		return "Login";
	}
	
	@GetMapping("/posts") 
	public String showPosts(Model model) {
		List<Blog> blogs = blogService.getAllBlogs();
		model.addAttribute("blogs", blogs);
		return "Posts";
	}
	
	@GetMapping("/blogs")
	public String getCategoriesByBlogs(Model model, @RequestParam("category") String category) {
		List<Blog> blogs = blogService.getBlogsByCategory(category);
		model.addAttribute("blogs",blogs);
		return "Posts";
	}
	
	@GetMapping("/adminLogin")
	public String adminLoginPage() {
		return "AdminLogin";
	}
	
	@PostMapping("/saveUserInformation")
	public String saveUserInformation(@ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile, HttpSession session) {
		try {	
			user.setRole("ROLE_USER");
			if (multipartFile.isEmpty()) {
				user.setProfileImageUrl("default.png");
			}
			else {
				File file = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(file.getAbsolutePath()+File.separator+"users"+File.separator+multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				user.setProfileImageUrl(multipartFile.getOriginalFilename());
			}
			User saveUser = userService.saveUser(user);
			if (saveUser!=null) {
				session.setAttribute("successMsg", "Registration successful");
			}
			else {
				session.setAttribute("failMsg", "Something went wrong");
			}
			return "redirect:/signup";
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("warningMsg", "Something went wrong while uploading the image..!!");
			return "redirect:/signup";
		}
	}
	

}
