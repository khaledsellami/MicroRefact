package com.cg.hbm.dao;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cg.hbm.entites.User;
@Repository
public interface IUserRepository extends JpaRepository<User, Integer>{


public User getUser(int user_id);

public void setUser(int user_id,User user);

}