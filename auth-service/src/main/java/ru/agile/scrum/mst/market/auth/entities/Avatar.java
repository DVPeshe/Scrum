package ru.agile.scrum.mst.market.auth.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avatars")
public class Avatar {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "avatar")
    private byte[] avatar;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
