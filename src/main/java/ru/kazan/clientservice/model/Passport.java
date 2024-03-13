package ru.kazan.clientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kazan.clientservice.utils.convector.GenderEnumConverter;
import ru.kazan.clientservice.utils.enums.GenderEnum;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer serial;

    @NotNull
    private Integer number;

    @Convert(converter = GenderEnumConverter.class)
    private GenderEnum gender;

    @Column(name = "issued_by")
    @NotNull
    private String issuedBy;

}
