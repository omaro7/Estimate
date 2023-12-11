package kr.co.goms.module.common.observer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import kr.co.goms.module.common.base.BaseBean;

public class ProcessGather {
    private static final String LOG_TAG = "ProcessGather";
    private Handler handler = null;
    private HashMap<Integer, Object> processHashMap = null;
    private Complete complete = null;
    private int iGatherMax = 10;

    public ProcessGather() {
    }

    public void create() {
        Log.d("ProcessGather", "create()");
        if (null == this.handler) {
            this.handler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message msg) {
                    Log.d("ProcessGather", "handleMessage()");
                    PROCESS process = PROCESS.ITEM_PID;
                    PROCESS[] processList = PROCESS.values();

                    for(int n = 0; n < processList.length; ++n) {
                        if (processList[n].ordinal() == msg.what) {
                            process = processList[n];
                            break;
                        }
                    }

                    switch(process) {
                        case ITEM_PID:
                            this.onItemPidFinish(msg.arg1, msg.obj);
                            break;
                        case TIME_LIMIT:
                            this.onTimeLimit();
                    }

                }

                void onItemPidFinish(int pid, Object object) {
                    Log.d("ProcessGather", "onItemPidFinish( " + pid + " )");

                    Object ProcessGatherObject = null;
                    if(BaseBean.STATUS.FINISH.equals(((BaseBean)object).getStatus())){
                        ProcessGatherObject = ProcessGather.this.onRemove(pid);
                    }

                    //Object object = ProcessGather.this.onRemove(pid);
                    //ProcessGather.this.onRemove(pid);
                    int remainProcess = ProcessGather.this.processHashMap.size();
                    if ( null == ProcessGatherObject && 0 == remainProcess) {
                        Log.d("ProcessGather", "onItemPidFinish() 이미 완료 되었다");
                    } else {
                        boolean completeFlag = true;
                        if(BaseBean.STATUS.FINISH.equals(((BaseBean)object).getStatus())) {
                            if (0 < remainProcess) {
                                Log.d("ProcessGather", "Remain Process : " + remainProcess);
                                completeFlag = false;
                            } else {
                                Log.d("ProcessGather", "Remain Process : " + remainProcess);
                                completeFlag = true;
                            }
                        }

                        if (null != ProcessGather.this.complete) {
                            ProcessGather.this.complete.complete(completeFlag, pid, object, remainProcess);
                        }

                    }
                }

                void onTimeLimit() {
                    Log.d("ProcessGather", "onTimeLimit()");
                    Iterator<Integer> iterator = ProcessGather.this.processHashMap.keySet().iterator();
                    if (iterator.hasNext()) {
                        int pid = (Integer)iterator.next();
                        ProcessGather.this.finishProcess(pid);
                    }

                }
            };
        }

        if (null == this.processHashMap) {
            this.processHashMap = new HashMap();
        }


    }

    public int createProcess(Object value) {
        UUID uuid = UUID.randomUUID();
        int pid = uuid.hashCode();
        int loopCount = iGatherMax;

        while(0 < loopCount) {
            Object obj = this.processHashMap.get(pid);
            if (null != obj) {
                uuid = UUID.randomUUID();
                pid = uuid.hashCode();
                --loopCount;
                Log.d("ProcessGather", "createProcess Overlap, Reissue, PID : " + pid + ", NAME : " + value);
            } else {
                loopCount = 0;
                Log.d("ProcessGather", "createProcess Now Issue, PID : " + pid + ", NAME : " + value);
            }
        }

        this.processHashMap.put(pid, value);
        return pid;
    }

    public void finishProcess(int pid) {
        synchronized(this) {
            Log.d("ProcessGather", "finishProcess : " + pid);
            Object obj = this.processHashMap.get(pid);
            if (null == obj) {
                Log.d("ProcessGather", "finishProcess : 요청 객체가 존재하지 않음");
            } else {
                Message message = new Message();
                message.what = PROCESS.ITEM_PID.ordinal();
                message.arg1 = pid;
                this.handler.sendMessage(message);
                Log.d("ProcessGather", "finishProcess send : " + pid);
            }
        }
    }

    public void finishProcess(int pid, Object object) {
        synchronized(this) {
            Log.d("ProcessGather", "finishProcess : " + pid);
            Object obj = this.processHashMap.get(pid);
            if (null == obj) {
                Log.d("ProcessGather", "finishProcess : 요청 객체가 존재하지 않음");
            } else {
                Message message = new Message();
                message.what = PROCESS.ITEM_PID.ordinal();
                message.arg1 = pid;
                message.obj = object;
                this.handler.sendMessage(message);
                Log.d("ProcessGather", "finishProcess send : " + pid);
            }
        }
    }

    public int getProcess(String objectValue){

        Log.d("ProcessGather", "getProcess objectValue : " + objectValue);

        if(processHashMap == null){
            Log.d("ProcessGather", "getProcess processHashMap null 입니다");
            return -1;
        }

        Iterator<?> keys = processHashMap.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            if(objectValue.equals(processHashMap.get(key))){
                Log.d("ProcessGather", "key: " + key + ", object : " + processHashMap.get(key));
                return (int)key;
            }
        }
        return -1;
    }

    public void startTimeLimit(long timeLimit) {
        Log.d("ProcessGather", "startTimeLimit : " + timeLimit);
        this.handler.sendEmptyMessageDelayed(PROCESS.TIME_LIMIT.ordinal(), timeLimit);
    }

    private Object onRemove(int pid) {
        if (null == this.processHashMap) {
            return null;
        } else {
            Log.d("ProcessGather", "onRemove : " + pid);
            return this.processHashMap.remove(pid);
        }
    }

    public void setOnCompleteListener(Complete completeListener) {
        this.complete = completeListener;
    }

    public interface Complete {
        void complete(boolean var1, int var2, Object var3, int var4);
    }

    protected static enum PROCESS {
        ITEM_PID,
        TIME_LIMIT;

        private PROCESS() {
        }
    }

    public void clear(){
        processHashMap.clear();
    }
}
