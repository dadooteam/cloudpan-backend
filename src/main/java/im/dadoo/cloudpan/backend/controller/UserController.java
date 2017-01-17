package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.dto.UserDto;
import im.dadoo.cloudpan.backend.so.UserSo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codekitten on 2017/1/17.
 */
@RestController
public class UserController {

  private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

  @Autowired
  private Gson gson;

  @Autowired
  private UserSo userSo;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> login(@RequestParam String phone, @RequestParam String password) {

    Result<UserDto> r = this.userSo.login(phone, password);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("phone", phone);
    logMap.put("password", password);
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public ResponseEntity<Result<?>> insert(@RequestParam String phone, @RequestParam String password) {

    Result<UserDto> r = this.userSo.insert(phone, password);

    Map<String, Object> logMap = new HashMap<>();
    logMap.put("phone", phone);
    logMap.put("password", password);
    SLOGGER.info(this.gson.toJson(logMap));

    return new ResponseEntity<>(r, HttpStatus.valueOf(r.getStatus()));
  }




}
