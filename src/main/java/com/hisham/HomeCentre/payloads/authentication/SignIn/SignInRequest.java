package com.hisham.HomeCentre.payloads.authentication.SignIn;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignInRequest {
    @Size(max = 256)
    private String emailOrUsername;

    @Size(min = 8, max = 100)
    private String password;
}
