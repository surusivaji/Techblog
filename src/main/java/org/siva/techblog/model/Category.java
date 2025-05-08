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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@Column(name="CategoryName", nullable=false, length=40)
	private String name;
	
	@Column(name="Status", nullable=false)
	private Boolean status;
	
}