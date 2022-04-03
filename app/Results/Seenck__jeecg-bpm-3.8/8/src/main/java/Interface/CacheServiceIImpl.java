package Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import Interface.CacheServiceI;
public class CacheServiceIImpl implements CacheServiceI{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://10";


public Object get(String cacheName,Object key){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/get"))
    .queryParam("cacheName",cacheName)
    .queryParam("key",key)
;  Object aux = restTemplate.getForObject(builder.toUriString(), Object.class);

 return aux;
}


public void put(String cacheName,Object key,Object value){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/put"))
    .queryParam("cacheName",cacheName)
    .queryParam("key",key)
    .queryParam("value",value)
;
  restTemplate.put(builder.toUriString(), null);
}


public boolean remove(String cacheName,Object key){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/remove"))
    .queryParam("cacheName",cacheName)
    .queryParam("key",key);
  boolean aux = restTemplate.getForObject(builder.toUriString(), boolean.class);

 return aux;
}


public void clean(String cacheName){
  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/clean"))
    .queryParam("cacheName",cacheName);

  restTemplate.put(builder.toUriString(), null);
}


}