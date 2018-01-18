package com.cab.allocation.registration.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cab.allocation.registration.config.RestUtils;
import com.cab.allocation.registration.model.TeamMember;
import com.cab.allocation.registration.repository.TeamMemberRepository;

@RestController
public class RegistrationController {

	private TeamMemberRepository teamMemberRepository;
	private RestUtils restUtils;

	@Autowired
	public RegistrationController(TeamMemberRepository teamMemberRepository, RestUtils restUtils) {
		super();
		this.teamMemberRepository = teamMemberRepository;
		this.restUtils = restUtils;
	}

	@PostMapping("/register")
	public ResponseEntity<TeamMember> register(@RequestBody TeamMember teamMember) {

		if (!restUtils.isDropPointPresent(teamMember.getDropPoint()))
			throw new ResourceNotFoundException(
					String.format("Drop Point { %s } not found ", teamMember.getDropPoint()));

		TeamMember persisted = teamMemberRepository.save(teamMember);
		return ResponseEntity.created(getLocationHeader(persisted)).body(persisted);
	}

	@GetMapping("/register")
	public ResponseEntity<List<TeamMember>> getTeamMembers() {
		return ResponseEntity.ok().body(teamMemberRepository.findAll());

	}

	@GetMapping("/teamMember/{id}")
	public ResponseEntity<TeamMember> getTeamMemberById(@PathVariable(name = "id") String id) {
		return ResponseEntity.ok().body(teamMemberRepository.findOne(id));

	}

	protected URI getLocationHeader(TeamMember teamMember) {
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("teamMember/{id}")
				.buildAndExpand(teamMember.getId()).toUri();
		return location;
	}

}
