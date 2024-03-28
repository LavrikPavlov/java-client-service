package ru.kazan.clientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kazan.clientservice.utils.convector.RoleEnumConverter;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id")
    private UUID clientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private String password;

    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum role;

}
