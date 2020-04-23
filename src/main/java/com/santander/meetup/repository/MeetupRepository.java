package com.santander.meetup.repository;

import com.santander.meetup.model.MeetupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetupRepository extends JpaRepository<MeetupModel, Long> {
}