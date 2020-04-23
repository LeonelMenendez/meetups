package com.santander.meetup.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "meetup_user")
@IdClass(MeetupUserModel.CompositeId.class)
@Getter
@Setter
@NoArgsConstructor
public class MeetupUserModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "meetup_id")
    private MeetupModel meetup;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(name = "checked_in")
    private boolean checkedIn;

    @Data
    public static class CompositeId implements Serializable {
        private Long meetup;
        private Long user;
    }
}



