package kr.co.goms.module.common.task;

import android.util.ArrayMap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RequestItem {

    public String targetUrl;
    public String targetMethodType;
    public Map<String, String> headerMap;
    public Map<String, Object> bodyMap;
    public ArrayMap<Integer, File> fileMap;

    public RequestItem() {
        this.targetMethodType = "POST";
        this.headerMap = new HashMap<String, String>();
        this.bodyMap = new HashMap<String, Object>();
        this.fileMap = new ArrayMap<Integer, File>();
    }
}
