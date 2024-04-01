package ru.kazan.clientservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.kazan.clientservice.utils.convector.RoleEnumConverter;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profile", schema = "user_storage")
public class UserProfile {

    @Id
    @Column(name = "client_id", updatable = false, nullable = false)
    private UUID clientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private String password;

    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum role;

    @Column(name = "last_code")
    @Length(min = 6, max = 6)
    private String lastCode;

}
