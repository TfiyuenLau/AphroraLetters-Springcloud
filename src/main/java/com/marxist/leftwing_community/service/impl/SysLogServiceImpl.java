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
        //分页查询日志信息
        Page<SysLog> logPage = new Page<>(page, 20);
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();

        //当总页数>200时限制最大查询id大于等于(总页数-200)的日志
        Long logCount = logMapper.selectCount(null);
        if (logCount > 200) {
            queryWrapper.orderByDesc("id").gt("id", logCount - 200);
        } else {
            queryWrapper.orderByDesc("id");
        }
        logMapper.selectPage(logPage, queryWrapper);

        return logPage;
    }

    /**
     * 按id删除日志
     * @param id
     * @return
     */
    @Override
    public int delLog(Long id) {

        return logMapper.deleteById(id);
    }
}
