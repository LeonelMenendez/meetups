package com.santander.meetup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "enrollment", uniqueConstraints = @UniqueConstraint(columnNames = {"meetup_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "meetup_id")
    private MeetupModel meetup;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(name = "checked_in")
    private boolean checkedIn;
}



