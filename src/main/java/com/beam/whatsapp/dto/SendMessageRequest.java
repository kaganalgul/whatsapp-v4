package com.beam.whatsapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessageRequest {
    private String message;
    private String sessionId;
    private String friendId;
}
