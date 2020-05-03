package io.github.lzmz.meetups.repository;

import io.github.lzmz.meetups.model.InvitationModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationModel, Long> {

    @EntityGraph(attributePaths = {"meetup", "user"})
    List<InvitationModel> findAll(Example invitation);

    boolean existsByMeetupIdAndUserId(Long meetupId, Long userId);
}