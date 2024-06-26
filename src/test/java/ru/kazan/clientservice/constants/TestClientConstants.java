package ru.kazan.clientservice.constants;


import ru.kazan.clientservice.dto.client.ResponseFullInfoDtoImpl;
import ru.kazan.clientservice.dto.client.ResponseShortInfoDtoImpl;
import ru.kazan.clientservice.model.Address;
import ru.kazan.clientservice.model.Client;
import ru.kazan.clientservice.model.Passport;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.utils.enums.ClientStatus;
import ru.kazan.clientservice.utils.enums.GenderEnum;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;


public class TestClientConstants {

    /**
     * CLIENT'S IDS
     */

    public static final String CLIENT_ID_FOR_CLIENT = "7e0729ed-dc53-4ff7-b710-0ba6ebb65578";
    public static final String CLIENT_ID_FOR_SESSION = "b3f62160-c084-41b1-8189-306d1906e2fb";

    /**
     * VERIFY CODE FOR TEST
     */

    public static final String VALID_CODE_FROM_DB = "892453";

    public static final String NOT_VALID_CODE = "0000000";



    /**
     * ENTITY
     * @Client
     * @Address
     * @Passport
     * @UserProfile
     */

    public static final Passport PASSPORT_DEFAULT = new Passport(
            1L,
            "1111",
            "222333",
            GenderEnum.MALE,
            "Test"
    );

    public static final Address ADDRESS_DEFAULT = new Address(1L,
            "Test",
            "Test",
            "Test",
            1,
            1
    );

    public static final Client CLIENT_DEFAULT_FOR_CLIENT = new Client(
            UUID.fromString(CLIENT_ID_FOR_CLIENT),
            "Test",
            "Test",
            "Test",
            "89001112233",
            "test@test.ru",
            20,
            new Date(),
            ClientStatus.ACCEPT,
            PASSPORT_DEFAULT,
            new HashSet<>(Collections.singleton(ADDRESS_DEFAULT))
    );

    public static final Client CLIENT_DEFAULT_FOR_SESSION = new Client(
            UUID.fromString(CLIENT_ID_FOR_SESSION),
            "Test",
            "Test",
            "Test",
            "89001112233",
            "test@test.ru",
            20,
            new Date(),
            ClientStatus.ACCEPT,
            PASSPORT_DEFAULT,
            new HashSet<>(Collections.singleton(ADDRESS_DEFAULT))
    );

    public static final UserProfile USER_PROFILE_FOR_CLIENT = new UserProfile(
            UUID.fromString(CLIENT_ID_FOR_CLIENT),
            CLIENT_DEFAULT_FOR_CLIENT,
            null,
                RoleEnum.CLIENT,
            "000000",
            "000000",
            null
    );

    public static final UserProfile USER_PROFILE_FOR_SESSION = new UserProfile(
            UUID.fromString(CLIENT_ID_FOR_SESSION),
            CLIENT_DEFAULT_FOR_SESSION,
            "{bcrypt}$2a$10$bjukXxbHErsHiLGY9JV/5ugTILO/AyNQmeixB0IC7fmL.vPQvfbA6",
            RoleEnum.ADMIN,
            "000000",
            "000000",
            null
    );





    /**
     * RESPONSE DTO FOR ANSWER
     */

    public static final ResponseFullInfoDtoImpl RESPONSE_FULL_INFO_DTO =
            ResponseFullInfoDtoImpl.builder()
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
                            "5050",
                            "119223",
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



