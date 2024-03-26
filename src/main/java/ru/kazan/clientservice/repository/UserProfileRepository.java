package ru.kazan.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazan.clientservice.model.UserProfile;

import java.util.UUID;


public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

}
