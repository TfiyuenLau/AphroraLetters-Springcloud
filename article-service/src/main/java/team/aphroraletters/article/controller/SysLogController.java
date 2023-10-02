package team.aphroraletters.article.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;
import team.aphroraletters.article.pojo.entity.SysLog;
import team.aphroraletters.article.pojo.response.ResultVO;
import team.aphroraletters.article.service.ISysLogService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @ApiOperation("获取服务器基本状态信息")
    @GetMapping("/getServerBasicInfo")
    public ResultVO getServerBasicInfo() {
        // 创建 ServerBasicInfo 对象
        ServerBasicInfo serverBasicInfo = new ServerBasicInfo();

        // 获取服务器主机基本信息
        Runtime runtime = Runtime.getRuntime();
        serverBasicInfo.setProcessors(runtime.availableProcessors());
        OperatingSystem os = OshiUtil.getOs();
        serverBasicInfo.setOs(os.getFamily());
        serverBasicInfo.setTime(LocalDateTimeUtil.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // 获取磁盘数据
        File file = new File("/");
        serverBasicInfo.setDiskTotal((double) file.getTotalSpace() / 1024 / 1024 / 1024);
        serverBasicInfo.setDiskFreeSpace((double) file.getFreeSpace() / 1024 / 1024 / 1024);

        return ResultVO.ok(serverBasicInfo);
    }

    @ApiOperation("获取服务器负载信息")
    @GetMapping("/getServerLoadInfo")
    public ResultVO getServerInfo() {
        // 创建 ServerBasicInfo 对象并返回
        ServerLoadInfo serverLoadInfo = new ServerLoadInfo();

        // 使用OSHI获取CPU与内存信息
        GlobalMemory memory = OshiUtil.getMemory();
        serverLoadInfo.setTotalMemory((double) memory.getTotal() / 1024 / 1024 / 1000);
        serverLoadInfo.setFreeMemory((double) memory.getAvailable() / 1024 / 1024 / 1000);
        serverLoadInfo.setCpuUsage(OshiUtil.getCpuInfo().getUsed());
        serverLoadInfo.setTime(LocalDateTimeUtil.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        return ResultVO.ok(serverLoadInfo);
    }

    /**
     * 服务器基本信息类
     */
    @Data
    public static class ServerBasicInfo {

        /**
         * 操作系统
         */
        private String os;

        /**
         * 可用处理器数量
         */
        private int processors;

        /**
         * 磁盘总量
         */
        private double diskTotal;

        /**
         * 磁盘剩余空间
         */
        private double diskFreeSpace;

        /**
         * 当前时间
         */
        private String time;

    }

    /**
     * 服务器负载信息类
     */
    @Data
    public static class ServerLoadInfo {

        /**
         * 总内存大小（单位：字节）
         */
        private double totalMemory;

        /**
         * 空闲内存大小（单位：字节）
         */
        private double freeMemory;

        /**
         * CPU使用率
         */
        private double cpuUsage;

        /**
         * 当前时间
         */
        private String time;

    }

}
