package com.cab.allocation.registration.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TeamMember {

	@Id
	private String id;

	@NotNull
	private String teamMemberId;
	@NotNull
	private Gender gender;
	@NotNull
	private String dropPoint;

	public TeamMember() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamMemberId() {
		return teamMemberId;
	}

	public void setTeamMemberId(String teamMemberId) {
		this.teamMemberId = teamMemberId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getDropPoint() {
		return dropPoint;
	}

	public void setDropPoint(String dropPoint) {
		this.dropPoint = dropPoint;
	}

}
