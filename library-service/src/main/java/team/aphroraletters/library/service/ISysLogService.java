package team.aphroraletters.library.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.library.entity.SysLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @MatikaneSpartakusbund
 * @since 2022-09-21
 */
public interface ISysLogService extends IService<SysLog> {

    IPage<SysLog> getLogByPage(Long page);

    int delLog(Long id);
}
