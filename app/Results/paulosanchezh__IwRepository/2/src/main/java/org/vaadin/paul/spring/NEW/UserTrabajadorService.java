package org.vaadin.paul.spring.NEW;
 import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.paul.spring.repositories.UserRepository;
import org.vaadin.paul.spring.entities.User;
@Service
public class UserTrabajadorService {

@Autowired
 private UserRepository userrepository;


public User getUser(int id){
return userrepository.getUser(id);
}


}