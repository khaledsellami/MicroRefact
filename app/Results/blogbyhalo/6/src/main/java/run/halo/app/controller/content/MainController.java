package run.halo.app.controller.content;
 import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.exception.ServiceException;
import run.halo.app.model.entity.User;
import run.halo.app.model.properties.BlogProperties;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.OptionService;
import run.halo.app.service.UserService;
import run.halo.app.utils.HaloUtils;
import run.halo.app.Interface.OptionService;
import run.halo.app.Interface.HaloProperties;
@Controller
public class MainController {

 private  String INDEX_REDIRECT_URI;

 private  String INSTALL_REDIRECT_URI;

 private  UserService userService;

 private  OptionService optionService;

 private  HaloProperties haloProperties;

public MainController(UserService userService, OptionService optionService, HaloProperties haloProperties) {
    this.userService = userService;
    this.optionService = optionService;
    this.haloProperties = haloProperties;
}
@GetMapping("favicon.ico")
public void favicon(HttpServletResponse response){
    String favicon = optionService.getByProperty(BlogProperties.BLOG_FAVICON).orElse("").toString();
    if (StringUtils.isNotEmpty(favicon)) {
        response.sendRedirect(HaloUtils.normalizeUrl(favicon));
    }
}


@GetMapping("install")
public void installation(HttpServletResponse response){
    String installRedirectUri = StringUtils.appendIfMissing(this.haloProperties.getAdminPath(), "/") + INSTALL_REDIRECT_URI;
    response.sendRedirect(installRedirectUri);
}


@GetMapping("${halo.admin-path:admin}")
public void admin(HttpServletResponse response){
    String adminIndexRedirectUri = HaloUtils.ensureBoth(haloProperties.getAdminPath(), HaloUtils.URL_SEPARATOR) + INDEX_REDIRECT_URI;
    response.sendRedirect(adminIndexRedirectUri);
}


@GetMapping("logo")
public void logo(HttpServletResponse response){
    String blogLogo = optionService.getByProperty(BlogProperties.BLOG_LOGO).orElse("").toString();
    if (StringUtils.isNotEmpty(blogLogo)) {
        response.sendRedirect(HaloUtils.normalizeUrl(blogLogo));
    }
}


@GetMapping("avatar")
public void avatar(HttpServletResponse response){
    User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
    if (StringUtils.isNotEmpty(user.getAvatar())) {
        response.sendRedirect(HaloUtils.normalizeUrl(user.getAvatar()));
    }
}


@GetMapping("version")
@ResponseBody
public String version(){
    return HaloConst.HALO_VERSION;
}


}