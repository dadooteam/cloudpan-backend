package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2017/1/7.
 */
@Controller
public class FileController {

  private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

  @Autowired
  private Environment env;

  @Autowired
  private Gson gson;

  @Autowired
  private FileSo fileSo;

  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> upload(@RequestParam(required = false) String path,
                                          @RequestParam MultipartFile file,
                                          @RequestAttribute long visitorId) {

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<FileDto> r = this.fileSo.upload(visitorId, path, file);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    logMap.put("name", file.getOriginalFilename());
    SLOGGER.info(this.gson.toJson(logMap));

    return ResponseEntity.status(r.getStatus()).body(r);
  }

  @RequestMapping(value = "/mkdir", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> mkdir(@RequestParam(required = false) String path,
                                         @RequestParam String name,
                                         @RequestAttribute long visitorId) {
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    name = StringUtils.strip(name);
    Result<FileDto> r = this.fileSo.mkdir(visitorId, path, name);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    logMap.put("name", name);
    SLOGGER.info(this.gson.toJson(logMap));

    return ResponseEntity.status(r.getStatus()).body(r);
  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public ResponseEntity<Result<?>> delete(@RequestParam(required = false) String path,
                                          @RequestAttribute long visitorId) {

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<Object> r = this.fileSo.delete(visitorId, path);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return ResponseEntity.status(r.getStatus()).body(r);
  }



  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public String download(@RequestParam(required = false) String path,
                         @RequestAttribute long visitorId,
                         HttpServletResponse response) {
    String resourceUrl =  "";
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    String fullPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), visitorId, path);
    File file = new File(fullPath);
    if (file.exists() && file.isFile()) {
      String filename = "default";
      try {
        filename = URLEncoder.encode(file.getName(), "UTF-8");
      } catch (Exception e) {

      }
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", filename));
      resourceUrl = String.format("/files/%d/%s", visitorId, path);
    }

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return String.format("forward:%s", resourceUrl);
  }

  @RequestMapping(value = "/preview", method = RequestMethod.GET)
  public ResponseEntity<Result<?>> preview(@RequestParam(required = false) String path,
                         @RequestAttribute long visitorId) {
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));

    Result<String> r = this.fileSo.preview(visitorId, path);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));
    return ResponseEntity.status(r.getStatus()).body(r);
  }

  @RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
  public void thumbnail(@RequestParam(required = false) String path,
                                           @RequestAttribute long visitorId,
                                             HttpServletResponse response) {
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));

    try {
      if (this.fileSo.thumbnail(visitorId, path, response.getOutputStream())) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "image/jpeg");
      }
    } catch (Exception e) {

    }

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public ResponseEntity<Result<?>> list(@RequestParam(value = "path", required = false) String path,
                                        @RequestAttribute long visitorId) {

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<List<FileDto>> r = this.fileSo.list(visitorId, path);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("visitorId", visitorId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return ResponseEntity.status(r.getStatus()).body(r);
  }
}
