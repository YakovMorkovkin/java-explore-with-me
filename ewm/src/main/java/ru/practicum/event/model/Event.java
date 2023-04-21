package ru.practicum.event.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.model.Category;
import ru.practicum.participationrequest.model.ParticipationRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(min = 20, max = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private List<ParticipationRequest> requests;
    @JoinColumn(name = "confirmed_requests")
    private Long confirmedRequests;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Length(min = 20, max = 7000)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;
    private boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    private String title;
    private String state;
    private Long views;

}
