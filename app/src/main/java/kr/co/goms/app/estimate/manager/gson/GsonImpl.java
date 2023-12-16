package kr.co.goms.app.estimate.manager.gson;

public interface GsonImpl {
    void builder();
    Object from(String data, Class gClass);
}
