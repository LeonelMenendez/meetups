package com.santander.meetup.repository;

import com.santander.meetup.model.InvitationModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationModel, Long> {

    Optional<InvitationModel> findById(Long invitationId);

    @EntityGraph(attributePaths = {"meetup", "user"})
    List findAll(Example invitation);

    boolean existsByMeetupIdAndUserIdAndStatusNot(Long meetupId, Long userId, InvitationModel.Status status);
}