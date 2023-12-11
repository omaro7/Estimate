package kr.co.goms.module.common.task;

import android.util.ArrayMap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RequestItem {

    public String targetUrl;
    public String targetMethodType;
    public Map<String, String> headerMap;
    public Map<String, String> bodyMap;
    public ArrayMap<String, File> fileMap;

    public RequestItem() {
        this.targetMethodType = "POST";
        this.headerMap = new HashMap<String, String>();
        this.bodyMap = new HashMap<String, String>();
        this.fileMap = new ArrayMap<String, File>();
    }
}
