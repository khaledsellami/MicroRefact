package org.gliderwiki.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.gliderwiki.Interface.AdminUserService;
public class AdminUserServiceImpl implements AdminUserService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://3";


public int insertUser(Map<Integer,Map> map){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/insertUser"))
    .queryParam("map",map)
;  int aux = restTemplate.getForObject(builder.toUriString(), int.class);

 return aux;
}


}