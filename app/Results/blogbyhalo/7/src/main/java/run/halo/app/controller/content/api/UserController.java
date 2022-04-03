package run.halo.app.controller.content.api;
 import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.halo.app.model.dto.UserDTO;
import run.halo.app.service.UserService;
import run.halo.app.Interface.UserService;
@RestController("ApiContentUserController")
@RequestMapping("/api/content/users")
public class UserController {

 private  UserService userService;

public UserController(UserService userService) {
    this.userService = userService;
}
@GetMapping("profile")
@ApiOperation("Gets blogger profile")
public UserDTO getProfile(){
    return userService.getCurrentUser().map(user -> (UserDTO) new UserDTO().convertFrom(user)).get();
}


}