package com.beam.whatsapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AuthenticationRequest {

    private String number;
    private String password;
}
