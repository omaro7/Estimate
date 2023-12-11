package kr.co.goms.module.common.router;


import kr.co.goms.module.common.command.Command;

public class RouterView {

    static Class activity;

    /**
     * 초기화 커맨드
     */
    static Class<Command> commandInit;

    /**
     * 초기 전문 설정
     */
    static Class<Command> commandFirstNetwork;

    /**
     * 백그라운드 이미지 ID
     */
    static int backgroundResId;

    /**
     * @param activity 라우터 액티비티
     * @param commandInit 초기화 커맨드
     * @param commandFirstNetwork 초기 전문 클래스 설정
     *     */
    public static void setting(Class activity, Class commandInit, Class commandFirstNetwork, int backgroundResId) {
        RouterView.activity = activity;
        RouterView.commandInit = commandInit;
        RouterView.commandFirstNetwork = commandFirstNetwork;
        RouterView.backgroundResId = backgroundResId;
    }

    public static Class getActivity() {
        return activity;
    }

    /**
     * 초기 전문 수행 클래스 생성
     * @return
     */
    public static Command createCommandFirstNetwork() {

        Command resultCommand = null;

        try {
            resultCommand = commandFirstNetwork.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return resultCommand;
    }

    /**
     * 초기화
     * @return
     */
    public static Command createCommandInit() {

        Command resultCommand = null;

        try {
            resultCommand = commandInit.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return resultCommand;
    }

    /**
     * 백그라운드 이미지 ID
     * @return
     */
    public static int getBackgroundResId() {
        return backgroundResId;
    }
}
