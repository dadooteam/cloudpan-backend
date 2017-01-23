package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
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

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
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

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
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

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }

  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public ResponseEntity<?> download(@RequestParam(required = false) String path,
                                            @RequestAttribute long visitorId) {
    path = StringUtils.strip(StringUtils.replaceChars(path, (char) 160, ' '));
    Result<File> temp = this.fileSo.download(visitorId, path);
    File file = temp.getData();
    if (file != null) {
      InputStream is = null;
      try {
        is = new BufferedInputStream(FileUtils.openInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        if (StringUtils.isBlank(mimeType)){
          mimeType = "application/octet-stream";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        ResponseEntity<InputStreamResource> r = ResponseEntity.ok().headers(headers)
            .contentLength(file.length()).contentType(MediaType.parseMediaType(mimeType))
            .body(new InputStreamResource(is));
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("visitorId", visitorId);
        logMap.put("path", path);
        SLOGGER.info(this.gson.toJson(logMap));
        return r;
      } catch (Exception e) {
        e.printStackTrace();
        temp.setData(null);
        ResponseEntity<Result<File>> r = ResponseEntity.status(temp.getStatus()).body(temp);
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("visitorId", visitorId);
        logMap.put("path", path);
        SLOGGER.info(this.gson.toJson(logMap));
        return r;
      } finally {
        //IOUtils.closeQuietly(is);
      }
    }
    return null;
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

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }
}
