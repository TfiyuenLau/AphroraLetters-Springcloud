package team.aphroraletters.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import team.aphroraletters.article.pojo.entity.Announcement;

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

    int updateAnnouncementById(Announcement announcement);

    int delAnnouncement(Long id);
}
