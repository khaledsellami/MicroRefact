package com.gs.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.gs.Interface.UserService;
public class UserServiceImpl implements UserService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://11";


public List<User> queryByRoleName(String roleName){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/queryByRoleName"))
    .queryParam("roleName",roleName)
;  List<User> aux = restTemplate.getForObject(builder.toUriString(), List<User>.class);

 return aux;
}


}