package com.santander.meetup.repository;

import com.santander.meetup.model.UserModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findById(Long id);

    Optional<UserModel> findByEmail(String email);

    List findAll(Example user);

    boolean existsByEmail(String email);
}