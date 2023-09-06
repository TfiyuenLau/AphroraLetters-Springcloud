package team.aphroraletters.library.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team.aphroraletters.library.entity.AuthorIndex;
import team.aphroraletters.library.entity.LibraryAuthor;
import team.aphroraletters.library.entity.response.ResultVO;
import team.aphroraletters.library.service.IAuthorIndexService;
import team.aphroraletters.library.service.ILibraryAuthorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import team.aphroraletters.library.util.OperateLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
public class LibraryController {

    @Autowired
    private ILibraryAuthorService libraryAuthorService;

    @Autowired
    private IAuthorIndexService authorIndexService;

    @ApiOperation("获取id对应作者信息")
    @GetMapping("/getAuthorByAuthorId/{authorId}")
    public ResultVO getAuthorByAuthorId(@PathVariable("authorId") Long authorId) {
        LibraryAuthor author = libraryAuthorService.getAuthorById(authorId);
        if (author == null) {
            return ResultVO.errorMsg("None");
        }

        return ResultVO.ok(author);
    }

    @ApiOperation("获取文库所有作者索引信息")
    @GetMapping("/getAuthorList")
    public ResultVO getAuthorList() {
        List<LibraryAuthor> libraryAuthorList = libraryAuthorService.getAllAuthorIndex();
        if (libraryAuthorList.isEmpty()) {
            return ResultVO.errorMsg("None");
        }

        return ResultVO.ok(libraryAuthorList);
    }

    @ApiOperation("获取作者id对应的文章")
    @GetMapping("/getAuthorIndexListByAuthorId/{authorId}")
    public ResultVO getAuthorIndexListByAuthorId(@PathVariable("authorId") Long authorId) {
        List<AuthorIndex> authorIndexList = authorIndexService.getListByAuthorId(authorId);
        if (authorIndexList.isEmpty()) {
            return ResultVO.errorMap("None");
        }

        return ResultVO.ok(authorIndexList);
    }

    //访问文库全索引页面
    @OperateLog(operateDesc = "访问文库")
    @RequestMapping("/library")
    public String toLibrary(Model model) {
        //查询文库作者
        List<LibraryAuthor> libraryAuthors = libraryAuthorService.getAllLibraryAuthor();
        model.addAttribute("libraryAuthors", libraryAuthors);

        return "library";
    }

    //访问通用文库人物索引页面
    @OperateLog(operateDesc = "访问马尔库塞文库索引")
    @RequestMapping("/author_index")
    public String toAuthorIndex(Long author_id, Model model) {
        //按id查询封装了AuthorIndex的LibraryAuthor对象
        LibraryAuthor author = libraryAuthorService.getAuthorById(author_id);
        model.addAttribute("author", author);

        return "author_index";
    }

    //访问literature具体文献页
    @OperateLog(operateDesc = "访问文库PDF文章")
    @RequestMapping("/literature")
    public String literature(String url, Model model) {
//        String url = "http://127.0.0.1:8080/pdf/DreamLogic实验指导3.1.pdf";
//        InetAddress address = InetAddress.getLocalHost();
        model.addAttribute("url", url);

        return "literature";
    }

    /**
     * pdf.js实现pdf分页加载(pdf文件流懒加载)
     *
     * @param file
     * @param response
     * @param request
     */
    public static void loadPDFByPage(File file, HttpServletResponse response, HttpServletRequest request) {
        BufferedInputStream bis = null;
        OutputStream os = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            os = response.getOutputStream();
            bos = new BufferedOutputStream(os);
            // 下载的字节范围
            int startByte, endByte, totalByte;
            if (request != null && request.getHeader("range") != null) {
                // 断点续传
                String[] range = request.getHeader("range").replaceAll("[^0-9\\-]", "").split("-");
                // 文件总大小
                totalByte = is.available();
                // 下载起始位置
                startByte = Integer.parseInt(range[0]);
                // 下载结束位置
                if (range.length > 1) {
                    endByte = Integer.parseInt(range[1]);
                } else {
                    endByte = totalByte - 1;
                }
                // 返回http状态
                response.setStatus(206);
            } else {
                // 正常下载
                // 文件总大小
                totalByte = is.available();
                // 下载起始位置
                startByte = 0;
                // 下载结束位置
                endByte = totalByte - 1;
                // 返回http状态
                response.setHeader("Accept-Ranges", "bytes");
                response.setStatus(200);
            }
            // 需要下载字节数
            int length = endByte - startByte + 1;
            // 响应头
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + totalByte);
//            response.setContentType("application/pdf");
            response.setContentType("application/octet-stream");
            response.setContentLength(length);
            // 响应内容
            bis.skip(startByte);
            int len = 0;
            byte[] buff = new byte[1024 * 64];
            while ((len = bis.read(buff, 0, buff.length)) != -1) {
                if (length <= len) {
                    bos.write(buff, 0, length);
                    break;
                } else {
                    length -= len;
                    bos.write(buff, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //也可使用try catch关闭IO流
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);
        }
    }


}
