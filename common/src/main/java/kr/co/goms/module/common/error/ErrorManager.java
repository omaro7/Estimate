package kr.co.goms.module.common.error;

import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.model.CommonBean;

/**
 * 에러 매니져
 ErrorManager.I().builder((CommonBean)baseBean.getObject()).command(getActivity(), (CommonBean)baseBean.getObject());
 */
public class ErrorManager {


    static ErrorManager mErrorManager;    //instance

    public enum ErrorCode {
        EC_NULL,
        EC_201,
        EC_400,
        EC_404,
        EC_1001,
        EC_1002,
        EC_1003,
    }

    public ErrorManager() {

    }

    public static ErrorManager I(){
        if(mErrorManager == null){
            mErrorManager = new ErrorManager();
        }
        return mErrorManager;
    }

    public void destroy() {

    }

    public Command builder(CommonBean commonBean){

        Command command = null;

        if(commonBean == null){
            return command;
        }

        ErrorCode errorCode = findErrorCode("EC_" + commonBean.getRcode());
        if(null != errorCode) {
            switch (errorCode) {
                case EC_201:
                    command = new CommandErrorCode201();
                    break;
                case EC_400:
                case EC_404:
                    command = new CommandErrorCode400();
                    break;
                case EC_1001:
                case EC_1002:
                case EC_1003:
                    command = new CommandExitApp();
                    break;
                default:
                    command = new CommandErrorCodeNull();
                    break;
            }
        }
        return command;
    }

    /**
     * 정의된 에러코드 찾기
     * @param errorCode
     * @return
     */
    public ErrorCode findErrorCode(String errorCode) throws IllegalArgumentException {
        ErrorCode[] values = ErrorCode.values();
        for(int n = 0 ; n < values.length ; ++n) {
            if(values[n].toString().equalsIgnoreCase(errorCode)) {
                return ErrorCode.valueOf(errorCode);
            }
        }
        return null;
    }
}
