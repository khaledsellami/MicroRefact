package com.ushahidi.swiftriver.core.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.ushahidi.swiftriver.core.Interface.RuleDao;
public class RuleDaoImpl implements RuleDao{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://0";


public Object create(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/create"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public Object findById(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findById"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public Object update(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/update"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public Object delete(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/delete"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


}