package run.halo.app.service.impl;
 import run.halo.app.model.support.HaloConst.URL_SEPARATOR;
import cn.hutool.core.date.DateUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import run.halo.app.exception.BadRequestException;
import run.halo.app.exception.ForbiddenException;
import run.halo.app.exception.NotFoundException;
import run.halo.app.model.dto.post.BasePostMinimalDTO;
import run.halo.app.model.entity.Post;
import run.halo.app.model.entity.PostComment;
import run.halo.app.model.enums.CommentViolationTypeEnum;
import run.halo.app.model.enums.PostPermalinkType;
import run.halo.app.model.properties.CommentProperties;
import run.halo.app.model.vo.PostCommentWithPostVO;
import run.halo.app.repository.PostCommentRepository;
import run.halo.app.repository.PostRepository;
import run.halo.app.service.CommentBlackListService;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostCommentService;
import run.halo.app.service.UserService;
import run.halo.app.utils.ServiceUtils;
import run.halo.app.utils.ServletUtils;
import run.halo.app.Interface.PostRepository;
import run.halo.app.Interface.CommentBlackListService;
@Slf4j
@Service
public class PostCommentServiceImpl extends BaseCommentServiceImpl<PostComment>implements PostCommentService{

 private  PostRepository postRepository;

 private  CommentBlackListService commentBlackListService;

public PostCommentServiceImpl(PostCommentRepository postCommentRepository, PostRepository postRepository, UserService userService, OptionService optionService, CommentBlackListService commentBlackListService, ApplicationEventPublisher eventPublisher) {
    super(postCommentRepository, optionService, userService, eventPublisher);
    this.postRepository = postRepository;
    this.commentBlackListService = commentBlackListService;
}
public BasePostMinimalDTO buildPostFullPath(BasePostMinimalDTO post){
    PostPermalinkType permalinkType = optionService.getPostPermalinkType();
    String pathSuffix = optionService.getPathSuffix();
    String archivesPrefix = optionService.getArchivesPrefix();
    int month = DateUtil.month(post.getCreateTime()) + 1;
    String monthString = month < 10 ? "0" + month : String.valueOf(month);
    int day = DateUtil.dayOfMonth(post.getCreateTime());
    String dayString = day < 10 ? "0" + day : String.valueOf(day);
    StringBuilder fullPath = new StringBuilder();
    if (optionService.isEnabledAbsolutePath()) {
        fullPath.append(optionService.getBlogBaseUrl());
    }
    fullPath.append(URL_SEPARATOR);
    if (permalinkType.equals(PostPermalinkType.DEFAULT)) {
        fullPath.append(archivesPrefix).append(URL_SEPARATOR).append(post.getSlug()).append(pathSuffix);
    } else if (permalinkType.equals(PostPermalinkType.ID)) {
        fullPath.append("?p=").append(post.getId());
    } else if (permalinkType.equals(PostPermalinkType.DATE)) {
        fullPath.append(DateUtil.year(post.getCreateTime())).append(URL_SEPARATOR).append(monthString).append(URL_SEPARATOR).append(post.getSlug()).append(pathSuffix);
    } else if (permalinkType.equals(PostPermalinkType.DAY)) {
        fullPath.append(DateUtil.year(post.getCreateTime())).append(URL_SEPARATOR).append(monthString).append(URL_SEPARATOR).append(dayString).append(URL_SEPARATOR).append(post.getSlug()).append(pathSuffix);
    } else if (permalinkType.equals(PostPermalinkType.YEAR)) {
        fullPath.append(DateUtil.year(post.getCreateTime())).append(URL_SEPARATOR).append(post.getSlug()).append(pathSuffix);
    } else if (permalinkType.equals(PostPermalinkType.ID_SLUG)) {
        fullPath.append(archivesPrefix).append(URL_SEPARATOR).append(post.getId()).append(pathSuffix);
    }
    post.setFullPath(fullPath.toString());
    return post;
}


@Override
public void validateTarget(Integer postId){
    Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));
    if (post.getDisallowComment()) {
        throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
    }
}


@Override
public void validateCommentBlackListStatus(){
    CommentViolationTypeEnum banStatus = commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
    Integer banTime = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
    if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
        throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
    }
}


@Override
@NonNull
public List<PostCommentWithPostVO> convertToWithPostVo(List<PostComment> postComments){
    if (CollectionUtils.isEmpty(postComments)) {
        return Collections.emptyList();
    }
    // Fetch goods ids
    Set<Integer> postIds = ServiceUtils.fetchProperty(postComments, PostComment::getPostId);
    // Get all posts
    Map<Integer, Post> postMap = ServiceUtils.convertToMap(postRepository.findAllById(postIds), Post::getId);
    return postComments.stream().filter(comment -> postMap.containsKey(comment.getPostId())).map(comment -> {
        // Convert to vo
        PostCommentWithPostVO postCommentWithPostVo = new PostCommentWithPostVO().convertFrom(comment);
        BasePostMinimalDTO basePostMinimalDto = new BasePostMinimalDTO().convertFrom(postMap.get(comment.getPostId()));
        postCommentWithPostVo.setPost(buildPostFullPath(basePostMinimalDto));
        postCommentWithPostVo.setAvatar(buildAvatarUrl(comment.getGravatarMd5()));
        return postCommentWithPostVo;
    }).collect(Collectors.toList());
}


}