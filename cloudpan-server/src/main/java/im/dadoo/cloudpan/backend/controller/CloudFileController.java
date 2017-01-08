package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.common.dto.Result;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import im.dadoo.cloudpan.backend.so.CloudFileSo;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
public class CloudFileController {

  private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

  @Resource
  private Gson gson;

  @Resource
  private CloudFileSo cloudFileSo;

  @RequestMapping(value = "/cloud-file/file", method = RequestMethod.POST)
  @ResponseBody
  public Result<?> insert(MultipartHttpServletRequest request, HttpServletResponse response) {
    long userId = 1L;
    long supId = NumberUtils.toLong(request.getParameter("supId"));
    MultipartFile file = request.getFile("file");
    Result<CloudFilePo> r = this.cloudFileSo.insert(userId, supId, file);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("supId", supId);
    logMap.put("fileName", file.getName());
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }

  @RequestMapping(value = "/cloud-file/dir", method = RequestMethod.POST)
  @ResponseBody
  public Result<?> insert(HttpServletRequest request, HttpServletResponse response) {
    long userId = 1L;
    long supId = NumberUtils.toLong(request.getParameter("supId"));
    String name = StringUtils.strip(request.getParameter("name"));
    Result<CloudFilePo> r = this.cloudFileSo.insert(userId, supId, name);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("supId", supId);
    logMap.put("dirName", name);
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }

  @RequestMapping(value = "/cloud-file/{id}/subs", method = RequestMethod.GET)
  @ResponseBody
  public Result<?> pageBySupId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> uv = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    long userId = 1L;
    long supId = NumberUtils.toLong(uv.get("id"));

    long pageCount = NumberUtils.toLong(request.getParameter("pageCount"), 1L);
    long pageSize = NumberUtils.toLong(request.getParameter("pageSize"), 20L);
    Result<List<CloudFilePo>> r = this.cloudFileSo.pageBySupId(userId, supId, pageCount, pageSize);

    response.setStatus(r.getStatus());

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("supId", supId);
    logMap.put("pageCount", pageCount);
    logMap.put("pageSize", pageSize);
    SLOGGER.info(this.gson.toJson(logMap));

    return r;
  }
}
