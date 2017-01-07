package im.dadoo.cloudpan.backend.common.constant;

/**
 * Created by codekitten on 2016/12/27.
 */
public enum CloudpanCode {

  OK(200_001_000, "接口返回正常"),

  SUP_NOT_FOUND(400_001_001, "上级文件夹不存在"),
  NAME_EXIST(400_001_002, "重名"),

  DIR_NOT_CREATED(400_001_003, "内部文件夹创建失败"),

  FORBIDDEN(403_001_000, "拒绝此操作"),

  NONAME_ERROR(500_001_000, "未知错误"),
  NETWORK_ERROR(500_001_001, "网络错误");

  private int code;
  private String name;

  CloudpanCode(int code, String name) {
    this.code = code;
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("CloudpanCode{");
    sb.append("code=").append(code);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
