package im.dadoo.cloudpan.backend.common.dto;

/**
 * Created by codekitten on 2017/1/7.
 */
public class CloudFileDto {

  private long id;

  private long gmtCreate;

  private long gmtModify;

  private long userId;

  private long supId;

  private String name;

  private String mime;

  private long size;

  private String md5;

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("CloudFileDto{");
    sb.append("id=").append(id);
    sb.append(", gmtCreate=").append(gmtCreate);
    sb.append(", gmtModify=").append(gmtModify);
    sb.append(", userId=").append(userId);
    sb.append(", supId=").append(supId);
    sb.append(", name='").append(name).append('\'');
    sb.append(", mime='").append(mime).append('\'');
    sb.append(", size=").append(size);
    sb.append(", md5='").append(md5).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(long gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public long getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(long gmtModify) {
    this.gmtModify = gmtModify;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getSupId() {
    return supId;
  }

  public void setSupId(long supId) {
    this.supId = supId;
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

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }
}
