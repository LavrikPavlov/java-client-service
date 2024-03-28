package ru.kazan.clientservice.dto.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeCodeSendDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("contact")
    private String contact;

    @Email
    @JsonIgnore
    private String email;

    @JsonIgnore
    private String mobilePhone;

    public String getContact() {
        if(type != null) {
            if(type.equalsIgnoreCase("email")) {
                this.email = contact;
                return this.email;
            } else if(type.equalsIgnoreCase("mobile_phone")) {
                this.mobilePhone = contact;
                return this.mobilePhone;
            }
        }
        return null;
    }
}
