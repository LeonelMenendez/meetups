package com.santander.meetup.repository;

import com.santander.meetup.model.MeetupModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetupRepository extends JpaRepository<MeetupModel, Long> {

    Optional<MeetupModel> findById(Long id);

    @EntityGraph(attributePaths = {"enrolledUsers"})
    Optional<MeetupModel> findWithEnrolledUsersById(Long id);

    @EntityGraph(attributePaths = {"enrolledUsers"})
    List<MeetupModel> findAllWithEnrolledUsersByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = {"enrolledUsers"})
    List<MeetupModel> findAllByEnrolledUsersUserId(Long userId);

    boolean existsById(Long id);

    boolean existsByOwnerIdAndDay(Long ownerId, LocalDate day);

}