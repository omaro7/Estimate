package kr.co.goms.module.common.base;

public class BaseBean  {
    private BaseBean.STATUS status;
    private String data;
    private Object object;

    public BaseBean() {
    }

    public void setStatus(BaseBean.STATUS status, String data) {
        this.status = status;
        this.data = data;
    }

    public void setStatus(BaseBean.STATUS status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public BaseBean.STATUS getStatus() {
        return this.status;
    }

    public String getData() {
        return this.data;
    }

    public Object getObject() {
        return this.object;
    }

    public enum STATUS {
        SUCCESS,
        PROGRESS,
        FAIL,
        ERROR,
        FINISH;

        private STATUS() {
        }
    }
}
