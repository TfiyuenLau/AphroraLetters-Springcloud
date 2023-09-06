package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.pojo.entity.VersionLog;

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
