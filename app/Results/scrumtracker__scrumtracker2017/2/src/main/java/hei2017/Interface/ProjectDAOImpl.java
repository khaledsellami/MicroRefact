package hei2017.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import hei2017.Interface.ProjectDAO;
public class ProjectDAOImpl implements ProjectDAO{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://3";


}