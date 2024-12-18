package org.danyuan.application.resume.user.controller;
 import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.danyuan.application.common.base.BaseController;
import org.danyuan.application.common.base.BaseControllerImpl;
import org.danyuan.application.common.base.BaseResult;
import org.danyuan.application.common.base.ResultUtil;
import org.danyuan.application.common.utils.MailVo;
import org.danyuan.application.common.utils.SimapleMailRegist;
import org.danyuan.application.common.utils.excel.WordToHtml;
import org.danyuan.application.common.utils.files.TxtFilesReader;
import org.danyuan.application.common.utils.files.TxtFilesWriter;
import org.danyuan.application.common.utils.string.StringUtils;
import org.danyuan.application.resume.user.po.SysUserBaseInfo;
import org.danyuan.application.resume.user.service.SysUserBaseService;
import org.danyuan.application.softm.roles.vo.SysUserBaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
@RestController
@RequestMapping("/sysUserBaseInfo")
public class SysUserBaseInfoController extends BaseControllerImpl<SysUserBaseInfo>implements BaseController<SysUserBaseInfo>{

@Autowired
 private PasswordEncoder passwordEncoder;

 private  Logger logger;

@Autowired
 private  SysUserBaseService sysUserBaseService;

@Autowired
 private SimapleMailRegist mailRegist;


@RequestMapping(path = "/uploadResume")
public BaseResult<String> uploadResume(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    BaseResult<String> result = new BaseResult<>();
    // 文件保存
    request.setCharacterEncoding("UTF-8");
    String emailString = request.getParameter("email");
    // String username = request.getParameter("username");
    MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
    List<MultipartFile> files = multipartHttpServletRequest.getFiles("file");
    for (MultipartFile multipartFile : files) {
        String filename = multipartFile.getOriginalFilename();
        InputStream inputStream = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String path = System.getProperty("user.dir") + "/resume/" + simpleDateFormat.format(new Date());
        result.setData(simpleDateFormat.format(new Date()) + "/" + URLEncoder.encode(filename, "utf-8"));
        File file = new File(path);
        try {
            inputStream = multipartFile.getInputStream();
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "/" + filename;
            FileOutputStream fos = new FileOutputStream(path);
            byte[] b = new byte[1024];
            while ((inputStream.read(b)) != -1) {
                fos.write(b);
            }
            fos.close();
            inputStream.close();
            // word 转html
            path = System.getProperty("user.dir") + "/resume/" + simpleDateFormat.format(new Date());
            String imgPathString = System.getProperty("user.dir") + "/resume/";
            if (filename.toLowerCase().indexOf(".docx") > -1) {
                WordToHtml.Word2007ToHtml(path.replace("\\", "/"), filename);
                result.setData(simpleDateFormat.format(new Date()) + "/" + URLEncoder.encode(filename.substring(0, filename.lastIndexOf(".")), "utf-8") + ".html");
                // 重写图片位置
                replaceImgPath(path.replace("\\", "/"), filename.substring(0, filename.lastIndexOf(".")) + ".html", imgPathString);
            } else if (filename.toLowerCase().indexOf(".doc") > -1) {
                try {
                    WordToHtml.Word2003ToHtml(path.replace("\\", "/"), filename);
                // 重写图片位置
                } catch (OfficeXmlFileException e) {
                    WordToHtml.Word2007ToHtml(path.replace("\\", "/"), filename);
                // 重写图片位置
                }
                replaceImgPath(path.replace("\\", "/"), filename.substring(0, filename.lastIndexOf(".")) + ".html", imgPathString);
                result.setData(simpleDateFormat.format(new Date()) + "/" + URLEncoder.encode(filename.substring(0, filename.lastIndexOf(".")), "utf-8") + ".html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 更新信息
    SysUserBaseInfo entity = new SysUserBaseInfo();
    entity.setEmail(emailString);
    entity = sysUserBaseService.findOne(entity);
    MailVo mailMessage = new MailVo();
    StringBuilder sBuilder = new StringBuilder();
    if (entity == null) {
        // 新建
        SysUserBaseInfo info = new SysUserBaseInfo();
        info.setUuid(UUID.randomUUID().toString());
        info.setEmail(emailString);
        // info.setDeleteFlag(0);
        // info.setCreateUser(username);
        // info.setUpdateUser(username);
        info.setUserName(emailString);
        info.setPersionName(emailString);
        String codeString = StringUtils.genRandomNumByLen(8);
        info.setPassword(codeString);
        info.setResumePath("http://www.danyuan.wang/" + result.getData());
        encryptPassword(info);
        sysUserBaseService.save(info);
        // 
        sBuilder.append("欢迎您使用《初学者》《简历管理系统》：");
        sBuilder.append("\n 已为您创建好简历 并在系统中为您注册新的账户 ");
        sBuilder.append("\n 账号： " + info.getUserName());
        sBuilder.append("\n 默认密码： " + codeString);
        sBuilder.append("\n 您的简历地址：http://www.danyuan.wang/" + result.getData());
        sBuilder.append("\n 账号密码用户后期登录使用。");
        sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
        sBuilder.append("\n 初学者：http://www.danyuan.wang");
        sBuilder.append("\n 一个致力于使用代码改变生活的网站！");
    } else {
        sBuilder.append("欢迎您使用《初学者》《简历管理系统》：");
        sBuilder.append("\n 已为您创建好简历 ");
        sBuilder.append("\n 您的简历地址：http://www.danyuan.wang/" + result.getData());
        sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
        sBuilder.append("\n 初学者：http://www.danyuan.wang");
        sBuilder.append("\n 一个致力于使用代码改变生活的网站！");
        entity.setResumePath("http://www.danyuan.wang/" + result.getData());
        entity.setResumePath(result.getData());
        sysUserBaseService.save(entity);
    }
    mailMessage.setMessage(sBuilder.toString());
    mailMessage.setTitle("简历注册");
    mailMessage.setMail(emailString);
    // 发送邮件
    mailRegist.SendMailToCustom(mailMessage);
    // 生成简历二维码，个人名片信息
    return result;
}


public void encryptPassword(SysUserBaseInfo userEntity){
    String password = userEntity.getPassword();
    password = passwordEncoder.encode(password);
    userEntity.setPassword(password);
}


public void replaceImgPath(String path,String filename,String imgPathString){
    List<String> htmlString = TxtFilesReader.readFileByLines(path + "/" + filename);
    StringBuilder stringBuilder = new StringBuilder();
    for (String string : htmlString) {
        stringBuilder.append(string.replaceAll(imgPathString.replace("\\", "/"), "/"));
    }
    TxtFilesWriter.writeToFile(stringBuilder.toString(), path + "/" + filename);
}


@RequestMapping(path = "/sendMail", method = RequestMethod.POST)
public BaseResult<String> sendMail(SysUserBaseInfo info){
    MailVo mailMessage = new MailVo();
    String codeString = StringUtils.genRandomNumByLen(6);
    StringBuilder sBuilder = new StringBuilder();
    sBuilder.append("您正在使用《初学者》《简历管理》，您的注册码是：");
    sBuilder.append("\n  " + codeString + "");
    sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
    sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
    sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
    sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
    sBuilder.append("\n 请妥善保管好，不要发送给任何人，谢谢合作！");
    sBuilder.append("\n 初学者：http://www.danyuan.wang");
    sBuilder.append("\n 一个致力于使用代码改变生活的网站！");
    mailMessage.setMessage(sBuilder.toString());
    mailMessage.setTitle("简历注册验证码");
    mailMessage.setMail(info.getEmail());
    // 发送邮件
    mailRegist.SendMailToCustom(mailMessage);
    BaseResult<String> result = new BaseResult<>();
    result.setCode(200);
    result.setData(codeString);
    return result;
}


@RequestMapping(path = "/findAllBySearchText", method = RequestMethod.POST)
public Page<SysUserBaseInfo> findAllBySearchText(int pageNumber,int pageSize,SysUserBaseInfo sysUserBaseInfo){
    logger.info("findAllBySearchText", SysUserBaseInfoController.class);
    return sysUserBaseService.findAllBySearchText(pageNumber, pageSize, sysUserBaseInfo);
}


@RequestMapping(path = "/save", method = RequestMethod.POST)
@ResponseBody
public BaseResult<SysUserBaseInfo> save(SysUserBaseInfo info){
    logger.info("save", SysUserBaseInfoController.class);
    try {
        SysUserBaseInfo baseInfo = sysUserBaseService.findByName(info.getUserName());
        if (baseInfo == null) {
            info.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            encryptPassword(info);
            sysUserBaseService.save(info);
        } else {
            return ResultUtil.error("用户名已存在");
        }
        return ResultUtil.success();
    } catch (Exception e) {
        return ResultUtil.error(0, e.getMessage());
    }
}


@RequestMapping(path = "/checkUserName", method = RequestMethod.POST)
public Map<String,Boolean> checkUserName(String userName){
    logger.info("checkUserName", SysUserBaseInfoController.class);
    boolean boo = sysUserBaseService.checkUserName(userName);
    Map<String, Boolean> map = new HashMap<>();
    map.put("valid", boo);
    return map;
}


@RequestMapping(path = "/saveBaseinfo", method = RequestMethod.POST)
public BaseResult<SysUserBaseInfo> saveBaseinfo(SysUserBaseInfo info){
    logger.info("save", SysUserBaseInfoController.class);
    try {
        sysUserBaseService.saveBaseinfo(info);
        return ResultUtil.success();
    } catch (Exception e) {
        return ResultUtil.error(e.getMessage());
    }
}


@RequestMapping(path = "/saveu", method = RequestMethod.POST)
@ResponseBody
public String saveu(SysUserBaseInfo info){
    logger.info("saveu", SysUserBaseInfoController.class);
    try {
        sysUserBaseService.saveu(info);
        return "1";
    } catch (Exception e) {
        e.printStackTrace();
        return e.toString();
    }
}


@RequestMapping(path = "/findByUuid", method = RequestMethod.POST)
public SysUserBaseInfo findByUuid(SysUserBaseInfo info){
    logger.info("findAllBySearchText", SysUserBaseInfoController.class);
    return sysUserBaseService.findByUuid(info.getUuid());
}


@RequestMapping(path = "/changePassword", method = RequestMethod.POST)
@ResponseBody
public String changePassword(SysUserBaseVo vo){
    logger.info("changePassword", SysUserBaseInfoController.class);
    try {
        encryptPassword(vo.getInfo());
        sysUserBaseService.changePassword(vo.getInfo());
        return "1";
    } catch (Exception e) {
        return "0";
    }
}


}