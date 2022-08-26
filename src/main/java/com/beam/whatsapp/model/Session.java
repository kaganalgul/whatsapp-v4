package com.beam.whatsapp.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Accessors(chain = true)
@TypeAlias("Session")
@Document("Session")
public class Session extends Base{
    private String title;
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private String lastMessage;
    private Date lastMessageTime;
    private String avatar;
}
