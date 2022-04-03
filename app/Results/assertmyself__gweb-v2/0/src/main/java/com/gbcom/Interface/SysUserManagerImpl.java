package com.gbcom.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.gbcom.Interface.SysUserManager;
public class SysUserManagerImpl implements SysUserManager{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://2";


public Boolean hasPrivilege(Long userId,String privilegeCode){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/hasPrivilege"))
    .queryParam("userId",userId)
    .queryParam("privilegeCode",privilegeCode)
;  Boolean aux = restTemplate.getForObject(builder.toUriString(), Boolean.class);

 return aux;
}


}