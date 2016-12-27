package im.dadoo.cloudpan.backend.so;

import com.google.common.io.Files;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codekitten on 2016/12/27.
 */

@Service
public class FileSo {

    private static final Logger ELOGGER = LoggerFactory.getLogger(Exception.class);

    @Resource
    private Environment env;

    public Result<List<FileDto>> traverse(String path) {
        Result<List<FileDto>> r = new Result<>();
        r.setCode(500_001_000);
        try {
            if (StringUtils.isNotBlank(path)) {
                String fullpath = this.env.getProperty("master.path") + path;
                File fullFile = new File(fullpath);
                if (fullFile != null && fullFile.exists()) {
                List<FileDto> data = Arrays.asList(fullFile.listFiles()).stream().map(file -> {
                        FileDto fileDto = new FileDto();
                        fileDto.setName(file.getName());
                        fileDto.setDirectory(file.isDirectory());
                        return fileDto;
                    }).collect(Collectors.toList());
                    r.setData(data);
                }
            }
            r.setCode(200_001_000);
        } catch (Exception e) {

        }
        return r;
    }

}
