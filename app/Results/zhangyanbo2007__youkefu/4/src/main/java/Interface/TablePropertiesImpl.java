package Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import Interface.TableProperties;
public class TablePropertiesImpl implements TableProperties{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://3";


}