package ru.kazan.clientservice.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kazan.clientservice.model.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("FROM Address a WHERE a.country = ?1 AND a.city = ?2 AND a.street = ?3" +
            " AND a.house = ?4 AND a.apartment = ?5")
    Optional<Address> findLikeAddress(String contry, String city, String street,
                                      Integer house, Integer apartment);

}
