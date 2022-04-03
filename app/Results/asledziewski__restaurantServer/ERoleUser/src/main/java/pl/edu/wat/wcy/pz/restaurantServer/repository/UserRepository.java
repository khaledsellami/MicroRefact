package pl.edu.wat.wcy.pz.restaurantServer.repository;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.pz.restaurantServer.entity.User;
import javax.transaction.Transactional;
import java.util.Optional;
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long>{


public Boolean existsByMail(String mail)
;

public Optional<User> findByMail(String mail)
;

public void setPassword(Long id,String password);

public void setUserId(Long id,Long userId);

}