package com.beam.whatsapp.repository;

import com.beam.whatsapp.model.Message;
import com.beam.whatsapp.model.Session;
import com.beam.whatsapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface SessionRepository extends MongoRepository<Session, String>  {
    public List<Session> findByUsersId(String userId);

}
