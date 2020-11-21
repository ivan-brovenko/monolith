package com.ivanbr.monolith.repository;

import com.ivanbr.monolith.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds user by his email.
     *
     * @param email user's email
     * @return optional of user
     */
    Optional<User> findByEmail(String email);
}