package pl.edu.wat.wcy.pz.restaurantServer.Request;
import pl.edu.wat.wcy.pz.restaurantServer.DTO.Reservation;
import java.util.*;
public interface ReservationRequest {

   public void setReservations(List<Reservation> reservations,Long userId);
   public List<Reservation> getReservations(Long userId);
}