package com.example.vetapp.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "cases")
public class Case {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User doctor;

	@Column
	private String name;

	@Column
	private String description;
	
	@Column
	private String price;

	@ManyToOne
	private Pet pet;
	
	@Column
	@CreationTimestamp
	private Timestamp createDateTime;
	
	public Case() {}

	public Case(User doctor, String name, String description, String price, Pet pet) {
		super();
		this.doctor = doctor;
		this.name = name;
		this.description = description;
		this.price = price;
		this.pet = pet;
	}

	public Case(Long id, User doctor, String name, String description, String price, Pet pet, Timestamp createDateTime) {
		super();
		this.id = id;
		this.doctor = doctor;
		this.name = name;
		this.description = description;
		this.price = price;
		this.pet = pet;
		this.createDateTime = createDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getDoctor() {
		return doctor;
	}

	public void setDoctor(User doctor) {
		this.doctor = doctor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

}
