package org.vaadin.paul.spring.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.paul.spring.Interface.UserRepository;
public class UserRepositoryImpl implements UserRepository{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://7";


public User findByid(int i){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByid"))
    .queryParam("i",i)
;  User aux = restTemplate.getForObject(builder.toUriString(), User.class);

 return aux;
}


}