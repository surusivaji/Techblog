package org.siva.techblog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="fullName", nullable=false, length=50)
	private String fullName;
	
	@Column(name="email", nullable=false, length=50, unique=true)
	private String email;
	
	@Column(name="mobileNumber", nullable=false, length=10, unique=true)
	private String mobileNumber;
	
	@Column(name="password", nullable=false, length=20)
	private String password;
	
	@Column(name="profileImage", nullable=false)
	private String profileImageUrl;
	
	@Column(name="role", nullable=false, length=20)
	private String role;

}
