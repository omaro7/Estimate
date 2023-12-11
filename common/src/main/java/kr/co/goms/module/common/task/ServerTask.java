package kr.co.goms.module.common.task;

import android.os.AsyncTask;

import java.net.ConnectException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kr.co.goms.module.common.request.RequestUrlCode;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerTask extends AsyncTask<Void, Void, String> implements RequestUrlCode {

    private static final String LOG_TAG = kr.co.goms.module.common.task.ServerTask.class.getSimpleName();

    private OkHttpClient okClient;
    private kr.co.goms.module.common.task.ServerTask.OnAsyncResult onAsyncResult;
    private RequestItem mItem;
    private RequestBody mRequestBody;
    private Request mRequest;

    public ServerTask() {
    }

    public void setExecute(){
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //this.execute();
    }

    public void setClient(){
        this.okClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();    // socket timeout
    }

    public void setOnAsyncResult(kr.co.goms.module.common.task.ServerTask.OnAsyncResult onAsyncResult){
        this.onAsyncResult = onAsyncResult;
    }

    public void setItem(RequestItem item){
        this.mItem = item;
    }

    public void setRequestBody(){
        int iFileTotal = 0;
        try {
            iFileTotal = mItem.fileMap.size();
        }catch(Exception e){

        }

        if(iFileTotal > 0){
            setRequestMultipartBody();
        }else{
            setRequestFormBody();
        }
    }

    public void setRequestMultipartBody(){

        String tmp = getRequestBodyString();
        String[] param = tmp.split("\\^");

        int iTotal = 0;
        try{
            iTotal = mItem.bodyMap.size();
        }catch(Exception e){

        }

        //.addFormDataPart("tarotResultFile","tarot_result_file.jpg", RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
        if(iTotal == 0) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 1) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 2) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 3) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 4) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 5) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 6) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(param[10], param[11])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 7) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(param[10], param[11])
                    .addFormDataPart(param[12], param[13])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 8) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(param[10], param[11])
                    .addFormDataPart(param[12], param[13])
                    .addFormDataPart(param[14], param[15])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 9) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(param[10], param[11])
                    .addFormDataPart(param[12], param[13])
                    .addFormDataPart(param[14], param[15])
                    .addFormDataPart(param[16], param[17])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }else if(iTotal == 10) {
            mRequestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param[0], param[1])
                    .addFormDataPart(param[2], param[3])
                    .addFormDataPart(param[4], param[5])
                    .addFormDataPart(param[6], param[7])
                    .addFormDataPart(param[8], param[9])
                    .addFormDataPart(param[10], param[11])
                    .addFormDataPart(param[12], param[13])
                    .addFormDataPart(param[14], param[15])
                    .addFormDataPart(param[16], param[17])
                    .addFormDataPart(param[18], param[19])
                    .addFormDataPart(mItem.fileMap.valueAt(0).getName(), mItem.fileMap.valueAt(0).getName(), RequestBody.create(MultipartBody.FORM, mItem.fileMap.valueAt(0)))
                    .build();
        }

    }

    public void setRequestFormBody(){

        String tmp = getRequestBodyString();
        String[] param = tmp.split("\\^");

        int iTotal = 0;
        try{
            iTotal = mItem.bodyMap.size();
        }catch(Exception e){

        }

        if(iTotal == 0) {
            mRequestBody = new FormBody.Builder()
                    .build();
        }else if(iTotal == 1) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .build();
        }else if(iTotal == 2) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .build();
        }else if(iTotal == 3) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .build();
        }else if(iTotal == 4) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .build();
        }else if(iTotal == 5) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .build();
        }else if(iTotal == 6) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .add(param[10], param[11])
                    .build();
        }else if(iTotal == 7) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .add(param[10], param[11])
                    .add(param[12], param[13])
                    .build();
        }else if(iTotal == 8) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .add(param[10], param[11])
                    .add(param[12], param[13])
                    .add(param[14], param[15])
                    .build();
        }else if(iTotal == 9) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .add(param[10], param[11])
                    .add(param[12], param[13])
                    .add(param[14], param[15])
                    .add(param[16], param[17])
                    .build();
        }else if(iTotal == 10) {
            mRequestBody = new FormBody.Builder()
                    .add(param[0], param[1])
                    .add(param[2], param[3])
                    .add(param[4], param[5])
                    .add(param[6], param[7])
                    .add(param[8], param[9])
                    .add(param[10], param[11])
                    .add(param[12], param[13])
                    .add(param[14], param[15])
                    .add(param[16], param[17])
                    .add(param[18], param[19])
                    .build();
        }

    }

    private String getRequestBodyString(){
        Iterator<Map.Entry<String, String>> iterator = mItem.bodyMap.entrySet().iterator();

        String params = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            if(StringUtil.isEmpty(params)) {
                params = param.getKey() + "^" + param.getValue();
            } else {
                params += "^" + param.getKey() + "^" + param.getValue();
            }
        }

        return params;
    }

    public void setRequest(){

        if(mRequestBody == null){
            setRequestFormBody();
        }

        mRequest = new Request.Builder()
                .url(mItem.targetUrl)
                .post(mRequestBody)
                .build();
    }

    @Override
    protected void onPreExecute() {

        if(null == okClient) {
            okClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();    // socket timeout
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            setRequest();

            Response response = okClient.newCall(mRequest).execute();

            if (!response.isSuccessful()) {
                onAsyncResult.onResultFail(REQUEST_FAILURE, "Your Result failed", response.body().string());
            } else {
                onAsyncResult.onResultSuccess(REQUEST_SUCCESS, "You Got Success Value", response.body().string());
            }
            return "";
        }catch (ConnectException e){
            e.printStackTrace();
            GomsLog.d("Stemp", "네트워크 접속에러입니다.");
            onAsyncResult.onResultFail(REQUEST_CONNECT_ERROR, "네트워크 접속에러입니다.", e.toString());
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            onAsyncResult.onResultFail(REQUEST_FAILURE, "Your Result failed", e.toString());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String o) {

    }

    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, String message, String jsonString);
        void onResultFail(int resultCode, String errorMessage, String jsonString);
    }

}
