package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "sessions")
@Getter
@NoArgsConstructor
@Entity
public class UserSession {
    @Id
    @Column(name = "ID", nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private User user;
    @Column(name = "ExpiresAt", nullable = false)
    private LocalDateTime expiresAt;

    public UserSession(User user, LocalDateTime expiresAt) {
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public void setId(UUID id) {
        if (id != null) {
            this.id = id;
        }
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        if (expiresAt != null) {
            this.expiresAt = expiresAt;
        }
    }
}
