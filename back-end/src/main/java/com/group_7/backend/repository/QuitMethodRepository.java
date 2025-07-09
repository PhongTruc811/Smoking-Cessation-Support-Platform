package com.group_7.backend.repository;

import com.group_7.backend.entity.QuitMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuitMethodRepository extends JpaRepository<QuitMethod, Long> {

}