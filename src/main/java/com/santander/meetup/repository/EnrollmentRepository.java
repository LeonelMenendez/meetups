package com.santander.meetup.repository;

import com.santander.meetup.model.EnrollmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentModel, Long> {
    Optional<EnrollmentModel> findById(Long enrollmentId);
    boolean existsByMeetupIdAndUserId(Long meetupId, Long userId);
}