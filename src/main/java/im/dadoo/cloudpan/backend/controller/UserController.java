package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.so.UserSo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public String login(@RequestParam String name, @RequestParam String password) {
    return "";
  }


}
