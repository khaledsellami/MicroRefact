package upce.semprace.eshop.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import upce.semprace.eshop.Interface.UzivatelRepository;
public class UzivatelRepositoryImpl implements UzivatelRepository{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://7";


public Optional<Uzivatel> findById(Long id){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/findById"))
    .queryParam("id",id)
;  Optional<Uzivatel> aux = restTemplate.getForObject(builder.toUriString(), Optional<Uzivatel>.class);

 return aux;
}


}