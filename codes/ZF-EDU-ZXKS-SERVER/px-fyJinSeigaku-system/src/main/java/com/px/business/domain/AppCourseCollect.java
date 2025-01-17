package com.px.business.domain;

import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;
import com.px.common.core.domain.entity.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程收藏对象 app_course_collect
 *
 * @author px
 * @date 2021-04-29
 */
public class AppCourseCollect extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private Long id;

    private Long courseId;

    /**
     * 学习者（用户）id
     */
    private Long userId;

    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /**
     * 课程
     */
    private AppCourse appCourse;

    /**
     * 教师
     */
    private AppPointTeacher appPointTeacher;

    /**
     * 用戶
     */
    private SysUser sysUser;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public AppCourse getAppCourse() {
        return appCourse;
    }

    public void setAppCourse(AppCourse appCourse) {
        this.appCourse = appCourse;
    }

    public AppPointTeacher getAppPointTeacher() {
        return appPointTeacher;
    }

    public void setAppPointTeacher(AppPointTeacher appPointTeacher) {
        this.appPointTeacher = appPointTeacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("createTime", getCreateTime())
                .append("orderNum", getOrderNum())
                .append("remark", getRemark())
                .toString();
    }
}
