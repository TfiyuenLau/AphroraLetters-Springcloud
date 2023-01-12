package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.entity.VersionLog;
import com.marxist.leftwing_community.dao.VersionLogMapper;
import com.marxist.leftwing_community.service.IVersionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2023-01-12
 */
@Service
public class VersionLogServiceImpl extends ServiceImpl<VersionLogMapper, VersionLog> implements IVersionLogService {

    @Autowired
    private VersionLogMapper versionLogMapper;

    /**
     * 获取首页最新的版本日志
     *
     * @return
     */
    @Override
    public List<VersionLog> getVersionLogs() {
        IPage<VersionLog> versionLogPage = new Page<>(1, 5);
        versionLogMapper.selectPage(versionLogPage, new QueryWrapper<VersionLog>().orderByDesc("id"));

        return versionLogPage.getRecords();
    }

    /**
     * 获取所有版本日志
     *
     * @return
     */
    @Override
    public List<VersionLog> getVersionLogList() {

        return versionLogMapper.selectList(new QueryWrapper<VersionLog>().orderByDesc("id"));
    }

    /**
     * 增加新的版本日志
     *
     * @param versionLog
     * @return
     */
    @Override
    public int insertVersionLog(VersionLog versionLog) {

        return versionLogMapper.insert(versionLog);
    }

}
