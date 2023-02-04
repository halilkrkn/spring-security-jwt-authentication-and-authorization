package com.example.springsecurityjwtauthenticationandauthorization.repository;

import com.example.springsecurityjwtauthenticationandauthorization.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Kullanıcıyı E-mail ile bulma
    Optional<User> findByEmail(String email);
}

