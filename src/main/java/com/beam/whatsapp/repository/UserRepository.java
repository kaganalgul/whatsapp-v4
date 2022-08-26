package com.beam.whatsapp.repository;

import com.beam.whatsapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    public Optional<User> findByNumber(String number);
}
