package com.beam.whatsapp.repository;

import com.beam.whatsapp.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

}
