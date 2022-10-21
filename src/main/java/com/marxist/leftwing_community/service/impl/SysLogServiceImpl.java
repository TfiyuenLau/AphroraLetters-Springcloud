package com.marxist.leftwing_community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marxist.leftwing_community.entity.SysLog;
import com.marxist.leftwing_community.dao.SysLogMapper;
import com.marxist.leftwing_community.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    @Autowired
    private SysLogMapper logMapper;

    /**
     * 查询日志分页对象
     * @param page
     * @return
     */
    @Override
    public IPage<SysLog> getLogByPage(Long page) {
        Page<SysLog> logPage = new Page<>(page, 10);
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        logMapper.selectPage(logPage, queryWrapper);

        return logPage;
    }
}
