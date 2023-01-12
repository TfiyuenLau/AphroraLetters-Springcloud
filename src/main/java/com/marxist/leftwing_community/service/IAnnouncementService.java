package com.marxist.leftwing_community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.marxist.leftwing_community.entity.Announcement;
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
public interface IAnnouncementService extends IService<Announcement> {

    List<Announcement> getAnnouncements();

    List<Announcement> getAnnouncementList();

    IPage<Announcement> getAnnouncementByPage(Long page);

    Announcement getAnnouncementById(Long id);

    int insertAnnouncement(Announcement announcement);

    int delAnnouncement(Long id);
}
