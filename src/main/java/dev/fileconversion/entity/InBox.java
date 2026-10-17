package dev.fileconversion.entity;


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
@Table(name = "in_box")
public class InBox {

    @Id
    @Column(name = "event_id", nullable = false)
    private Long eventId;


    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

}
