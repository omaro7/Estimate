package kr.co.goms.module.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.command.CommandExit;
import kr.co.goms.module.common.manager.DialogCommandFactory;
import kr.co.goms.module.common.manager.DialogManager;

/**
 * Created by USER on 2016-12-18.
 */
public class DialogPermissionNotice extends Dialog {

    private OnClickListener positiveListener = null;
    private Context mContext;
    private Activity mActivity;
    public DialogPermissionNotice(Activity activity, Context context, OnClickListener positiveListener) {
        super(context);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivity = activity;
        this.mContext = context;
        this.positiveListener = positiveListener;

        setContentView(R.layout.dialog_permission_notice);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        init();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            RelativeLayout root =(RelativeLayout)findViewById(R.id.root);
            DisplayMetrics dm=mContext.getResources().getDisplayMetrics();
            int top= Math.round(20*dm.density);
            root.setPadding(0, top,0,0);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#6a72d1"));
        }
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setViewElements();
    }
    ImageView close;
    private void setViewElements() {
        TextView positiveBtn = (TextView) findViewById(R.id.btn_ok);
        if (positiveBtn != null) {
           positiveBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   dismiss();
                   if (positiveListener != null) {
                       positiveListener.onClick(DialogPermissionNotice.this, DialogInterface.BUTTON_POSITIVE);
                   }
               }
           });
        }
    }

    @Override
    public void onBackPressed() {
        DialogManager.I().setTitle("앱사용 권한 안내")
                .setMessage("앱을 사용하기 위해서는 [전화,저장] 권한을 허용해 주셔야 정상적인 서비스 이용이 가능합니다.\n\n앱 권한 설정화면으로 이동하시겠습니까?")
                .setShowTitle(false)
                .setShowMessage(true)
                .setNegativeBtnName(mActivity.getString(R.string.no))
                .setPositiveBtnName(mActivity.getString(R.string.yes))
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setCommand(DialogCommandFactory.I().createDialogCommand(mActivity, DialogCommandFactory.DIALOG_TYPE.permission.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            //앱 종료 처리
                            Command exit = new CommandExit();
                            exit.command(mActivity, "");
                            mActivity.finish();
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            //설정이동
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        }
                    }
                }))
                .showDialog(mActivity);
    }

}
