package kr.co.goms.module.common.manager;

import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.task.RequestParamTask;
import kr.co.goms.module.common.task.ServerTask;

/**
 * httpClient 매니저
 * 1.RequestItem 설정
 *         RequestItem requestItem = new RequestItem();
 *         requestItem.targetUrl = url;
 *         requestItem.targetMethodType = "POST";
 *         requestItem.headerMap = new HashMap<String, String>();
 *         requestItem.bodyMap = new HashMap<String, String>();
 *         requestItem.fileMap = new ArrayMap<String, File>();
 *         //requestItem.bodyMap.put("mbIdx", "1");
 *         //requestItem.bodyMap.put("mbToken", "sdfsdfaddffsdfsdf");
 *
 * 2.HttpClientManager instance 생성 및 sendServerData 처리
 *         HttpClientManager.I().sendServerData(requestItem, new ServerTask.OnAsyncResult() {
 *             @Override
 *             public void onResultSuccess(int resultCode, String message, String jsonString) {
 *                 Log.d(TAG, "json(서버) : " + jsonString);
 *
 *                 jsonString = "{'res_num':'25001120','res_result':'200','res_message':'Success','res_datetime':'2022-07-11 09:51:19'," +
 *                         "'res_data':[" +
 *                         "{'res_calendar_idx':'1','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good1','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'1111','res_calendar_comment_date':'2022-07-01','res_calendar_comment_stime':'11:10','res_calendar_comment_etime':'13:10','res_calendar_comment_place':'강남구청역#1','res_regdate':'조금전'}," +
 *                         "{'res_calendar_idx':'2','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good2','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'2222','res_calendar_comment_date':'2022-07-08','res_calendar_comment_stime':'12:20','res_calendar_comment_etime':'14:10','res_calendar_comment_place':'강남구청역#2','res_regdate':'30분 전'}," +
 *                         "{'res_calendar_idx':'3','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good3','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'3333','res_calendar_comment_date':'2022-07-18','res_calendar_comment_stime':'13:30','res_calendar_comment_etime':'15:10','res_calendar_comment_place':'강남구청역#3','res_regdate':'1시간 전'}," +
 *                         "{'res_calendar_idx':'4','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good4','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'4444','res_calendar_comment_date':'2022-07-28','res_calendar_comment_stime':'14:40','res_calendar_comment_etime':'16:10','res_calendar_comment_place':'강남구청역#4','res_regdate':'2022-07-11 09:51:10'}," +
 *                         "{'res_calendar_idx':'5','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good5','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'5555','res_calendar_comment_date':'2022-07-30','res_calendar_comment_stime':'15:50','res_calendar_comment_etime':'17:10','res_calendar_comment_place':'강남구청역#5','res_regdate':'2022-07-11 09:51:10'}," +
 *                         "{'res_calendar_idx':'5','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good5','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'5555','res_calendar_comment_date':'2022-07-30','res_calendar_comment_stime':'15:50','res_calendar_comment_etime':'17:10','res_calendar_comment_place':'강남구청역#5','res_regdate':'2022-07-11 09:51:10'}," +
 *                         "{'res_calendar_idx':'5','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good5','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'5555','res_calendar_comment_date':'2022-07-30','res_calendar_comment_stime':'15:50','res_calendar_comment_etime':'17:10','res_calendar_comment_place':'강남구청역#5','res_regdate':'2022-07-11 09:51:10'}," +
 *                         "{'res_calendar_idx':'5','res_mb_idx':'1','res_mb_name':'omaro','res_mb_profile':'good5','res_mb_profile_path':'','res_calendar_comment_color':'yellow','res_calendar_comment_cate':'업무일지','res_calendar_comment_title':'제목입니다','res_calendar_comment_msg':'5555','res_calendar_comment_date':'2022-07-30','res_calendar_comment_stime':'15:50','res_calendar_comment_etime':'17:10','res_calendar_comment_place':'강남구청역#5','res_regdate':'2022-07-11 09:51:10'}" +
 *                         "]" +
 *                         "}";
 *
 *                 Log.d(TAG, "json(로컬) : " + jsonString);
 *
 * 3. JsonParserManager instance 생성, 초기화 및 parser 처리
 *                 JsonParserManager.I().init();
 *                 JsonParserManager.I().parserYouTubeJsonData(jsonString, JsonParserManager.JSON_PARSER_NAME.calendar_comment.name(), new JsonParserManager.OnParserResult() {
 *                     @Override
 *                     public void onParserResultSuccess(int resultCode, Object object) {
 *
 *                         ArrayList<YoutubeBeanS> youtubeList = (ArrayList<YoutubeBeanS>)object;
 *                         Log.d(TAG, "youtubeList 갯수 : " + youtubeList.size());
 *
 *                         //왜 화면로딩이 되지 않을까?? -> Thread 처리
 *                         if (Looper.myLooper() == Looper.getMainLooper()) {
 *                             //setCalendarCommentAdapter(calendarList);
 *                         } else {
 *                             // WorkThread이면, MainThread에서 실행 되도록 변경.
 *                             getActivity().runOnUiThread(new Runnable() {
 *                                 @Override
 *                                 public void run() {
 *                                     //setCalendarCommentAdapter(calendarList);
 *                                 }
 *                             });
 *                         }
 *                     }
 *
 *                     @Override
 *                     public void onParserResultFail(int resultCode, String errorMessage) {
 *
 *                     }
 *                 });
 *             }
 *
 *             @Override
 *             public void onResultFail(int resultCode, String errorMessage) {
 *                 Log.d(TAG, "json : " + errorMessage);
 *             }
 *         });
 */

public class HttpClientManager {

    static final String LOG_TAG = HttpClientManager.class.getSimpleName();

    static HttpClientManager instance;

    protected HttpClientManager() {

    }

    protected void onDestroy() {

    }

    public static HttpClientManager I() {
        if(null == instance) {
            instance = new HttpClientManager();
        }

        return instance;
    }

    public static void D() {
        if(null != instance) {
            instance.onDestroy();
            instance = null;
        }
    }

    public void sendServerData(RequestItem item, ServerTask.OnAsyncResult onAsyncResult){
        RequestParamTask requestParamTask = new RequestParamTask();
        requestParamTask.setClient();
        requestParamTask.setItem(item);
        requestParamTask.setRequestBody();
        requestParamTask.setOnAsyncResult(onAsyncResult);
        requestParamTask.setExecute();
    }
    
}
