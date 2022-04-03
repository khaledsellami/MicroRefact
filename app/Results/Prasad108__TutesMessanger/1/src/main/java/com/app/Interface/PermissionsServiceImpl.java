package com.app.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.app.Interface.PermissionsService;
public class PermissionsServiceImpl implements PermissionsService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://0";


public Permissions find(int id){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/find"))
    .queryParam("id",id)
;  Permissions aux = restTemplate.getForObject(builder.toUriString(), Permissions.class);

 return aux;
}


}