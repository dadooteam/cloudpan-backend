package im.dadoo.cloudpan.backend.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于slf4j日志输出
 *
 * @author shuwen
 * @since 1.0
 */
@Component
public class MdcInterceptor implements HandlerInterceptor {

  private static final String HTTP_METHOD = "HttpMethod";
  private static final String REQUEST_URI = "RequestUri";
  private static final String REMOTE_ADDR = "RemoteAddr";
  private static final String X_SOURCE = "XSource";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String remoteAddr;
    if (StringUtils.isNotBlank(request.getHeader("X-Forwarded-For"))) {
      remoteAddr = request.getHeader("X-Forwarded-For").split(",")[0];
    } else if (StringUtils.isNotBlank(request.getHeader("X-Real-IP"))) {
      remoteAddr = request.getHeader("X-Real-IP");
    } else {
      remoteAddr = request.getRemoteAddr();
    }
    MDC.put(REMOTE_ADDR, remoteAddr);
    MDC.put(HTTP_METHOD, request.getMethod());
    String queryString = request.getQueryString();
    if (StringUtils.isNotBlank(queryString)) {
      MDC.put(REQUEST_URI, request.getRequestURI() + "?" + queryString);
    } else {
      MDC.put(REQUEST_URI, request.getRequestURI());
    }
    MDC.put(X_SOURCE, request.getHeader("X-SOURCE"));
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    MDC.remove(REMOTE_ADDR);
    MDC.remove(HTTP_METHOD);
    MDC.remove(REQUEST_URI);
    MDC.remove(X_SOURCE);
  }

}
