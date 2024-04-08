package ru.kazan.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("FROM Client c " +
            "JOIN FETCH Passport p ON p.id = c.passport.id " +
            "JOIN FETCH c.address a " +
            "WHERE c.id = ?1")
    Optional<Client> findFullInfoClientById(UUID id);

    List<Client> findClientByAddressContains(Address address);

    @Query("FROM Client c WHERE c.email = ?1 OR c.mobilePhone = ?1")
    Optional<Client> findClientByEmailOrMobilePhone(String contact);

    @Query("FROM Client c JOIN FETCH Passport p ON c.passport.id = p.id WHERE p.serial = ?1 AND p.number =?2")
    Optional<Client> findClientByPassportSerialNumber(String serial, String number);

}
