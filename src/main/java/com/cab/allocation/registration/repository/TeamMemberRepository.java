package com.cab.allocation.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cab.allocation.registration.model.TeamMember;

public interface TeamMemberRepository extends MongoRepository<TeamMember, String> {

}
