package dev.fileconversion.entity;


import dev.fileconversion.enums.InboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inbox")
public class Inbox {

    @Id
    @Column(name = "event_id", nullable = false)
    private Long eventId;


    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InboxStatus status;

}
