package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
                String fullpath = this.env.getProperty("master.path") + "/" +path;
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
            ELOGGER.error(String.format("%s遍历失败", path), e);
        }
        r.setStatus(r.getCode() / 1_000_000);
        return r;
    }

    public Result<File> download(String path) {
        Result<File> r = new Result<>();
        r.setCode(500_001_000);
        try {
            if (StringUtils.isNotBlank(path)) {
                String fullpath = this.env.getProperty("master.path") + "/" + path;
                File fullFile = new File(fullpath);
                if (fullFile != null && fullFile.exists() && fullFile.isFile()) {
                    r.setData(fullFile);
                }
            }
            r.setCode(200_001_000);
        } catch (Exception e) {
            ELOGGER.error(String.format("%s下载失败", path), e);
        }
        r.setStatus(r.getCode() / 1_000_000);
        return r;
    }

    public Result<Object> upload(String path, MultipartFile file) {
      Result<Object> r = new Result<>();
      r.setCode(500_001_000);
      try {
        String directory = this.env.getProperty("master.path") + "/" + path;
        String fullpath = directory + "/" + file.getOriginalFilename();
        File newFile = new File(fullpath);
        if (newFile.exists()) {
          newFile.delete();
        }

        if (new File(directory).mkdirs() && newFile.createNewFile()) {
          FileCopyUtils.copy(file.getInputStream(), new BufferedOutputStream(new FileOutputStream(newFile)));
        } else {
          throw new Exception(String.format("创建新文件失败，路径%s，文件名%s", fullpath, file.getOriginalFilename()));
        }
        r.setCode(200_001_000);
      } catch (Exception e) {
        r.setMessage(e.getLocalizedMessage());
        ELOGGER.error(Long.toString(r.getCode()), e);
      }
      r.setStatus(r.getCode() / 1_000_000);
      return r;
    }

}
