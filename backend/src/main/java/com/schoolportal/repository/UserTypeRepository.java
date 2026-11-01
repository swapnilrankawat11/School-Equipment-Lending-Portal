package com.schoolportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoolportal.entity.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

}
