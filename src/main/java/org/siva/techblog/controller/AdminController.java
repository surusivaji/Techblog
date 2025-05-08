package org.siva.techblog.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.siva.techblog.model.Admin;
import org.siva.techblog.model.Blog;
import org.siva.techblog.model.Category;
import org.siva.techblog.model.Comment;
import org.siva.techblog.model.Likes;
import org.siva.techblog.model.User;
import org.siva.techblog.service.IAdminService;
import org.siva.techblog.service.IBlogService;
import org.siva.techblog.service.ICategoryService;
import org.siva.techblog.service.ICommentService;
import org.siva.techblog.service.ILikeService;
import org.siva.techblog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IBlogService blogService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICommentService commentService;
	
	@Autowired
	private ILikeService likeService;
	
	@ModelAttribute
	public void commonData(Model model, HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			model.addAttribute("admin", admin);
		}
	}
	
	@PostMapping("/getAdminInformation")
	public String getAdminInformation(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
		Admin admin = adminService.getAdminByEmailAndPassword(email, password);
		if (admin!=null) {
			session.setAttribute("admin", admin);
			return "redirect:/admin/home";
		}
		else {
			session.setAttribute("failMsg", "Something went wrong..!!");
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/home")
	public String homePage(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			return "Admin/Home";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/addBlog")
	public String addBlog(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			model.addAttribute("categories", categoryService.getActiveCategories());
			return "Admin/AddBlog";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/saveBlogInformation")
	public String saveBlogInformation(HttpSession session, @ModelAttribute Blog blog, @RequestParam("image") MultipartFile multipartFile) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			try {
				if (multipartFile.isEmpty()) {
					session.setAttribute("warningMsg", "You are not uploading the image 😮😮😮");
					return "redirect:/admin/addBlog";
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"blog_posts"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					blog.setBlogImage(multipartFile.getOriginalFilename());
				}
				blog.setAdmin(admin);
				blog.setCreatedAt(Date.valueOf(LocalDate.now()));
				Blog saveBlog = blogService.saveBlog(blog);
				if (saveBlog!=null) {
					session.setAttribute("successMsg", "Blog added successfully 😎😎😎");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
				return "redirect:/admin/addBlog";
			}
			catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("warningMsg", "Sorry is not uploading properly 🙄🙄🙄");
				return "redirect:/admin/addBlog";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/blogs")
	public String blogsInformation(HttpSession session, Model model, @RequestParam(defaultValue="0") int pageNo) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Blog> page = blogService.getAllBlogs(pageNo);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("blogs", page.getContent());
			return "Admin/ViewBlogs";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/editBlog/{id}")
	public String editBlog(HttpSession session, Model model, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			List<Category> categories = categoryService.getActiveCategories();
			model.addAttribute("categories", categories);
			Blog blog = blogService.getBlogById(id);
			if (blog!=null) {
				model.addAttribute("blog", blog);
				return "Admin/EditBlog";
			}
			else {
				session.setAttribute("warningMsg", "Blog information not found 🤯🤯🤯");
				return "redirect:/admin/blogs";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/updateBlogInformation")
	public String updateBlogInformation(@ModelAttribute Blog blog, HttpSession session,@RequestParam("image") MultipartFile multipartFile) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			try {
				if (multipartFile.isEmpty()) {
					Blog oldBlog = blogService.getBlogById(blog.getId());
					blog.setBlogImage(oldBlog.getBlogImage());
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"blog_posts"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					blog.setBlogImage(multipartFile.getOriginalFilename());
				}
				blog.setAdmin(admin);
				blog.setCreatedAt(Date.valueOf(LocalDate.now()));
				Blog saveBlog = blogService.saveBlog(blog);
				if (saveBlog!=null) {
					session.setAttribute("successMsg", "Blog information successfully updated 🤩🤩🤩");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 🤯🤯🤯🤯");
				}
				return "redirect:/admin/blogs";
			} catch (Exception e) {
				session.setAttribute("warningMsg", "Blog image is not updated properly 😵‍💫😵‍💫😵‍💫");
				return "redirect:/admin/blogs";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteBlog/{id}")
	public String deleteBlogInformation(HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Blog blog = blogService.getBlogById(id);
			if (blog!=null) {
				Boolean deleteBlog = blogService.deleteBlog(blog);
				if (deleteBlog) {
					session.setAttribute("successMsg", "Blog information successfully deleted 🤬🤬🤬");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
			}
			else {
				session.setAttribute("warningMsg", "Blog information not found 🫡🫡🫡");
			}
			return "redirect:/admin/blogs";
		}
		else {
			return "redirect:/adminLogin";
		}
	} 
	
	@GetMapping("/categories")
	public String cateogories(HttpSession session, @RequestParam(defaultValue="0") int pageNo, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Category> page = categoryService.getCategories(pageNo);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("categories", page.getContent());
			return "Admin/Category";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(HttpSession session, @ModelAttribute Category category) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Category saveCategory = categoryService.saveCategory(category);
			System.out.println(saveCategory);
			if (saveCategory!=null) {
				session.setAttribute("successMsg", "Category successfully added 😎😎😎");
			}
			else {
				session.setAttribute("failMsg", "Something went wrong 😡😡😡");
			}
			return "redirect:/admin/categories";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/editCategory/{id}")
	public String editCategoryPage(HttpSession session, Model model, @PathVariable("id") Integer id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Category category = categoryService.getCategoryById(id);
			if (category!=null) {
				model.addAttribute("category", category);
				return "/Admin/EditCategory";
			}
			else {
				session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				return "redirect:/admin/categories";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/updateCategoryInformation")
	public String updateCategoryInformation(HttpSession session, @ModelAttribute Category category) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Category saveCategory = categoryService.saveCategory(category);
			if (saveCategory!=null) {
				session.setAttribute("successMsg", "Category information updated 😎😎😎");
			}
			else {
				session.setAttribute("failMsg", "Soemthing went wrong 😡😡😡");
			}
			return "redirect:/admin/categories";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategoryInformation(@PathVariable("id") Integer id, HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Category category = categoryService.getCategoryById(id);
			if (category!=null) {
				Boolean isDeleted = categoryService.deleteCategory(category);
				if (isDeleted) {
					session.setAttribute("successMsg", "Category information is removed 🙄🙄🙄");
				}
				else {
					session.setAttribute("failMsg", "Soemthing went wrong 😡😡😡");
				}
			}
			else {
				session.setAttribute("warningMsg", "Category is not found 🫡🫡🫡");
			}
			return "redirect:/admin/categories";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/users")
	public String showUsers(Model model, HttpSession session, @RequestParam(defaultValue="0") int pageNo) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<User> page = userService.getAllUsers(pageNo);
			model.addAttribute("users", page.getContent());
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			return "/Admin/ViewUsers";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/editUser/{id}")
	public String editUser(HttpSession session, Model model, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			User user = userService.getUserById(id);
			if (user!=null) {
				model.addAttribute("user", user);
				return "/Admin/EditUser";
			}
			else {
				session.setAttribute("warningMsg", "User is not found 🥱🥱🥱");
				return "redirect:/admin/users";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/updateUserInformation")
	public String updateUserInformation(HttpSession session, Model model, @ModelAttribute User user, @RequestParam("image") MultipartFile multipartFile) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			try {				
				User oldUser = userService.getUserById(user.getId());
				if (multipartFile.isEmpty()) {
					user.setProfileImageUrl(oldUser.getProfileImageUrl());
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"users"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					user.setProfileImageUrl(multipartFile.getOriginalFilename());
				}
				user.setRole(oldUser.getRole());
				User saveUser = userService.saveUser(user);
				if (saveUser!=null) {
					session.setAttribute("successMsg", "User information updated successfully 😎😎😎");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
				return "redirect:/admin/users";
			}
			catch (Exception e) {
				session.setAttribute("warningMsg", "Profile image is not updating 🙄🙄🙄");
				return "redirect:/admin/users";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUserInformation(HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			User user = userService.getUserById(id);
			if (user!=null) {
				Boolean deleteUser = userService.deleteUser(user);
				if (deleteUser) {
					session.setAttribute("successMsg", "User information successfully deleted 😑😑😑");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😵‍💫😵‍💫😵‍💫");
				}
			}
			else {
				session.setAttribute("warningMsg", "User information not found 🙄🙄🙄");
			}
			return "redirect:/admin/users";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/addAdmin")
	public String addAdminPage(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			return "/Admin/AddAdmin";
		}
		else {
			return "redirect:/adminLogin";
		}
	} 
	
	@PostMapping("/saveAdminInformation")
	public String saveAdminInformation(HttpSession session, @ModelAttribute Admin admin, @RequestParam("image") MultipartFile multipartFile) {
		Admin adminInfo = (Admin) session.getAttribute("admin");
		if (adminInfo!=null) {
			try {
				if (multipartFile.isEmpty()) {
					session.setAttribute("warningMsg", "Please upload image 👩‍🍼👩‍🍼👩‍🍼");
					return "redirect:/admin/addAdmin";
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"users"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					admin.setProfileImageUrl(multipartFile.getOriginalFilename());
				}
				admin.setRole("ROLE_ADMIN");
				admin.setId(null);
				Admin save = adminService.saveAdmin(admin);
				if (save!=null) {
					session.setAttribute("successMsg", "Admin added successfully 😍😍😍");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😵‍💫😵‍💫😵‍💫");
				}
				return "redirect:/admin/addAdmin";
			} catch (Exception e) {
				session.setAttribute("warningMsg", "Image is not uploading 🙄🙄🙄");
				return "redirect:/admin/addAdmin";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/admins")
	public String adminsInformation(Model model, HttpSession session, @RequestParam(defaultValue = "0") int pageNo) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Admin> page = adminService.getAllAdminsInformation(pageNo);
			List<Admin> admins = page.getContent();
			List<Admin> list = new ArrayList<Admin>();
			for (Admin a : admins) {
				if (a.getId()!=admin.getId()) {
					list.add(a);
				}
			}
			model.addAttribute("admins", list);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			return "/Admin/ViewAdmins";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/editAdmin/{id}")
	public String editAdmin(Model model, HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Admin adminInfo = adminService.getAdminById(id);
			if (adminInfo!=null) {
				model.addAttribute("adminInfo", adminInfo);
				return "/Admin/EditAdmin";
			}
			else {
				session.setAttribute("failMsg", "admin information not found 😡😡😡");
				return "redirect:/admin/admins";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/updateAdminInformation")
	public String updateAdminInformation(HttpSession session, 
										 Integer id,
										@RequestParam("fullName") String fullName,
										@RequestParam("email") String email,
										@RequestParam("mobileNumber") String mobileNumber,
										@RequestParam("image") MultipartFile multipartFile) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			try {				
				Admin updateAdmin = adminService.getAdminById(id);
				updateAdmin.setFullName(fullName);
				updateAdmin.setEmail(email);
				updateAdmin.setMobileNumber(mobileNumber);
				if (multipartFile.isEmpty()) {
					updateAdmin.setProfileImageUrl(updateAdmin.getProfileImageUrl());
				}
				else {
					File file = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+"users"+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					updateAdmin.setProfileImageUrl(multipartFile.getOriginalFilename());
				}
				Admin isUpdate = adminService.saveAdmin(updateAdmin);
				if (isUpdate!=null) {
					session.setAttribute("successMsg", "Admin information updated successfully 😍😍😍");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
				return "redirect:/admin/admins";
			} catch (Exception e) {
				session.setAttribute("warningMsg", "Something went wrong 😵‍💫😵‍💫😵‍💫");
				return "redirect:/admin/admins";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@PostMapping("/changePassword")
	public String changePassword(HttpSession session, @RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Admin adminInfo = adminService.getAdminByEmailAndPassword(admin.getEmail(), oldPassword);
			if (adminInfo!=null) {
				session.setAttribute("updateSuccess", "Password changed 😍😍😍");
			}
			else {
				session.setAttribute("updateFail", "Password is incorrect 😵‍💫😵‍💫😵‍💫");
			}
			return "redirect:/admin/admins";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteAdmin/{id}")
	public String deleteAdminInformation(HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Admin adminInfo = adminService.getAdminById(id);
			if (adminInfo!=null) {
				Boolean deleteAdmin = adminService.deleteAdmin(adminInfo);
				if (deleteAdmin) {
					session.setAttribute("successMsg", "Admin information deleted 🙄🙄🙄");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				}
			}
			else {
				session.setAttribute("warningMsg", "Admin information is not found 🥱🥱🥱");
			}
			return "redirect:/admin/admins";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAdminAccount(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Boolean isDelete = adminService.deleteAdmin(admin);
			if (isDelete) {
				session.setAttribute("successMsg", "Account deleted permunantly 😊😊😊");
				return "redirect:/adminLogin";
			}
			else {
				session.setAttribute("failMsg", "Something went wrong 😡😡😡");
				return "redirect:/admin/admins";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/comments")
	public String commentsPage(HttpSession session, @RequestParam(defaultValue="0") int pageNo, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Comment> page = blogService.getCommentsByAuthor(admin, pageNo);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("totalPages", page.getTotalPages());
			List<Comment> comments = page.getContent().stream().sorted((comments1, comments2) -> comments2.getId().compareTo(comments1.getId())).toList();
			model.addAttribute("comments", comments);
			return "/Admin/Comments";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteComment/{id}")
	public String deleteComment(HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Comment comment = commentService.getCommentById(id);
			if (comment!=null) {
				Boolean isDelete = commentService.deleteComment(comment);
				if (isDelete) {
					session.setAttribute("successMsg", "Comment is deleted 😊😊😊");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 🤬🤬🤬");
				}
			}
			else {
				session.setAttribute("warningMsg", "Comment not found 😡😡😡");
			}
			return "redirect:/admin/comments";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/likes")
	public String likesPage(HttpSession session, @RequestParam(defaultValue="0") int pageNo, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Likes> page = blogService.getLikesByAuthor(admin, pageNo);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			List<Likes> likes = page.getContent().stream().sorted((likes1, likes2) -> likes2.getId().compareTo(likes1.getId())).toList();
			model.addAttribute("likes", likes);
			return "/Admin/ViewLikes";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/deleteLike/{id}")
	public String deleteLike(HttpSession session, @PathVariable("id") int id) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Likes likes = likeService.getLikesById(id);
			if (likes!=null) {
				Boolean isDelete = likeService.deleteLike(likes);
				if (isDelete) {
					session.setAttribute("successMsg", "Like is deleted 🙄🙄🙄");
				}
				else {
					session.setAttribute("failMsg", "Something went wrong 🤬🤬🤬");
				}
			}
			else {
				session.setAttribute("warningMsg", "Likes not found 😵‍💫😵‍💫😵‍💫");
			}
			return "redirect:/admin/likes";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/yourBlogs")
	public String yourBlogs(HttpSession session, @RequestParam(defaultValue="0") int pageNo, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Page<Blog> page = blogService.getBlogsByAuthor(admin, pageNo);
			model.addAttribute("blogs", page.getContent());
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			return "/Admin/YourBlogs";
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/viewBlog/{id}")
	public String viewBlog(HttpSession session, @PathVariable("id") int id, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			Blog blog = blogService.getBlogById(id);
			if (blog!=null) {
				model.addAttribute("blog", blog);
				model.addAttribute("comments", commentService.getCommentsByBlog(blog));
				return "/Admin/ViewBlog";
			}
			else {
				session.setAttribute("failMsg", "Blog information not found 👎👎👎");
				return "redirect:/admin/yourBlogs";
			}
		}
		else {
			return "redirect:/adminLogin";
		}
	}
	
	@GetMapping("/logout")
	public String adminLogout(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin!=null) {
			session.removeAttribute("admin");
			session.setAttribute("logoutInfo", "You have logged out 🥱🥱🥱");
			return "redirect:/adminLogin";
		}
		else {
			return "redirect:/adminLogin";
		}
	}

}
