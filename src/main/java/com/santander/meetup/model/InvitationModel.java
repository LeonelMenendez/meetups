package com.santander.meetup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
public class InvitationModel {

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

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }
}



