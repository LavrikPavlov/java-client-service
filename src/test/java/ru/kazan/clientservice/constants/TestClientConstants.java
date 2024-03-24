package ru.kazan.clientservice.constants;


import ru.kazan.clientservice.dto.client.ResponseFullInfoDtoImpl;
import ru.kazan.clientservice.dto.client.ResponseShortInfoDtoImpl;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Passport;
import ru.kazan.clientservice.utils.enums.ClientStatus;
import ru.kazan.clientservice.utils.enums.GenderEnum;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;


public class TestClientConstants {

    /**
     * CLIENT'S IDS
     */

    public static final String CLIENT_ID_CORRECT = "7e0729ed-dc53-4ff7-b710-0ba6ebb65578";

    public static final String CLIENT_ID_NOT_CORRECT = "7e07529ed-dc53-4A7-b710-0ba6ebb25578";

    /**
     * RESPONSE DTO FOR ANSWER
     */

    public static final ResponseFullInfoDtoImpl RESPONSE_FULL_INFO_DTO =
            ResponseFullInfoDtoImpl.builder()
                    .id(UUID.fromString("7e0729ed-dc53-4ff7-b710-0ba6ebb65578"))
                    .firstName("Алиса")
                    .lastName("Акопова")
                    .patronymic("Владимировна")
                    .mobilePhone("89139229100")
                    .email("alisa.akopova2000@yandex.ru")
                    .age(19)
                    .status(ClientStatus.ACCEPT)
                    .dateRegistration("10.03.2024, 00:00")
                    .passport(new Passport(
                            4L,
                            5050,
                            111000,
                            GenderEnum.FEMALE,
                            "ГУ МВД по Новосибирской обл. Центральный р-он"
                    ))
                    .address(new HashSet<>(Collections.singleton(
                            new Address(
                                    1L,
                                    "Россия",
                                    "Новосибирск",
                                    "Депутатская",
                                    43,
                                    293
                            )
                            )))
                    .build();

    public static final ResponseShortInfoDtoImpl RESPONSE_SHORT_INFO_DTO =
            ResponseShortInfoDtoImpl.builder()
                    .id(UUID.fromString("7e0729ed-dc53-4ff7-b710-0ba6ebb65578"))
                    .firstName("Алиса")
                    .lastName("Акопова")
                    .patronymic("Владимировна")
                    .gender(GenderEnum.FEMALE)
                    .mobilePhone("89139229100")
                    .email("alisa.akopova2000@yandex.ru")
                    .age(19)
                    .status(ClientStatus.ACCEPT)
                    .build();
}



