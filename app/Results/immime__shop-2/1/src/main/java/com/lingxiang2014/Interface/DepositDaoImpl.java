package com.lingxiang2014.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.lingxiang2014.Interface.DepositDao;
public class DepositDaoImpl implements DepositDao{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://0";


public Object persist(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/persist"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


}