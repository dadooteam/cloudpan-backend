package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2017/1/7.
 */
@RestController
public class FileController {

  private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

  @Autowired
  private Gson gson;

  @Autowired
  private FileSo fileSo;

  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> upload(@RequestParam(required = false) String path, @RequestParam MultipartFile file) {
    long userId = 1L;

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<FileDto> r = this.fileSo.upload(userId, path, file);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    logMap.put("name", file.getOriginalFilename());
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }

  @RequestMapping(value = "/mkdir", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> mkdir(@RequestParam(required = false) String path, @RequestParam String name) {
    long userId = 1L;
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    name = StringUtils.strip(name);
    Result<FileDto> r = this.fileSo.mkdir(userId, path, name);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    logMap.put("name", name);
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public ResponseEntity<Result<?>> delete(@RequestParam(required = false) String path) {
    long userId = 1L;

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<Object> r = this.fileSo.delete(userId, path);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public ResponseEntity<Result<?>> list(@RequestParam(value = "path", required = false) String path) {
    long userId = 1L;

    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<List<FileDto>> r = this.fileSo.list(userId, path);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("userId", userId);
    logMap.put("path", path);
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }
}