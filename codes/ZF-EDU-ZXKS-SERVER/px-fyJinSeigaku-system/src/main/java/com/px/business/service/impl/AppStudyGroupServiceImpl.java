package com.px.business.service.impl;

import com.px.business.domain.AppStudyGroup;
import com.px.business.mapper.AppStudyGroupMapper;
import com.px.business.service.IAppStudyGroupService;
import com.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学习班班群Service业务层处理
 *
 * @author px
 * @date 2021-04-29
 */
@Service
public class AppStudyGroupServiceImpl implements IAppStudyGroupService {
    @Autowired
    private AppStudyGroupMapper appStudyGroupMapper;

    /**
     * 查询学习班班群
     *
     * @param id 学习班班群ID
     * @return 学习班班群
     */
    @Override
    public AppStudyGroup selectAppStudyGroupById(Long id) {
        return appStudyGroupMapper.selectAppStudyGroupById(id);
    }

    /**
     * 查询学习班班群列表
     *
     * @param appStudyGroup 学习班班群
     * @return 学习班班群
     */
    @Override
    public List<AppStudyGroup> selectAppStudyGroupList(AppStudyGroup appStudyGroup) {
        return appStudyGroupMapper.selectAppStudyGroupList(appStudyGroup);
    }

    /**
     * 新增学习班班群
     *
     * @param appStudyGroup 学习班班群
     * @return 结果
     */
    @Override
    public int insertAppStudyGroup(AppStudyGroup appStudyGroup) {
        appStudyGroup.setCreateTime(DateUtils.getNowDate());
        return appStudyGroupMapper.insertAppStudyGroup(appStudyGroup);
    }

    /**
     * 修改学习班班群
     *
     * @param appStudyGroup 学习班班群
     * @return 结果
     */
    @Override
    public int updateAppStudyGroup(AppStudyGroup appStudyGroup) {
        return appStudyGroupMapper.updateAppStudyGroup(appStudyGroup);
    }

    /**
     * 批量删除学习班班群
     *
     * @param ids 需要删除的学习班班群ID
     * @return 结果
     */
    @Override
    public int deleteAppStudyGroupByIds(Long[] ids) {
        return appStudyGroupMapper.deleteAppStudyGroupByIds(ids);
    }

    /**
     * 删除学习班班群信息
     *
     * @param id 学习班班群ID
     * @return 结果
     */
    @Override
    public int deleteAppStudyGroupById(Long id) {
        return appStudyGroupMapper.deleteAppStudyGroupById(id);
    }


    @Override
    public AppStudyGroup selectAppStudyGroupByStudyId(Long studyId) {
        return appStudyGroupMapper.selectAppStudyGroupByStudyId(studyId);
    }
}
