package com.beam.whatsapp.dto;

import com.beam.whatsapp.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SessionRequest {
    private List<User> users;
}
