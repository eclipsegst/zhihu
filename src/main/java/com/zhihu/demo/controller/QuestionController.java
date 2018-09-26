package com.zhihu.demo.controller;

import com.zhihu.demo.model.Question;
import com.zhihu.demo.result.Result;
import com.zhihu.demo.service.QuestionService;
import com.zhihu.demo.service.UserService;
import com.zhihu.demo.util.WrapUtil;
import com.zhihu.demo.vo.QuestionVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    private WrapUtil wrapUtil;

    @Autowired
    public void setWrapUtil(WrapUtil wrapUtil) {
        this.wrapUtil = wrapUtil;
    }

    /**
     * 检索所有的问题  测
     *
     * @return 包含问题列表的result    注意列表可能为空
     */
    @ApiOperation(value = "检索所有的问题", notes = "检索所有的问题", httpMethod = "GET")
    @GetMapping("/getquestionlist")
    public Result<List<QuestionVo>> getQuestionList() {
        List<Question> list = questionService.getQuestionList();
        List<QuestionVo> voList = wrapUtil.wrap(list);
        return Result.success(voList);
    }

    /**
     * 根据用户id检索用户发布的问题    测
     *
     * @return 返回某个用户的所有问题
     */
    @ApiOperation(value = "检索个人问题", notes = "根据用户id检索用户发布的问题", httpMethod = "GET")
    @GetMapping("/getquestionlistbyuid/")
    public Result<List<Question>> getQuestionListByUID() {
        UserService userService = new UserService();
        return Result.success(questionService.getQuestionListByUID(Integer.parseInt(userService.getUserIdFromSecurity())));
    }

    /**
     * 增加一个问题   测
     *
     * @param question 要增加的问题对象
     * @return 返回一个result  增加成功的话data为要增加的对象，失败就失败
     */
    @ApiOperation(value = "增加一个问题", notes = "增加一个问题", httpMethod = "POST")
    @PostMapping("/addquestion")
    public Result<Question> addQuestion(@RequestBody @Valid Question question) {
        return questionService.addQuestion(question);
    }

    /**
     * 根据问题的id删除问题  测
     *
     * @param id 问题的id
     * @return 返回一个result  删除成功的话data为要增加的对象，失败就失败
     */
    @ApiOperation(value = "删除问题", notes = "根据问题的id删除问题", httpMethod = "GET")
    @GetMapping("/deletequestion/{qid}")
    public Result<Object> deleteQuestion(@PathVariable("qid") Integer id) {
        return questionService.deleteQuestion(id);
    }

    /**
     * 修改问题的评论数或者点赞数，或者浏览次数，
     *
     * @param question 要被修改的问题对象，具体需要问题的id和对应要修改的数据，
     * @return 返回一个result  修改成功的话data为要增加的对象，失败就失败
     */
    @ApiOperation(value = "修改评论或者点赞数", notes = "修改评论或者点赞数", httpMethod = "POST")
    @PostMapping("/modifyquestion")
    public Result<Question> modifyQuestion(@RequestBody @Valid Question question) {
        return questionService.modifyQuestion(question);
    }

    @ApiOperation(value = "得到qid列表对应的问题", notes = "得到qid列表对应的问题", httpMethod = "POST")
    @PostMapping("/getquestionsbyqids")
    public Result<List<Question>> getQuestionsByQids(@RequestBody @Valid Set<String> qidList) {
        List<String> list1 = new ArrayList<String>(qidList);
        List<Integer> list2 = parseIntegersList(list1);
        return questionService.getQuestionByQids(list2);
    }


    private List<Integer> parseIntegersList(List<String> StringList) {
        List<Integer> IntegerList = new ArrayList<Integer>();
        for (String x : StringList) {
            Integer z = Integer.parseInt(x);
            IntegerList.add(z);
        }
        return IntegerList;
    }
}
