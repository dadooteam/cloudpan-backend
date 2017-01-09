package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.common.dto.FileDto;
import im.dadoo.cloudpan.backend.common.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2017/1/7.
 */
@Controller
public class FileController {

  private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

  @Resource
  private Gson gson;

  @Resource
  private FileSo fileSo;

  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  @ResponseBody
  public Result<?> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
    long userId = 1L;
    String path = StringUtils.strip(StringUtils.replaceChars(request.getParameter("path"), (char) 160, ' '));
    MultipartFile file = request.getFile("file");
    Result<FileDto> r = this.fileSo.upload(userId, path, file);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    logMap.put("name", file.getName());
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }

  @RequestMapping(value = "/mkdir", method = RequestMethod.POST)
  @ResponseBody
  public Result<?> mkdir(HttpServletRequest request, HttpServletResponse response) {
    long userId = 1L;
    String path = StringUtils.strip(StringUtils.replaceChars(request.getParameter("path"), (char) 160, ' '));
    String name = StringUtils.strip(request.getParameter("name"));
    Result<FileDto> r = this.fileSo.mkdir(userId, path, name);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    logMap.put("name", name);
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  @ResponseBody
  public Result<?> delete(HttpServletRequest request, HttpServletResponse response) {
    long userId = 1L;
    String path = StringUtils.strip(StringUtils.replaceChars(request.getParameter("path"), (char) 160, ' '));
    Result<Object> r = this.fileSo.delete(userId, path);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }


  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ResponseBody
  public Result<?> list(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> uv = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    long userId = 1L;

    String path = StringUtils.strip(StringUtils.replaceChars(request.getParameter("path"), (char) 160, ' '));
    Result<List<FileDto>> r = this.fileSo.list(userId, path);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }
}
