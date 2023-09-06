package team.aphroraletters.article.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.aphroraletters.article.entity.SysLog;
import team.aphroraletters.article.entity.response.ResultVO;
import team.aphroraletters.article.service.ISysLogService;

@RestController
@RequestMapping("/admin")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @ApiOperation("通过页码获取日志列表")
    @GetMapping("/getSysLogByPage/{page}")
    public ResultVO getSysLogByPage(@PathVariable("page") Long page) {
        IPage<SysLog> sysLogIPage = sysLogService.getLogByPage(page);
        if (sysLogIPage.getRecords().isEmpty()) {
            return ResultVO.errorMsg("无对应的日志信息");
        }

        return ResultVO.ok(sysLogIPage);
    }

    @ApiOperation("获取服务器负载信息")
    @GetMapping("/getServerInfo")
    public ResultVO getServerInfo() {
        // 创建 ServerInfo 对象并返回
        ServerInfo serverInfo = new ServerInfo();

        // 获取服务器基本参数
        Runtime runtime = Runtime.getRuntime();
        serverInfo.setPid(new ApplicationPid().toString());
        serverInfo.setTotalMemory(runtime.totalMemory());
        serverInfo.setFreeMemory(runtime.freeMemory());
        serverInfo.setProcessors(runtime.availableProcessors());

        return ResultVO.ok(serverInfo);
    }

    /**
     * 主机服务信息实体类
     */
    @Data
    public static class ServerInfo {
        /**
         * 应用程序进程 ID
         */
        private String pid;

        /**
         * 总内存大小（单位：字节）
         */
        private long totalMemory;

        /**
         * 空闲内存大小（单位：字节）
         */
        private long freeMemory;

        /**
         * 可用处理器数量
         */
        private int processors;

        /**
         * CPU使用率
         */
        private double cpuUsage;

        /**
         * 磁盘读取情况
         */
        private double diskReadBytes;

        /**
         * 磁盘写入情况
         */
        private double diskWriteBytes;

    }
}
