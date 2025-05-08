package org.siva.techblog.service;

import org.siva.techblog.model.Admin;
import org.springframework.data.domain.Page;

public interface IAdminService {
	
	Admin saveAdmin(Admin admin);
	
	Admin getAdminByEmailAndPassword(String email, String password);
	
	Page<Admin> getAllAdminsInformation(int pageNo);
	
	Admin getAdminById(int id);
	
	Boolean deleteAdmin(Admin admin);

}
