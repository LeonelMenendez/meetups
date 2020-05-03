package com.santander.meetup.repository;

import com.santander.meetup.model.EnrollmentModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentModel, Long> {

    @EntityGraph(attributePaths = {"meetup", "user"})
    List<EnrollmentModel> findAllWithMeetupAndUserByUserId(Long userId);

    boolean existsByMeetupIdAndUserId(Long meetupId, Long userId);

    long countByMeetupId(Long meetupId);
}