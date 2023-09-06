package team.aphroraletters.article.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.aphroraletters.article.entity.Announcement;
import team.aphroraletters.article.entity.VersionLog;
import team.aphroraletters.article.entity.response.ResultVO;
import team.aphroraletters.article.service.IAnnouncementService;
import team.aphroraletters.article.service.IVersionLogService;

import java.util.List;

@RestController
public class AnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    @Autowired
    private IVersionLogService versionLogService;

    @ApiOperation("获取首页部分公告栏最新消息")
    @GetMapping("/getAnnouncements")
    public ResultVO getAnnouncements() {
        List<Announcement> announcements = announcementService.getAnnouncements();
        if (announcements.isEmpty()) {
            return ResultVO.errorMsg("没有公告栏数据");
        }

        return ResultVO.ok(announcements);
    }

    @ApiOperation("获取所有公告栏数据")
    @GetMapping("/getAnnouncementList")
    public ResultVO getAnnouncementList() {
        List<Announcement> announcementList = announcementService.getAnnouncementList();
        if (announcementList.isEmpty()) {
            return ResultVO.errorMsg("没有公告栏数据");
        }

        return ResultVO.ok(announcementList);
    }

    @ApiOperation("通过id获取公告封装对象")
    @GetMapping("/getAnnouncementById/{id}")
    public ResultVO getAnnouncementById(@PathVariable("id") long announcementId) {
        Announcement announcement = announcementService.getAnnouncementById(announcementId);
        if (announcement == null) {
            return ResultVO.errorMsg("未找到id=" + announcementId + "对应的公告对象");
        }

        return ResultVO.ok(announcement);
    }

    @ApiOperation("管理员通过page获取公告数据列表")
    @GetMapping("/admin/getAnnouncementByPage/{page}")
    public ResultVO getAnnouncementsByPage(@PathVariable("page") long page) {
        IPage<Announcement> announcementIPage = announcementService.getAnnouncementByPage(page);
        if (announcementIPage == null) {
            return ResultVO.errorMsg("未找到page=" + page + "对应的公告列表");
        }

        return ResultVO.ok(announcementIPage);
    }

    @ApiOperation("管理员新增一则公告")
    @PostMapping("/admin/insertAnnouncement")
    public ResultVO insertAnnouncement(@RequestBody Announcement announcement) {
        try {
            announcementService.insertAnnouncement(announcement);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("新增成功");
    }

    @ApiOperation("管理员更新公告命令")
    @PutMapping("/admin/updateAnnouncement")
    public ResultVO updateAnnouncement(@RequestBody Announcement announcement) {
        try {
            announcementService.updateAnnouncementById(announcement);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("更新成功");
    }

    @ApiOperation("管理员发送删除公告请求")
    @DeleteMapping("/admin/deleteAnnouncementById/{id}")
    public ResultVO deleteAnnouncementById(@PathVariable("id") Long id) {
        try {
            announcementService.delAnnouncement(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("删除成功");
    }

    @ApiOperation("获取首页版本最新日志消息")
    @GetMapping("/getVersionLogs")
    public ResultVO getVersionLogs() {
        List<VersionLog> versionLogs = versionLogService.getVersionLogs();
        if (versionLogs.isEmpty()) {
            return ResultVO.errorMsg("没有版本日志消息");
        }

        return ResultVO.ok(versionLogs);
    }

    @ApiOperation("获取所有日志消息")
    @GetMapping("/getVersionLogList")
    public ResultVO getVersionLogList() {
        List<VersionLog> versionLogList = versionLogService.getVersionLogList();
        if (versionLogList.isEmpty()) {
            return ResultVO.errorMsg("没有版本日志消息");
        }

        return ResultVO.ok(versionLogList);
    }

}
