package com.santander.meetup.repository;

import com.santander.meetup.model.MeetupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MeetupRepository extends JpaRepository<MeetupModel, Long> {
    Optional<MeetupModel> findById(Long id);
    boolean existsById(Long id);
    boolean existsByOwnerIdAndDay(Long ownerId, LocalDate day);
}