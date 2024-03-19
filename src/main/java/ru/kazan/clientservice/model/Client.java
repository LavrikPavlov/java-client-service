package ru.kazan.clientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kazan.clientservice.utils.enums.ClientStatus;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    private String patronymic;

    @Column(name = "mobile_phone")
    @NotNull
    private String mobilePhone;

    @Email
    @NotNull
    private String email;

    private Integer age;

    @Column(name = "date_registration")
    private Date dateRegistration;

    private ClientStatus status;

    @OneToOne
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "address_client",
            joinColumns = @JoinColumn(name = "client_uuid"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> address;


}
