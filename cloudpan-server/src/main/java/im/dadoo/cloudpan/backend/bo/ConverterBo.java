package im.dadoo.cloudpan.backend.bo;

import im.dadoo.cloudpan.backend.common.constant.CloudpanConstant;
import im.dadoo.cloudpan.backend.common.dto.FileDto;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codekitten on 2017/1/7.
 */
@Component
public class ConverterBo {

  @Resource
  private Environment env;

  public FileDto toCloudFileDto(CloudFilePo po) {
    FileDto r = null;
    if (po != null) {
      r = new FileDto();
      r.setId(po.getId());
      r.setGmtCreate(po.getGmtCreate());
      r.setGmtModify(po.getGmtModify());
      r.setSupId(po.getSupId());
      r.setName(po.getName());
      r.setMime(po.getMime());
      r.setSize(po.getSize());
      if (StringUtils.isBlank(po.getMd5())) {
        r.setType(CloudpanConstant.TYPE_DIR);
      } else {
        r.setType(CloudpanConstant.TYPE_FILE);
      }
    }
    return r;
  }

  public List<FileDto> toCloudFileDtos(List<CloudFilePo> pos) {
    return pos.stream().filter(po ->  po != null).map(po -> this.toCloudFileDto(po)).collect(Collectors.toList());
  }
}
