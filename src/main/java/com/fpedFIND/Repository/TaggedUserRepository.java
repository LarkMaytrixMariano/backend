package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpedFIND.Entity.TaggedUser;

public interface TaggedUserRepository extends JpaRepository<TaggedUser, Long> {

}