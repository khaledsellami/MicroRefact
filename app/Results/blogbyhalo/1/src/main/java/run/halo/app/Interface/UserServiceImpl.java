package run.halo.app.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import run.halo.app.Interface.UserService;
public class UserServiceImpl implements UserService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://6";


public User createBy(UserParam userParam){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/createBy"))
    .queryParam("userParam",userParam)
;  User aux = restTemplate.getForObject(builder.toUriString(), User.class);

 return aux;
}


public void setPassword(User user,String plainPassword){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setPassword"))
    .queryParam("user",user)
    .queryParam("plainPassword",plainPassword)
;
  restTemplate.put(builder.toUriString(), null);
}


public Object update(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/update"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public Optional<User> getCurrentUser(){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getCurrentUser"))
;  Optional<User> aux = restTemplate.getForObject(builder.toUriString(), Optional<User>.class);

 return aux;
}


public Object map(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/map"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


}