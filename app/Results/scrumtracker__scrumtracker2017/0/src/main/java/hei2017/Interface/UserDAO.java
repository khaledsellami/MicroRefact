package hei2017.Interface;
public interface UserDAO {

   public Set<User> findByUserTasksId(Long id);
}