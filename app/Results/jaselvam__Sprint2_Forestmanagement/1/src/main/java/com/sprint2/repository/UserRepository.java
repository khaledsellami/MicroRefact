package com.sprint2.repository;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sprint2.model.User;
@Repository
public interface UserRepository extends // all the methods present in the JpaRepository can be used by UserRepository
JpaRepository<User, String>{


}