package com.lingxiang2014.controller.admin;
 import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lingxiang2014.Message;
import com.lingxiang2014.Pageable;
import com.lingxiang2014.entity.Tag;
import com.lingxiang2014.entity.BaseEntity.Save;
import com.lingxiang2014.entity.Tag.Type;
import com.lingxiang2014.service.TagService;
@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController{

@Resource(name = "tagServiceImpl")
 private  TagService tagService;


@RequestMapping(value = "/add", method = RequestMethod.GET)
public String add(ModelMap model){
    model.addAttribute("types", Type.values());
    return "/admin/tag/add";
}


@RequestMapping(value = "/edit", method = RequestMethod.GET)
public String edit(Long id,ModelMap model){
    model.addAttribute("types", Type.values());
    model.addAttribute("tag", tagService.find(id));
    return "/admin/tag/edit";
}


@RequestMapping(value = "/save", method = RequestMethod.POST)
public String save(Tag tag,RedirectAttributes redirectAttributes){
    if (!isValid(tag, Save.class)) {
        return ERROR_VIEW;
    }
    tag.setArticles(null);
    tagService.save(tag);
    addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    return "redirect:list.jhtml";
}


@RequestMapping(value = "/update", method = RequestMethod.POST)
public String update(Tag tag,RedirectAttributes redirectAttributes){
    if (!isValid(tag)) {
        return ERROR_VIEW;
    }
    tagService.update(tag, "type", "articles", "products");
    addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
    return "redirect:list.jhtml";
}


@RequestMapping(value = "/list", method = RequestMethod.GET)
public String list(Pageable pageable,ModelMap model){
    model.addAttribute("page", tagService.findPage(pageable));
    return "/admin/tag/list";
}


@RequestMapping(value = "/delete", method = RequestMethod.POST)
@ResponseBody
public Message delete(Long[] ids){
    tagService.delete(ids);
    return SUCCESS_MESSAGE;
}


}