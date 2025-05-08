package org.siva.techblog.repository;

import org.siva.techblog.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	Admin findByEmailAndPassword(String email, String password);

}
