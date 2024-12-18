package run.halo.app.service.impl;
 import org.springframework.stereotype.Service;
import run.halo.app.exception.ServiceException;
import run.halo.app.model.dto.StatisticDTO;
import run.halo.app.model.dto.StatisticWithUserDTO;
import run.halo.app.model.dto.UserDTO;
import run.halo.app.model.entity.User;
import run.halo.app.model.enums.CommentStatus;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.service.CategoryService;
import run.halo.app.service.JournalCommentService;
import run.halo.app.service.JournalService;
import run.halo.app.service.LinkService;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostCommentService;
import run.halo.app.service.PostService;
import run.halo.app.service.SheetCommentService;
import run.halo.app.service.SheetService;
import run.halo.app.service.StatisticService;
import run.halo.app.service.TagService;
import run.halo.app.service.UserService;
import run.halo.app.Interface.PostService;
import run.halo.app.Interface.SheetService;
import run.halo.app.Interface.JournalService;
import run.halo.app.Interface.PostCommentService;
import run.halo.app.Interface.SheetCommentService;
import run.halo.app.Interface.JournalCommentService;
import run.halo.app.Interface.OptionService;
import run.halo.app.Interface.CategoryService;
import run.halo.app.Interface.TagService;
import run.halo.app.Interface.UserService;
@Service
public class StatisticServiceImpl implements StatisticService{

 private  PostService postService;

 private  SheetService sheetService;

 private  JournalService journalService;

 private  PostCommentService postCommentService;

 private  SheetCommentService sheetCommentService;

 private  JournalCommentService journalCommentService;

 private  OptionService optionService;

 private  LinkService linkService;

 private  CategoryService categoryService;

 private  TagService tagService;

 private  UserService userService;

public StatisticServiceImpl(PostService postService, SheetService sheetService, JournalService journalService, PostCommentService postCommentService, SheetCommentService sheetCommentService, JournalCommentService journalCommentService, OptionService optionService, LinkService linkService, CategoryService categoryService, TagService tagService, UserService userService) {
    this.postService = postService;
    this.sheetService = sheetService;
    this.journalService = journalService;
    this.postCommentService = postCommentService;
    this.sheetCommentService = sheetCommentService;
    this.journalCommentService = journalCommentService;
    this.optionService = optionService;
    this.linkService = linkService;
    this.categoryService = categoryService;
    this.tagService = tagService;
    this.userService = userService;
}
@Override
public StatisticWithUserDTO getStatisticWithUser(){
    StatisticDTO statisticDto = getStatistic();
    StatisticWithUserDTO statisticWithUserDto = new StatisticWithUserDTO();
    statisticWithUserDto.convertFrom(statisticDto);
    User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
    statisticWithUserDto.setUser(new UserDTO().convertFrom(user));
    return statisticWithUserDto;
}


@Override
public StatisticDTO getStatistic(){
    StatisticDTO statisticDto = new StatisticDTO();
    statisticDto.setPostCount(postService.countByStatus(PostStatus.PUBLISHED));
    // Handle comment count
    long postCommentCount = postCommentService.countByStatus(CommentStatus.PUBLISHED);
    long sheetCommentCount = sheetCommentService.countByStatus(CommentStatus.PUBLISHED);
    long journalCommentCount = journalCommentService.countByStatus(CommentStatus.PUBLISHED);
    statisticDto.setCommentCount(postCommentCount + sheetCommentCount + journalCommentCount);
    statisticDto.setTagCount(tagService.count());
    statisticDto.setCategoryCount(categoryService.count());
    statisticDto.setJournalCount(journalService.count());
    long birthday = optionService.getBirthday();
    long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
    statisticDto.setEstablishDays(days);
    statisticDto.setBirthday(birthday);
    statisticDto.setLinkCount(linkService.count());
    statisticDto.setVisitCount(postService.countVisit() + sheetService.countVisit());
    statisticDto.setLikeCount(postService.countLike() + sheetService.countLike());
    return statisticDto;
}


}