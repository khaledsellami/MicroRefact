package Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
public class SqlViewFactoryImpl implements SqlViewFactory{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://6";


public void setDefaultProfileId(WindowId windowId,ViewProfileId profileId){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/setDefaultProfileId"))
    .queryParam("windowId",windowId)
    .queryParam("profileId",profileId);

  restTemplate.put(builder.toUriString(), null);
}


}