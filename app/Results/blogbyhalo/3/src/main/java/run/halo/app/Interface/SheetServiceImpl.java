package run.halo.app.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import run.halo.app.Interface.SheetService;
public class SheetServiceImpl implements SheetService{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://10";


public Object getBySlug(Object Object){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/getBySlug"))
    .queryParam("Object",Object)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


}