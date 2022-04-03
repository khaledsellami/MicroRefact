package edu.nju.careerbridge.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import edu.nju.careerbridge.Interface.SkillRepository;
public class SkillRepositoryImpl implements SkillRepository{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://3";


public void deleteByPhone(String phone){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/deleteByPhone"))
    .queryParam("phone",phone)
;
  restTemplate.put(builder.toUriString(), null);
}


public Object save(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/save"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public List<Skill> findByPhone(String phone){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findByPhone"))
    .queryParam("phone",phone)
;  List<Skill> aux = restTemplate.getForObject(builder.toUriString(), List<Skill>.class);

 return aux;
}


}