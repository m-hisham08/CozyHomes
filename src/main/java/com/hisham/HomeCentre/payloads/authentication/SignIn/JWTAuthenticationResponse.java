package com.hisham.HomeCentre.payloads.authentication.SignIn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JWTAuthenticationResponse(String accessToken){
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
    }
}
