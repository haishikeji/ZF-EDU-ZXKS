package com.px.obj.service.impl;

import com.px.common.utils.DateUtils;
import com.px.obj.domain.AppObjAnswer;
import com.px.obj.domain.AppObjExamine;
import com.px.obj.mapper.AppObjExamineMapper;
import com.px.obj.mapper.AppObjTopicMapper;
import com.px.obj.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考试Service业务层处理
 *
 * @author px
 * @date 2021-05-17
 */
@Service
public class AppObjExamineServiceImpl implements IAppObjExamineService {
    @Autowired
    private AppObjExamineMapper appObjExamineMapper;

    /**
     * 试卷
     */
    @Autowired
    private AppObjTopicMapper appObjTopicMapper;

    /**
     * 题库
     */
    @Autowired
    private IAppObjIssueService issueService;

    /**
     * 考生选项
     */
    @Autowired
    private IAppObjAnswerItemService answerItemService;

    /**
     * 考生答案
     */
    @Autowired
    private IAppObjAnswerService answerService;


    /**
     * 题库选项
     */
    @Autowired
    private IAppObjOptionService optionService;

    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    @Override
    public AppObjExamine selectAppObjExamineById(Long id) {
        return appObjExamineMapper.selectAppObjExamineById(id);
    }

    /**
     * 查询考试列表
     *
     * @param appObjExamine 考试
     * @return 考试
     */
    @Override
    public List<AppObjExamine> selectAppObjExamineList(AppObjExamine appObjExamine) {
        return appObjExamineMapper.selectAppObjExamineList(appObjExamine);
    }

    @Override
    public List<AppObjExamine> selectAppObjExamineListForUser(AppObjExamine appObjExamine) {
        return appObjExamineMapper.selectAppObjExamineListForUser(appObjExamine);
    }

    /**
     * 新增考试
     *
     * @param appObjExamine 考试
     * @return 结果
     */
    @Override
    public int insertAppObjExamine(AppObjExamine appObjExamine) {
        appObjExamine.setCreateTime(DateUtils.getNowDate());
        return appObjExamineMapper.insertAppObjExamine(appObjExamine);
    }

    /**
     * 修改考试
     *
     * @param appObjExamine 考试
     * @return 结果
     */
    @Override
    public int updateAppObjExamine(AppObjExamine appObjExamine) {
        return appObjExamineMapper.updateAppObjExamine(appObjExamine);
    }

    /**
     * 批量删除考试
     *
     * @param ids 需要删除的考试ID
     * @return 结果
     */
    @Override
    public int deleteAppObjExamineByIds(Long[] ids) {
        return appObjExamineMapper.deleteAppObjExamineByIds(ids);
    }

    /**
     * 删除考试信息
     *
     * @param id 考试ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteAppObjExamineById(Long id) {
        AppObjAnswer appObjAnswer = new AppObjAnswer();
        appObjAnswer.setExamineId(id);
        List<AppObjAnswer> appObjAnswerList = answerService.selectAppObjAnswerList(appObjAnswer);
        for (AppObjAnswer answer : appObjAnswerList) {//先循环删除每个答案的明细
            answerItemService.deleteAppObjAnswerItemByAnswer(answer.getId());
        }
        answerService.deleteAppObjAnswerByExamine(id);//删除答案
        int i = appObjExamineMapper.deleteAppObjExamineById(id);//删除成绩
        return i;
    }

    /**
     * 通过考试id获取考试结果
     *
     * @param examId
     * @return
     */
    @Override
    public AppObjExamine getTopicById(Long examId) {
        return appObjExamineMapper.selectAppObjExamineById(examId);
    }

    /**
     * 通过章节id和用户id获取考试结果
     *
     * @param chapterId 章节id
     * @param userId    用户id
     * @return
     */
    public AppObjExamine getExamById(Long chapterId, Long userId) {
        return appObjExamineMapper.selectAppObjExamineByChapterId(chapterId, userId);
    }

    /**
     * 获取登录人是否以答完
     *
     * @param objExamine
     * @return
     */
    @Override
    public List<AppObjExamine> getLoginExam(AppObjExamine objExamine) {
        return appObjExamineMapper.getLoginExam(objExamine);
    }

    /**
     * 针对用户成绩
     *
     * @param objExamine
     * @return
     */
    @Override
    public AppObjExamine selectAppObjExamineByTopicId(AppObjExamine objExamine) {
        return appObjExamineMapper.selectAppObjExamineByTopicId(objExamine);
    }
}
