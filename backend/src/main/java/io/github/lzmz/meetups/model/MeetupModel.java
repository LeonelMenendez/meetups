package io.github.lzmz.meetups.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meetup", uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "day"}))
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(of = {"owner", "day"})
public class MeetupModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate day;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserModel owner;

    @NotNull
    private Double temperature;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "meetup", fetch = FetchType.LAZY)
    private Set<EnrollmentModel> enrolledUsers = new HashSet<>();
}
