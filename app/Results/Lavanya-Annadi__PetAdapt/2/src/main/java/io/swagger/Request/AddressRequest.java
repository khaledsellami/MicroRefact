package io.swagger.Request;
import io.swagger.DTO.Address;
public interface AddressRequest {

   public List<Address> getAddress(Long id);
   public void setAddress(List<Address> address,Long id);
}