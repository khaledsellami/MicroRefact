package cn.com.cnc.fcc.Interface;
 import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import cn.com.cnc.fcc.Interface.PageUtil;
public class PageUtilImpl implements PageUtil{

@Autowired
 private RestTemplate restTemplate;

  String url = "http://6";


}