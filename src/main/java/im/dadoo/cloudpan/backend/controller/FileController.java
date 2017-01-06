package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2016/12/27.
 */
@Controller
public class FileController {

    private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

    @Resource
    private Gson gson;

    @Resource
    private FileSo fileSo;

    @RequestMapping(value = "/traverse", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> traverse(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getParameter("path");
        Result<List<FileDto>> r = this.fileSo.traverse(path);

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("path", path);
        SLOGGER.info(this.gson.toJson(logMap));
        return r;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> download(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getParameter("path");
        Result<File> r = this.fileSo.download(path);
        File file = r.getData();
        if (file != null) {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                String mimeType = URLConnection.guessContentTypeFromStream(is);
                if (StringUtils.isBlank(mimeType)){
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("inline; filename=\"" + file.getName() +"\""));
                response.setContentLength((int)file.length());
                FileCopyUtils.copy(is, response.getOutputStream());
            } catch (Exception e) {}
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("path", path);
        SLOGGER.info(this.gson.toJson(logMap));
        return r;
    }

  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  @ResponseBody
  public Result<?> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
    String path = request.getParameter("path");
    MultipartFile file = request.getFile("file");
    Result<Object> r = this.fileSo.upload(path, file);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("path", path);
    logMap.put("filename", file.getName());
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }
}
