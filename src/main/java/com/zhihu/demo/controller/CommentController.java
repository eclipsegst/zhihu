package com.zhihu.demo.controller;

import com.zhihu.demo.model.Comment;
import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.CommentService;
import com.zhihu.demo.vo.AddComment;
import com.zhihu.demo.vo.DeleteComment;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 根据问题的id查询对应的评论   测
     * @param qid   问题id
     * @param pageNum   要访问的页数
     * @param pageSize  页面的大小
     * @return  返回某个用户的所有问题
     */
    @ApiOperation(value = "查询评论",notes = "根据问题的id查询对应的评论",httpMethod = "GET")
    @GetMapping("/querycommentbyqid/{qid}/{pageNum}/{pageSize}")
    public Result<List<Comment>> queryCommentByQid(@PathVariable("qid") Integer qid,
                                                   @PathVariable("pageNum") Integer pageNum,
                                                   @PathVariable("pageSize") Integer pageSize) {
        return Result.success(commentService.queryCommentByQid(qid,pageNum,pageSize));
    }

    /**
     * 根据用户的id查询评论   测
     * @param uid  用户id
     * @return  对应的评论集
     */
    @ApiOperation(value = "查询个人评论",notes = "根据用户的id查询评论",httpMethod = "GET")
    @GetMapping("/querycommentbyuid/{uid}")
    public Result<List<List>> queryCommentByUid(@PathVariable("uid") Integer uid) {
        return Result.success(commentService.queryCommentByUid(uid));
    }

    /**
     * 添加一个评论  同时修改对应问题的评论数    测
     * @param addComment  要添加的评论包括 content u_id q_id cnum
     * @return 成功的话result里面的data是 修改了评论数的问题对象， 失败就没有
     */
    @ApiOperation(value = "添加评论",notes = "添加一个评论,同时修改对应问题的评论数",httpMethod = "POST")
    @PostMapping("/addcomment")
    public Result<Question> addComment(@Valid AddComment addComment){
        return commentService.addComment(addComment.getComment(),addComment.getQuestion());
    }

    /**
     * 删除一个评论，同时修改对应问题的评论    测
     * @param deleteComment  要添加的评论包括  u_id q_id cnum
     * @return  成功的话result里面的data是 修改了评论数的问题对象， 失败就没有
     */
    @ApiOperation(value = "删除评论",notes = "删除一个评论，同时修改对应问题的评论 ",httpMethod = "POST")
    @PostMapping("/deletecomment")
    public Result<Question> deleteComment(@Valid DeleteComment deleteComment){
        return commentService.deleteComment(deleteComment.getComment(), deleteComment.getQuestion());
    }

    /**
     * 修改评论的点赞数     测
     * @param comment   要修改的评论    就传入id 和点赞数
     * @return  成功的话result里面的data就是这个评论对象
     */
    @ApiOperation(value = "修改评论的点赞数",notes = "修改评论的点赞数 ",httpMethod = "POST")
    @PostMapping("/modifycomment")
    public Result<Comment> modifyComment(@Valid Comment comment){
        return commentService.modifyComment(comment);
    }

}
