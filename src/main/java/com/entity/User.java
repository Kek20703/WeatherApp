package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // TODO: не будет ли проблем с неуникальными логинами?
    @Column(name = "Login")
    private String login;
    @Column(name = "Password")
    private String password;
    // TODO: можно подумать об использовании @ElementCollection, @Embeddable
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserSession> sessions;
    // TODO: возможно имеет смысл сделать ManyToMany, чтобы не хранить повторяющиеся среди разных юзеров локации
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Location> locations;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
