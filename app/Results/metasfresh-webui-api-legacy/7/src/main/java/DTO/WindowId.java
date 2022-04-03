package DTO;
 import java.util.OptionalInt;
import javax.annotation.Nullable;
import org.adempiere.ad.element.api.AdWindowId;
import org.adempiere.exceptions.AdempiereException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.metas.util.Check;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
public class WindowId {

 private  String value;

 private  OptionalInt valueInt;

 private RestTemplate restTemplate = new RestTemplate();

  String url = "http://5";


public WindowId ofNullable(AdWindowId adWindowId){
    return adWindowId != null ? new WindowId(adWindowId.getRepoId()) : null;
 

  UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.concat("/ofNullable"))

.queryParam("adWindowId",adWindowId);
WindowId aux = restTemplate.getForObject(builder.toUriString(),WindowId.class);
return aux;
}


}