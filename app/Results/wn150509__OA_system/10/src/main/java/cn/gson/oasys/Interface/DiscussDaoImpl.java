package cn.gson.oasys.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import cn.gson.oasys.Interface.DiscussDao;
public class DiscussDaoImpl implements DiscussDao{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://12";


}