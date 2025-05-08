package org.siva.techblog.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Blog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="Title", nullable=false, length=50)
	private String title;
	
	@Lob
	@Column(name="content", nullable=false, columnDefinition = "text")
	private String content;
	
	@Column(nullable=false)
	private String blogImage;
	
	@Column(name="Category", nullable=false, length=50)
	private String category;
	
	@Column(name="createdAt", nullable=false)
	private Date createdAt;
	
	@Lob
	@Column(name="sampleCode", columnDefinition = "text", nullable=false)
	private String sampleCode;
	
	@ManyToOne
	private Admin admin;
	
	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Likes> likes = new ArrayList<>();

}
