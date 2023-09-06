package team.aphroraletters.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.aphroraletters.article.dao.AnnouncementMapper;
import team.aphroraletters.article.pojo.entity.Announcement;
import team.aphroraletters.article.service.IAnnouncementService;

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
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 获取首页部分公告栏最新消息
     *
     * @return
     */
    @Override
    public List<Announcement> getAnnouncements() {
        Page<Announcement> announcementPage = new Page<>(1, 8);
        announcementMapper.selectPage(announcementPage, new QueryWrapper<Announcement>().orderByDesc("id"));

        return announcementPage.getRecords();
    }

    /**
     * 获取公告栏所有通知消息
     *
     * @return
     */
    @Override
    public List<Announcement> getAnnouncementList() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        return announcementMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询公告栏通知消息
     *
     * @param page
     * @return
     */
    @Override
    public IPage<Announcement> getAnnouncementByPage(Long page) {
        IPage<Announcement> announcementPage = new Page<>(page, 10);
        announcementMapper.selectPage(announcementPage, null);

        return announcementPage;
    }

    /**
     * 按id获取公告对象
     *
     * @param id
     * @return
     */
    @Override
    public Announcement getAnnouncementById(Long id) {

        return announcementMapper.selectById(id);
    }

    /**
     * 添加公告栏消息
     *
     * @param announcement
     * @return
     */
    @Override
    public int insertAnnouncement(Announcement announcement) {
        //使用工具类将md转为html格式
//        MarkdownEntity markdownEntity = MarkDown2HtmlWrapper.ofContent(announcement.getContent());
//        announcement.setContent(markdownEntity.getHtml());

        return announcementMapper.insert(announcement);
    }

    @Override
    public int updateAnnouncementById(Announcement announcement) {
        return announcementMapper.updateById(announcement);
    }

    /**
     * 逻辑删除一个公告内容
     *
     * @param id
     * @return
     */
    @Override
    public int delAnnouncement(Long id) {

        return announcementMapper.deleteById(id);
    }

}
