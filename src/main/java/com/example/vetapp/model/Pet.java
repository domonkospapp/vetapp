package com.example.vetapp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "pets")
public class Pet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;
	
	@Column
	private Long yearOfBirth;

	@Column
	private String type;
	
	@ManyToOne
	private User owner;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "pet")
	private List<Case> cases;
	
	Pet(){}

	public Pet(Long id, String name, Long yearOfBirth, String type, User owner, List<Case> cases) {
		super();
		this.id = id;
		this.name = name;
		this.yearOfBirth = yearOfBirth;
		this.type = type;
		this.owner = owner;
		this.cases = cases;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Long yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Case> getCases() {
		return cases;
	}

	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

}
