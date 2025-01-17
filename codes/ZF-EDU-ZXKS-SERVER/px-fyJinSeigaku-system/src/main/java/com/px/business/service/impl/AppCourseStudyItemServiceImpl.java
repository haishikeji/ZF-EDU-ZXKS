package com.px.business.service.impl;

import com.px.business.domain.AppCourseStudy;
import com.px.business.domain.AppCourseStudyItem;
import com.px.business.domain.vo.AppCourseChapterVo;
import com.px.business.domain.vo.AppCourseStudyItemVo;
import com.px.business.mapper.AppCourseStudyItemMapper;
import com.px.business.mapper.AppCourseStudyMapper;
import com.px.business.service.IAppCourseStudyItemService;
import com.px.common.utils.DateUtils;
import com.px.obj.mapper.AppObjCertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程学习子Service业务层处理
 *
 * @author px
 * @date 2021-04-29
 */
@Service
public class AppCourseStudyItemServiceImpl implements IAppCourseStudyItemService {
    @Autowired
    private AppCourseStudyItemMapper appCourseStudyItemMapper;

    @Autowired
    private AppCourseStudyMapper appCourseStudyMapper;

    @Autowired
    private AppObjCertificateMapper appObjCertificateMapper;

    /**
     * 查询课程学习子
     *
     * @param courseId 课程学习子ID
     * @return 课程学习子
     */
    @Override
    public AppCourseStudyItem selectAppCourseStudyItemById(Long courseId) {
        return appCourseStudyItemMapper.selectAppCourseStudyItemById(courseId);
    }

    /**
     * 查询课程播放数
     *
     * @param courseId 课程学习子ID
     * @return 课程学习子
     */
    @Override
    public Long selectAppCourseStudyItemNumValue(Long courseId) {
        return appCourseStudyItemMapper.selectAppCourseStudyItemNumValue(courseId);
    }

    /**
     * 查询课程学习子列表
     *
     * @param appCourseStudyItem 课程学习子
     * @return 课程学习子
     */
    @Override
    public List<AppCourseStudyItem> selectAppCourseStudyItemList(AppCourseStudyItem appCourseStudyItem) {
        return appCourseStudyItemMapper.selectAppCourseStudyItemList(appCourseStudyItem);
    }

    /**
     * 新增课程学习子
     *
     * @param appCourseStudyItem 课程学习子
     * @return 结果
     */
    @Override
    public int insertAppCourseStudyItem(AppCourseStudyItem appCourseStudyItem) {
        appCourseStudyItem.setCreateTime(DateUtils.getNowDate());
        int insert = appCourseStudyItemMapper.insertAppCourseStudyItem(appCourseStudyItem);
//        if (insert > 0 && "1".equals(appCourseStudyItem.getPercent())) {
//            CodeUtil.getCourseCode("ZS");
//            AppObjCertificateVo appObjCertificateVo = new AppObjCertificateVo();
//            int available = appObjCertificateMapper.getCertificateAvailable(appObjCertificateVo);
//        }
        return insert;
    }

    /**
     * 修改课程学习子
     *
     * @param appCourseStudyItem 课程学习子
     * @return 结果
     */
    @Override
    public int updateAppCourseStudyItem(AppCourseStudyItem appCourseStudyItem) {
        int update = appCourseStudyItemMapper.updateAppCourseStudyItem(appCourseStudyItem);
//        if (update > 0 && "1".equals(appCourseStudyItem.getPercent())) {
//            CodeUtil.getCourseCode("ZS");
//        }
        return update;
    }

    /**
     * 批量删除课程学习子
     *
     * @param courseIds 需要删除的课程学习子ID
     * @return 结果
     */
    @Override
    public int deleteAppCourseStudyItemByIds(Long[] courseIds) {
        return appCourseStudyItemMapper.deleteAppCourseStudyItemByIds(courseIds);
    }

    /**
     * 删除课程学习子信息
     *
     * @param courseId 课程学习子ID
     * @return 结果
     */
    @Override
    public int deleteAppCourseStudyItemById(Long courseId) {
        return appCourseStudyItemMapper.deleteAppCourseStudyItemById(courseId);
    }

    @Override
    public List<AppCourseChapterVo> selectUseCourseStudyByCourseId(Long courseId, Long userId) {
        return appCourseStudyItemMapper.selectUseCourseStudyByCourseId(courseId, userId);
    }

    @Override
    public AppCourseStudyItem selectUseCourseStudyByCourseIdAndChapterId(Long courseId, Long userId, Long chapterId) {
        return appCourseStudyItemMapper.selectUseCourseStudyByCourseIdAndChapterId(courseId, userId, chapterId);
    }

    @Override
    public int brushingCourse(Long courseId, Long userId, Long chapterId) {
        AppCourseStudyItem appCourseStudyItem = appCourseStudyItemMapper.selectUseCourseStudyByUserIdAndChapterId(userId, chapterId);
        if (appCourseStudyItem == null) {
            AppCourseStudy appCourseStudy = appCourseStudyMapper.selectUseCourseStudyByCourseId(courseId, userId);
            if (appCourseStudy == null) {
                appCourseStudy = new AppCourseStudy();
                appCourseStudy.setCourseId(courseId);
                appCourseStudy.setUserId(userId);
                appCourseStudyMapper.insertAppCourseStudy(appCourseStudy);
            }
            appCourseStudyItem = new AppCourseStudyItem();
            appCourseStudyItem.setCreateTime(DateUtils.getNowDate());
            appCourseStudyItem.setUserId(userId);
            appCourseStudyItem.setChapterId(chapterId);
            appCourseStudyItem.setCourseId(courseId);
            appCourseStudyItem.setPercent("0");
            appCourseStudyItem.setNum(0);
            appCourseStudyItem.setCsId(appCourseStudy.getId());
            appCourseStudyItemMapper.insertAppCourseStudyItem(appCourseStudyItem);
        }
        AppCourseStudyItem acsi = new AppCourseStudyItem();
        acsi.setId(appCourseStudyItem.getId());
        int i;
        if (appCourseStudyItem.getBrushingNum() == null) {
            i = 0;
        } else {
            i = appCourseStudyItem.getBrushingNum();
        }
        i++;
        acsi.setBrushingNum(i);
        return appCourseStudyItemMapper.updateAppCourseStudyItem(acsi);
    }

    @Override
    public int selectSumCourseStudyItem(Long userId) {
        return appCourseStudyItemMapper.selectSumCourseStudyItem(userId);
    }

    @Override
    public List<AppCourseStudyItemVo> selectScoreInfo(Long userId) {
        return appCourseStudyItemMapper.selectScoreInfo(userId);
    }
}
