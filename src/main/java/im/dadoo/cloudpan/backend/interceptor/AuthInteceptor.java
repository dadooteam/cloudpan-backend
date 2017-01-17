package im.dadoo.cloudpan.backend.interceptor;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.constant.CloudpanCode;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.dto.UserDto;
import im.dadoo.cloudpan.backend.so.UserSo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by codekitten on 2017/1/17.
 */
@Component
public class AuthInteceptor implements HandlerInterceptor {

  @Autowired
  private Gson gson;

  @Autowired
  private UserSo userSo;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    boolean r = false;
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isNotBlank(token)) {
      Result<UserDto> rUserDto = this.userSo.auth(token);
      if (CloudpanCode.OK.getCode() == rUserDto.getCode()) {
        UserDto userDto = rUserDto.getData();
        request.setAttribute("visitorId", userDto.getId());
        r = true;
      } else {
        response.setStatus(rUserDto.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        try {
          writer.append(this.gson.toJson(rUserDto));
        } finally {
          if (writer != null) {
            writer.close();
          }
        }
      }
    }
    return r;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

  }
}
