package com.santander.meetup.repository;

import com.santander.meetup.model.MeetupUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetupUserRepository extends JpaRepository<MeetupUserModel, MeetupUserModel.CompositeId> {
}