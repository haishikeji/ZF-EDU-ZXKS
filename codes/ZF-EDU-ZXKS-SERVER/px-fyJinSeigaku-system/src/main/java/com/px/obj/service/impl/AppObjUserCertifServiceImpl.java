package com.px.obj.service.impl;

import com.px.common.utils.DateUtils;
import com.px.common.utils.file.FileSaveUtils;
import com.px.obj.domain.AppObjCertificate;
import com.px.obj.domain.AppObjUserCertif;
import com.px.obj.mapper.AppObjCertificateMapper;
import com.px.obj.mapper.AppObjUserCertifMapper;
import com.px.obj.service.IAppObjUserCertifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户关联证书Service业务层处理
 *
 * @author px
 * @date 2021-05-19
 */
@Service
public class AppObjUserCertifServiceImpl implements IAppObjUserCertifService {
    @Autowired
    private AppObjUserCertifMapper appObjUserCertifMapper;

    @Autowired
    private AppObjCertificateMapper appObjCertificateMapper;

    /**
     * 查询用户关联证书
     *
     * @param id 用户关联证书ID
     * @return 用户关联证书
     */
    @Override
    public AppObjUserCertif selectAppObjUserCertifById(Long id) {
        return appObjUserCertifMapper.selectAppObjUserCertifById(id);
    }

    /**
     * 查询用户关联证书
     *
     * @param userId 用户ID
     * @param cerId  证书ID
     * @return 用户关联证书
     */
    public AppObjUserCertif selectAppObjUserCertifByUserIdAndCerId(Long userId, Long cerId) {
        return appObjUserCertifMapper.selectAppObjUserCertifByUserIdAndCerId(userId, cerId);
    }

    /**
     * 查询用户关联证书列表
     *
     * @param appObjUserCertif 用户关联证书
     * @return 用户关联证书
     */
    @Override
    public List<AppObjUserCertif> selectAppObjUserCertifList(AppObjUserCertif appObjUserCertif) {
        return appObjUserCertifMapper.selectAppObjUserCertifList(appObjUserCertif);
    }

    /**
     * 获取证书编号
     *
     * @return
     */
    @Override
    public AppObjUserCertif getCode(AppObjUserCertif appObjUserCertif) {
        appObjUserCertif.setCreateTime(DateUtils.getNowDate());
        AppObjCertificate appObjCertificate = appObjCertificateMapper.selectAppObjCertificateById(appObjUserCertif.getCerefiId());
        AppObjUserCertif pojo = new AppObjUserCertif();
        pojo.setCerefiId(appObjUserCertif.getCerefiId());
        pojo.setCertifNo(appObjCertificate.getCode());
        List<AppObjUserCertif> list = appObjUserCertifMapper.selectAppObjUserCertifList(pojo);
        String code = appObjCertificate.getCode();
        if (list != null && list.size() > 0) {
            code = code + String.format("%06d", (list.size() + 1));
        } else {
            code = code + "000001";
        }
        appObjUserCertif.setCertifNo(code);
        appObjUserCertifMapper.insertAppObjUserCertif(appObjUserCertif);
        return appObjUserCertif;
    }

    /**
     * 新增用户关联证书
     *
     * @param appObjUserCertif 用户关联证书
     * @return 结果
     */
    @Override
    public AppObjUserCertif insertAppObjUserCertif(AppObjUserCertif appObjUserCertif) {
        appObjUserCertif.setCreateTime(DateUtils.getNowDate());
        if (appObjUserCertif.getImg() != null && !appObjUserCertif.getImg().isEmpty()) {
            if (appObjUserCertif.getImg().contains("data:image/png;base64,")) {
                appObjUserCertif.setImg(FileSaveUtils.saveFile(appObjUserCertif.getImg()));
            }
        }
        int i = appObjUserCertifMapper.insertAppObjUserCertif(appObjUserCertif);
        return i > 0 ? appObjUserCertif : null;
    }

    /**
     * 修改用户关联证书
     *
     * @param appObjUserCertif 用户关联证书
     * @return 结果
     */
    @Override
    public AppObjUserCertif updateAppObjUserCertif(AppObjUserCertif appObjUserCertif) {
        appObjUserCertif.setUpdateTime(DateUtils.getNowDate());
        if (appObjUserCertif.getImg() != null && !appObjUserCertif.getImg().isEmpty()) {
            if (appObjUserCertif.getImg().contains("data:image/png;base64,")) {
                appObjUserCertif.setImg(FileSaveUtils.saveFile(appObjUserCertif.getImg()));
            }
        }
        appObjUserCertifMapper.updateAppObjUserCertif(appObjUserCertif);
        return appObjUserCertif;
    }

    /**
     * 批量删除用户关联证书
     *
     * @param ids 需要删除的用户关联证书ID
     * @return 结果
     */
    @Override
    public int deleteAppObjUserCertifByIds(Long[] ids) {
        return appObjUserCertifMapper.deleteAppObjUserCertifByIds(ids);
    }

    /**
     * 删除用户关联证书信息
     *
     * @param id 用户关联证书ID
     * @return 结果
     */
    @Override
    public int deleteAppObjUserCertifById(Long id) {
        return appObjUserCertifMapper.deleteAppObjUserCertifById(id);
    }
}
