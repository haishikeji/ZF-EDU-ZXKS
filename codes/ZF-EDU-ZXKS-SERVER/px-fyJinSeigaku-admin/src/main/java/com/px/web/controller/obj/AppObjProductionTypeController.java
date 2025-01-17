package com.px.web.controller.obj;

import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.page.TableDataInfo;
import com.px.common.utils.poi.ExcelUtil;
import com.px.obj.domain.AppObjProductionType;
import com.px.obj.service.IAppObjProductionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作品分类关联Controller
 *
 * @author px
 * @date 2021-05-18
 */
@RestController
@RequestMapping("/obj/production/type")
public class AppObjProductionTypeController extends BaseController {
    @Autowired
    private IAppObjProductionTypeService appObjProductionTypeService;

    @GetMapping("/list")
    public TableDataInfo list(AppObjProductionType appObjProductionType) {
        startPage();
        List<AppObjProductionType> list = appObjProductionTypeService.selectAppObjProductionTypeList(appObjProductionType);
        return getDataTable(list);
    }

    @GetMapping("/export")
    public AjaxResult export(AppObjProductionType appObjProductionType) {
        List<AppObjProductionType> list = appObjProductionTypeService.selectAppObjProductionTypeList(appObjProductionType);
        ExcelUtil<AppObjProductionType> util = new ExcelUtil<AppObjProductionType>(AppObjProductionType.class);
        return util.exportExcel(list, "type");
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(appObjProductionTypeService.selectAppObjProductionTypeById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody AppObjProductionType appObjProductionType) {
        return toAjax(appObjProductionTypeService.insertAppObjProductionType(appObjProductionType));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody AppObjProductionType appObjProductionType) {
        return toAjax(appObjProductionTypeService.updateAppObjProductionType(appObjProductionType));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(appObjProductionTypeService.deleteAppObjProductionTypeByIds(ids));
    }
}
