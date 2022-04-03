package cn.gson.oasys.model.dao.discuss;
 import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import cn.gson.oasys.model.entity.discuss.Comment;
import cn.gson.oasys.model.entity.discuss.Reply;
public interface CommentDao extends JpaRepository<Comment, Long>{


@Query("from Comment t where t.reply.replyId in (?1)")
public List<Comment> findComments(Long[] taskids)
;

public List<Comment> findByReply(Reply reply)
;

}