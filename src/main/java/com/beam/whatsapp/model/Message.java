package com.beam.whatsapp.model;

import com.beam.whatsapp.model.status.MessageStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Message extends Base{
    private String content;
    private Date readAt;
    private Date receivedAt;
    private Date sentAt = new Date();
    @DBRef
    private User sender;
    private List<String> receiverNumbers = new ArrayList<>();
    private List<String> sentUserIdList;
    private List<String> readUserIdList;
    private MessageStatus status = MessageStatus.SENT;
}
