package com.px.web.controller.obj;

import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.page.TableDataInfo;
import com.px.common.utils.poi.ExcelUtil;
import com.px.obj.domain.AppObjTopicIssue;
import com.px.obj.service.IAppObjTopicIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目和题库关联Controller
 *
 * @author px
 * @date 2021-05-16
 */
@RestController
@RequestMapping("/obj/topic/issue")
public class AppObjTopicIssueController extends BaseController {
    @Autowired
    private IAppObjTopicIssueService appObjTopicIssueService;

    @GetMapping("/list")
    public TableDataInfo list(AppObjTopicIssue appObjTopicIssue) {
        startPage();
        List<AppObjTopicIssue> list = appObjTopicIssueService.selectAppObjTopicIssueList(appObjTopicIssue);
        return getDataTable(list);
    }

    @GetMapping("/export")
    public AjaxResult export(AppObjTopicIssue appObjTopicIssue) {
        List<AppObjTopicIssue> list = appObjTopicIssueService.selectAppObjTopicIssueList(appObjTopicIssue);
        ExcelUtil<AppObjTopicIssue> util = new ExcelUtil<AppObjTopicIssue>(AppObjTopicIssue.class);
        return util.exportExcel(list, "issue");
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(appObjTopicIssueService.selectAppObjTopicIssueById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody AppObjTopicIssue appObjTopicIssue) {
        return toAjax(appObjTopicIssueService.insertAppObjTopicIssue(appObjTopicIssue));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody AppObjTopicIssue appObjTopicIssue) {
        return toAjax(appObjTopicIssueService.updateAppObjTopicIssue(appObjTopicIssue));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(appObjTopicIssueService.deleteAppObjTopicIssueByIds(ids));
    }
}
