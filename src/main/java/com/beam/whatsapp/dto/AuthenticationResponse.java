package com.beam.whatsapp.dto;

import com.beam.whatsapp.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {

    /**
     * 0 -> success
     * 10 -> password does not match
     * 11 -> username not found
     * */
    private int code;
    private User user;

}
