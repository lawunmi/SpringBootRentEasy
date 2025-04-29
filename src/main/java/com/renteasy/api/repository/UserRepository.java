package com.renteasy.api.repository;

import com.renteasy.api.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
   /* @Query(value = "{ '_id' : ?0 }", fields = "{ 'username' : 1, '_id': 0 }")
    String findUsernameById(String id);*/
}