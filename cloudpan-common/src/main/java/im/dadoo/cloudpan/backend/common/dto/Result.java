package im.dadoo.cloudpan.backend.common.dto;

/**
 * Created by codekitten on 2016/12/27.
 */
public class Result<T> {

    private int code;

    private int status;

    private String message;

    private T data;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Result{");
        sb.append("code=").append(code);
        sb.append(", status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
