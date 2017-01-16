package im.dadoo.cloudpan.backend.dto;

/**
 * Created by codekitten on 2017/1/7.
 */
public class FileDto {

  //最近更新时间戳
  private long gmtModify;

  //文件路径
  private String path;

  //文件名or文件夹名
  private String name;

  //文件类型（文件夹为空）
  private String mime;

  //文件大小（文件夹为0）
  private long size;

  //文件类型，1文件，2文件夹
  private int type;

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("FileDto{");
    sb.append("gmtModify=").append(gmtModify);
    sb.append(", path='").append(path).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", mime='").append(mime).append('\'');
    sb.append(", size=").append(size);
    sb.append(", type=").append(type);
    sb.append('}');
    return sb.toString();
  }

  public long getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(long gmtModify) {
    this.gmtModify = gmtModify;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
