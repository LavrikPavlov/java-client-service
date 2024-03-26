package ru.kazan.clientservice.model;

import jakarta.persistence.*;
import lombok.*;
import ru.kazan.clientservice.utils.convector.RoleEnumConverter;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private String password;

    @Convert(converter = RoleEnumConverter.class)
    private Set<RoleEnum> role;

}
