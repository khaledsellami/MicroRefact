package cn.gson.oasys.controller.user;
 import cn.gson.oasys.common.formValid.BindingResultVOUtil;
import cn.gson.oasys.common.formValid.MapToList;
import cn.gson.oasys.common.formValid.ResultEnum;
import cn.gson.oasys.common.formValid.ResultVO;
import cn.gson.oasys.model.dao.informdao.InformRelationDao;
import cn.gson.oasys.model.dao.maildao.MailreciverDao;
import cn.gson.oasys.model.dao.processdao.NotepaperDao;
import cn.gson.oasys.model.dao.user.DeptDao;
import cn.gson.oasys.model.dao.user.PositionDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.mail.Mailreciver;
import cn.gson.oasys.model.entity.notice.NoticeUserRelation;
import cn.gson.oasys.model.entity.process.Notepaper;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.user.NotepaperService;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import cn.gson.oasys.Interface.UserDao;
import cn.gson.oasys.Interface.DeptDao;
import cn.gson.oasys.Interface.PositionDao;
import cn.gson.oasys.Interface.MailreciverDao;
import cn.gson.oasys.DTO.ResultVO;
@Controller
@RequestMapping("/")
public class UserpanelController {

@Autowired
 private  UserDao udao;

@Autowired
 private  DeptDao ddao;

@Autowired
 private  PositionDao pdao;

@Autowired
 private  InformRelationDao irdao;

@Autowired
 private  MailreciverDao mdao;

@Autowired
 private  NotepaperDao ndao;

@Autowired
 private  NotepaperService nservice;

@Value("${img.rootpath}")
 private  String rootpath;


@RequestMapping("image/**")
public void image(Model model,HttpServletResponse response,Long userId,HttpServletRequest request){
    String projectPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    System.out.println(projectPath);
    String startpath = new String(URLDecoder.decode(request.getRequestURI(), "utf-8"));
    String path = startpath.replace("/image", "");
    File f = new File(rootpath, path);
    ServletOutputStream sos = response.getOutputStream();
    FileInputStream input = new FileInputStream(f.getPath());
    byte[] data = new byte[(int) f.length()];
    IOUtils.readFully(input, data);
    // 将文件流输出到浏览器
    IOUtils.write(data, sos);
    input.close();
    sos.close();
}


@RequestMapping("notepaper")
public String deletepaper(HttpServletRequest request,Long userId){
    User user = udao.findOne(userId);
    String paperid = request.getParameter("id");
    Long lpid = Long.parseLong(paperid);
    Notepaper note = ndao.findOne(lpid);
    if (user.getUserId().equals(note.getUserId().getUserId())) {
        nservice.delete(lpid);
    } else {
        System.out.println("权限不匹配，不能删除");
        return "redirect:/notlimit";
    }
    return "redirect:/userpanel";
}


@RequestMapping("panel")
public String index(Long userId,Model model,int page,int size){
    Pageable pa = new PageRequest(page, size);
    User user = udao.findOne(userId);
    // 找便签
    Page<Notepaper> list = ndao.findByUserIdOrderByCreateTimeDesc(user, pa);
    List<Notepaper> notepaperlist = list.getContent();
    model.addAttribute("notepaperlist", notepaperlist);
    model.addAttribute("page", list);
    model.addAttribute("url", "panel");
    return "user/panel";
}


@RequestMapping("saveuser")
public String saveemp(MultipartFile filePath,HttpServletRequest request,User user,BindingResult br,Long userId){
    String imgpath = nservice.upload(filePath);
    User users = udao.findOne(userId);
    // 重新set用户
    users.setRealName(user.getRealName());
    users.setUserTel(user.getUserTel());
    users.setEamil(user.getEamil());
    users.setAddress(user.getAddress());
    users.setUserEdu(user.getUserEdu());
    users.setSchool(user.getSchool());
    users.setIdCard(user.getIdCard());
    users.setBank(user.getBank());
    users.setSex(user.getSex());
    users.setThemeSkin(user.getThemeSkin());
    users.setBirth(user.getBirth());
    if (!StringUtil.isEmpty(user.getUserSign())) {
        users.setUserSign(user.getUserSign());
    }
    if (!StringUtil.isEmpty(user.getPassword())) {
        users.setPassword(user.getPassword());
    }
    if (!StringUtil.isEmpty(imgpath)) {
        users.setImgPath(imgpath);
    }
    request.setAttribute("users", users);
    ResultVO res = BindingResultVOUtil.hasErrors(br);
    if (!ResultEnum.SUCCESS.getCode().equals(res.getCode())) {
        List<Object> list = new MapToList<>().mapToList(res.getData());
        request.setAttribute("errormess", list.get(0).toString());
        System.out.println("list错误的实体类信息：" + user);
        System.out.println("list错误详情:" + list);
        System.out.println("list错误第一条:" + list.get(0));
        System.out.println("啊啊啊错误的信息——：" + list.get(0).toString());
    } else {
        udao.save(users);
        request.setAttribute("success", "执行成功！");
    }
    return "forward:/userpanel";
}


public void UserpanelController(){
    try {
        rootpath = ResourceUtils.getURL("classpath:").getPath().replace("/target/classes/", "/static/image");
    // System.out.println(rootpath);
    } catch (IOException e) {
        System.out.println("获取项目路径异常");
    }
}


@RequestMapping("writep")
public String savepaper(Notepaper npaper,Long userId,String concent){
    User user = udao.findOne(userId);
    npaper.setCreateTime(new Date());
    npaper.setUserId(user);
    System.out.println("内容" + npaper.getConcent());
    if (npaper.getTitle() == null || npaper.getTitle().equals(""))
        npaper.setTitle("无标题");
    if (npaper.getConcent() == null || npaper.getConcent().equals(""))
        npaper.setConcent(concent);
    ndao.save(npaper);
    return "redirect:/userpanel";
}


}