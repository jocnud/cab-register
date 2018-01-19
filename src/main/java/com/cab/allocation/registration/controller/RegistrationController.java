package com.cab.allocation.registration.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cab.allocation.registration.config.RestUtils;
import com.cab.allocation.registration.model.TeamMember;
import com.cab.allocation.registration.repository.TeamMemberRepository;

import io.swagger.annotations.ApiOperation;

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

	@ApiOperation(value = "Registers users for cab service in bulk", response = Iterable.class)
	@PostMapping("/register")
	public ResponseEntity<List<TeamMember>> register(@Valid @RequestBody List<TeamMember> teamMembers) {

		teamMembers.stream().forEach(teamMember -> {

			if (!restUtils.isDropPointPresent(teamMember.getDropPoint()))
				throw new ResourceNotFoundException(
						String.format("Drop Point { %s } not found ", teamMember.getDropPoint()));
			teamMemberRepository.save(teamMember);
		});

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("register/").build().toUri();

		return ResponseEntity.created(location).body(teamMemberRepository.findAll());
	}

	@ApiOperation(value = "Registers single user for cab service", response = TeamMember.class)
	@PostMapping("/register/teamMember")
	public ResponseEntity<TeamMember> registerTeamMember(@Valid @RequestBody TeamMember teamMember) {

		if (!restUtils.isDropPointPresent(teamMember.getDropPoint()))
			throw new ResourceNotFoundException(
					String.format("Drop Point { %s } not found ", teamMember.getDropPoint()));

		TeamMember persisted = teamMemberRepository.save(teamMember);
		return ResponseEntity.created(getLocationHeader(persisted)).body(persisted);
	}

	@ApiOperation(value = "Updates an existing user", response = TeamMember.class)
	@PutMapping("/register/teamMember")
	public ResponseEntity<TeamMember> updateTeamMember(@Valid @RequestBody TeamMember teamMember) {

		TeamMember fetchedTeamMember = teamMemberRepository.findByTeamMemberId(teamMember.getTeamMemberId());

		if (fetchedTeamMember == null)
			throw new ResourceNotFoundException(
					String.format("No Team Member with id { %s } found ", teamMember.getTeamMemberId()));

		if (!restUtils.isDropPointPresent(teamMember.getDropPoint()))
			throw new ResourceNotFoundException(
					String.format("Drop Point { %s } not found ", teamMember.getDropPoint()));
		teamMember.setId(fetchedTeamMember.getId());
		TeamMember persistedTeamMemeber = teamMemberRepository.save(teamMember);
		return ResponseEntity.created(getLocationHeader(persistedTeamMemeber)).body(persistedTeamMemeber);
	}

	@ApiOperation(value = "( ADMIN ONLY ) Get all registered users ", response = Iterable.class)
	@GetMapping("/teamMembers")
	public ResponseEntity<List<TeamMember>> getTeamMembers() {
		return ResponseEntity.ok().body(teamMemberRepository.findAll());

	}

	@ApiOperation(value = "Returns a registered users by its teamMemberId ", response = Iterable.class)
	@GetMapping("/teamMember/{teamMemberId}")
	public ResponseEntity<TeamMember> getTeamMemberById(@PathVariable(name = "teamMemberId") String teamMemberId) {
		return ResponseEntity.ok().body(teamMemberRepository.findByTeamMemberId(teamMemberId));

	}

	@ApiOperation(value = "Deletes a team member using teamMemberId ", response = TeamMember.class)
	@DeleteMapping("/teamMember/{teamMemberId}")
	public ResponseEntity<Void> deleteTeamMember(@PathVariable(name = "teamMemberId") String teamMemberId) {

		TeamMember fetchedTeamMember = teamMemberRepository.findByTeamMemberId(teamMemberId);

		if (fetchedTeamMember == null)
			throw new ResourceNotFoundException(
					String.format("Team member with id { %s } was not found ", teamMemberId));
		teamMemberRepository.delete(fetchedTeamMember);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

	protected URI getLocationHeader(TeamMember teamMember) {
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("teamMember/{id}")
				.buildAndExpand(teamMember.getId()).toUri();
		return location;
	}

}
