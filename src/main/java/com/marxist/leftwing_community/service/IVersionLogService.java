package com.marxist.leftwing_community.service;

import com.marxist.leftwing_community.entity.VersionLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2023-01-12
 */
public interface IVersionLogService extends IService<VersionLog> {

    List<VersionLog> getVersionLogs();

    List<VersionLog> getVersionLogList();

    int insertVersionLog(VersionLog versionLog);
}
