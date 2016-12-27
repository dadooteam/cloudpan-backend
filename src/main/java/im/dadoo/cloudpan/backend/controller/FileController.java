package im.dadoo.cloudpan.backend.controller;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.so.FileSo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codekitten on 2016/12/27.
 */
@Controller
public class FileController {

    private static final Logger SLOGGER = LoggerFactory.getLogger("stat");

    @Resource
    private Gson gson;

    @Resource
    private FileSo fileSo;

    @RequestMapping(value = "/traverse", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> traverse(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getParameter("path");
        Result<List<FileDto>> r = this.fileSo.traverse(path);

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("path", path);
        SLOGGER.info(this.gson.toJson(logMap));
        return r;
    }
}
