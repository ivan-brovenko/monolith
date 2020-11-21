package com.ivanbr.monolith.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Actor {

    @Id
    private Long id;
    private String name;
    private String birthday;
    private String deathday;

    @ManyToMany(mappedBy = "actors")
    private Set<User> users = new HashSet<>();

    public Actor() {
    }

    public Actor(Long id, String name, String birthday, String deathday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.deathday = deathday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id.equals(actor.id) &&
                name.equals(actor.name) &&
                birthday.equals(actor.birthday) &&
                deathday.equals(actor.deathday) &&
                users.equals(actor.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthday, deathday, users);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", deathday='" + deathday + '\'' +
                '}';
    }
}
