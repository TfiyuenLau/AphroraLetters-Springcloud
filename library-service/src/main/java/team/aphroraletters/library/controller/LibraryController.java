package team.aphroraletters.library.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.aphroraletters.library.pojo.entity.AuthorIndex;
import team.aphroraletters.library.pojo.entity.LibraryAuthor;
import team.aphroraletters.library.pojo.request.LibraryAuthorParams;
import team.aphroraletters.library.pojo.request.LiteratureIndexParams;
import team.aphroraletters.library.pojo.response.ResultVO;
import team.aphroraletters.library.service.IAuthorIndexService;
import team.aphroraletters.library.service.ILibraryAuthorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
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

    @ApiOperation("上传文章作者头像图片")
    @PostMapping("/admin/uploadAuthorImage")
    public ResultVO uploadAuthorImage(@RequestParam("image") MultipartFile imageFile) {
        File uploadFile;
        try {
            uploadFile = uploadFile(imageFile, "img");
        } catch (FileNotFoundException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(uploadFile.getName());
    }

    @ApiOperation("新增收录一位文库作者")
    @PostMapping("/admin/insertAuthor")
    public ResultVO insertAuthor(@RequestBody LibraryAuthorParams authorParams) {
        LibraryAuthor libraryAuthor = new LibraryAuthor();
        BeanUtils.copyProperties(authorParams, libraryAuthor);
        try {
            libraryAuthorService.insertLibraryAuthor(libraryAuthor);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("收录作者【" + authorParams.getCharacterName() + "】成功");
    }

    @ApiOperation("上传文章PDF文件")
    @PostMapping("/admin/uploadAuthorPdf")
    public ResultVO uploadAuthorPdf(@RequestParam("pdf") MultipartFile pdfFile) {
        File uploadFile;
        try {
            uploadFile = uploadFile(pdfFile, "pdf");
        } catch (FileNotFoundException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(uploadFile.getName());
    }

    @ApiOperation("收录一篇新增的文库文章")
    @PostMapping("/admin/insertLiterature")
    public ResultVO insertLiterature(@RequestBody LiteratureIndexParams literatureIndexParams) {
        AuthorIndex authorIndex = new AuthorIndex();
        BeanUtils.copyProperties(literatureIndexParams, authorIndex);
        try {
            authorIndexService.insertAuthorIndex(authorIndex);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("收录文章【" + literatureIndexParams.getTitle() + "】成功");
    }

    @ApiOperation("更新文库作者信息")
    @PutMapping("/admin/updateAuthor")
    public ResultVO updateAuthor(@RequestBody LibraryAuthorParams authorParams) {
        LibraryAuthor libraryAuthor = new LibraryAuthor();
        BeanUtils.copyProperties(authorParams, libraryAuthor);
        try {
            libraryAuthorService.updateLibraryAuthorById(libraryAuthor);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("作者【" + authorParams.getCharacterName() + "】基本信息修改成功");
    }

    @ApiOperation("修改文库文章数据")
    @PutMapping("/admin/updateLiterature")
    public ResultVO updateLiterature(@RequestBody LiteratureIndexParams literatureIndexParams) {
        AuthorIndex authorIndex = new AuthorIndex();
        BeanUtils.copyProperties(literatureIndexParams, authorIndex);
        try {
            authorIndexService.updateAuthorIndexById(authorIndex);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("文章【" + literatureIndexParams.getTitle() + "】信息修改成功");
    }

    @ApiOperation("删除文库中ID对应的指定作者")
    @DeleteMapping("/admin/deleteAuthor")
    public ResultVO deleteAuthor(@RequestParam("id") Long id) {
        try {
            libraryAuthorService.deleteLibraryAuthorById(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("文库作者删除成功");
    }

    @ApiOperation("删除文库中ID对应的指定文章")
    @DeleteMapping("/admin/deleteLiterature")
    public ResultVO deleteLiterature(@RequestParam("articleId") Long articleId) {
        try {
            authorIndexService.deleteAuthorIndex(articleId);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok("文库文章删除成功");
    }

    /**
     * 上传文件通用方法
     *
     * @param file         MultipartFile 文件对象
     * @param filePathName 静态资源文件目录——img、md、page、pdf
     * @return 创建的targetPicFile对象
     * @throws FileNotFoundException 抛出找不到文件异常
     */
    private File uploadFile(MultipartFile file, String filePathName) throws FileNotFoundException {
        // 获取上传文件的地址
        File newFile = new File(ResourceUtils.getURL("classpath:").getPath());
        // 项目路径绝对my_web_project\target\classes
        String absolutePath = newFile.getAbsolutePath();
        // 文件放入/static/#/目录下
        String pathPic = absolutePath + "/static/" + filePathName + "/";
        // 获取文件后缀
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        // 生片路径和唯一标识
        File targetPicFile = new File(pathPic, UUID.randomUUID().toString().replace("-", "") + fileSuffix);
        if (!targetPicFile.exists()) {
            targetPicFile.mkdirs();
        }

        // 保存文件至target
        try {
            file.transferTo(targetPicFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 复制target文件到/static/*/(仅Win开发时使用)
        try {
            Files.copy(new File(targetPicFile.getAbsolutePath()).toPath(),
                    new File(System.getProperty("user.dir") + "\\library-service\\src\\main\\resources\\static\\" + filePathName + "\\" + targetPicFile.getName()).toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return targetPicFile; // 返回创建的目标文件对象
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
