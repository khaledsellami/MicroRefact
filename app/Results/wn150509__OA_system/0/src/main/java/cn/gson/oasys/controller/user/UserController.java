package cn.gson.oasys.controller.user;
 import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.util.StringUtil;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import cn.gson.oasys.model.dao.roledao.RoleDao;
import cn.gson.oasys.model.dao.user.DeptDao;
import cn.gson.oasys.model.dao.user.PositionDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.role.Role;
import cn.gson.oasys.model.entity.user.Dept;
import cn.gson.oasys.model.entity.user.Position;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.Interface.UserDao;
import cn.gson.oasys.Interface.DeptDao;
import cn.gson.oasys.Interface.PositionDao;
import cn.gson.oasys.Interface.RoleDao;
@Controller
@RequestMapping("/")
public class UserController {

@Autowired
 private UserDao udao;

@Autowired
 private DeptDao ddao;

@Autowired
 private PositionDao pdao;

@Autowired
 private RoleDao rdao;


@RequestMapping(value = "useredit", method = RequestMethod.GET)
public String usereditget(Long userid,Model model){
    if (userid != null) {
        User user = udao.findOne(userid);
        model.addAttribute("where", "xg");
        model.addAttribute("user", user);
    }
    List<Dept> depts = (List<Dept>) ddao.findAll();
    List<Position> positions = (List<Position>) pdao.findAll();
    List<Role> roles = (List<Role>) rdao.findAll();
    model.addAttribute("depts", depts);
    model.addAttribute("positions", positions);
    model.addAttribute("roles", roles);
    return "user/edituser";
}


@RequestMapping("usermanagepaging")
public String userPaging(Model model,int page,int size,String usersearch){
    Sort sort = new Sort(new Order(Direction.ASC, "dept"));
    Pageable pa = new PageRequest(page, size, sort);
    Page<User> userspage = null;
    if (StringUtil.isEmpty(usersearch)) {
        userspage = udao.findByIsLock(0, pa);
    } else {
        System.out.println(usersearch);
        userspage = udao.findnamelike(usersearch, pa);
    }
    List<User> users = userspage.getContent();
    model.addAttribute("users", users);
    model.addAttribute("page", userspage);
    model.addAttribute("url", "usermanagepaging");
    return "user/usermanagepaging";
}


@RequestMapping("selectdept")
@ResponseBody
public List<Position> selectdept(Long deptid){
    return pdao.findByDeptidAndNameNotLike(deptid, "%经理");
}


@RequestMapping("userlogmanage")
public String userlogmanage(){
    return "user/userlogmanage";
}


@RequestMapping("useronlyname")
@ResponseBody
public boolean useronlyname(String username){
    System.out.println(username);
    User user = udao.findByUserName(username);
    System.out.println(user);
    if (user == null) {
        return true;
    }
    return false;
}


@RequestMapping("usermanage")
public String usermanage(Model model,int page,int size){
    Sort sort = new Sort(new Order(Direction.ASC, "dept"));
    Pageable pa = new PageRequest(page, size, sort);
    Page<User> userspage = udao.findByIsLock(0, pa);
    List<User> users = userspage.getContent();
    model.addAttribute("users", users);
    model.addAttribute("page", userspage);
    model.addAttribute("url", "usermanagepaging");
    return "user/usermanage";
}


@RequestMapping("deleteuser")
public String deleteuser(Long userid,Model model){
    User user = udao.findOne(userid);
    user.setIsLock(1);
    udao.save(user);
    model.addAttribute("success", 1);
    return "/usermanage";
}


@RequestMapping(value = "useredit", method = RequestMethod.POST)
public String usereditpost(User user,Long deptid,Long positionid,Long roleid,boolean isbackpassword,Model model){
    System.out.println(user);
    System.out.println(deptid);
    System.out.println(positionid);
    System.out.println(roleid);
    Dept dept = ddao.findOne(deptid);
    Position position = pdao.findOne(positionid);
    Role role = rdao.findOne(roleid);
    if (user.getUserId() == null) {
        String pinyin = PinyinHelper.convertToPinyinString(user.getUserName(), "", PinyinFormat.WITHOUT_TONE);
        user.setPinyin(pinyin);
        user.setPassword("123456");
        user.setDept(dept);
        user.setRole(role);
        user.setPosition(position);
        user.setFatherId(dept.getDeptmanager());
        udao.save(user);
    } else {
        User user2 = udao.findOne(user.getUserId());
        user2.setUserTel(user.getUserTel());
        user2.setRealName(user.getRealName());
        user2.setEamil(user.getEamil());
        user2.setAddress(user.getAddress());
        user2.setUserEdu(user.getUserEdu());
        user2.setSchool(user.getSchool());
        user2.setIdCard(user.getIdCard());
        user2.setBank(user.getBank());
        user2.setThemeSkin(user.getThemeSkin());
        user2.setSalary(user.getSalary());
        user2.setFatherId(dept.getDeptmanager());
        if (isbackpassword) {
            user2.setPassword("123456");
        }
        user2.setDept(dept);
        user2.setRole(role);
        user2.setPosition(position);
        udao.save(user2);
    }
    model.addAttribute("success", 1);
    return "/usermanage";
}


}