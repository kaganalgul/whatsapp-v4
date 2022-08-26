package com.beam.whatsapp.model;

import com.beam.whatsapp.model.status.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@TypeAlias("User")
@Document("User")
public class User extends Base {
    private String name;
    private String surname;
    private String number;
    private String password;
    private UserStatus status;
    private List<String> addedNumbers = new ArrayList<>();
    private String avatar;
    private Date lastSeen = new Date();
}
