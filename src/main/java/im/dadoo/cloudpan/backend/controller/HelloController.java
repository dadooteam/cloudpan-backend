package im.dadoo.cloudpan.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by codekitten on 2017/1/7.
 */
@RestController
public class HelloController {

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello() {
    return "hello";
  }

}
