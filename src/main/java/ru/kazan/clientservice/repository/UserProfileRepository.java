package ru.kazan.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kazan.clientservice.model.UserProfile;

import java.util.Optional;
import java.util.UUID;


public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @Query("FROM UserProfile WHERE clientId = ?1")
    Optional<UserProfile> findByClientId(UUID clientId);

}
