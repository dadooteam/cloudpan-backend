package im.dadoo.cloudpan.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2017/1/7.
 */
@Controller
public class HelloController {

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  @ResponseBody
  public String hello(HttpServletRequest request, HttpServletResponse response) {
    return "hello";
  }

}
