package kr.co.goms.module.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.co.goms.module.common.R;

public class DialogDownloadProcessing extends Dialog {

	private final String TAG = DialogDownloadProcessing.class.getSimpleName();
	private final int XML_LAYOUT = R.layout.dialog_download_processing;
	static Context mContext;

	TextView mTvTitle;
	TextView mTvSubTitle;
	TextView mTvProcessByteTxt;
	TextView mTvProcessPercentTxt;
	ProgressBar mPbTotal;
	ProgressBar mPbItem;

	public DialogDownloadProcessing(Context context) {
		super(context);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_FULLSCREEN;
		lpWindow.dimAmount = 1.0f;
		// lpWindow.gravity = Gravity.CENTER;

		getWindow().setAttributes(lpWindow);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		setContentView(XML_LAYOUT);

		mPbTotal = (ProgressBar) findViewById(R.id.pb_file_total);
		mPbItem = (ProgressBar) findViewById(R.id.pb_file_item);

		mTvTitle = (TextView) findViewById(R.id.tv_dialog_download_processing_title);
		mTvSubTitle = findViewById(R.id.tv_dialog_download_processing_subtitle);
		mTvProcessByteTxt = (TextView) findViewById(R.id.tv_dialog_processing_byte_txt);
		mTvProcessPercentTxt = (TextView) findViewById(R.id.tv_dialog_processing_percent_txt);

		/** 다이얼로그에 중간 제목구분바 안보이게 하기 **/
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			int dividerId = getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
			if (dividerId != 0) {
				View divider = findViewById(dividerId);
				divider.setVisibility(View.GONE);
			}
		}
	}

	public DialogDownloadProcessing(Context context, int theme) {
		super(context, theme);
	}

	public void setHeaderTitle(String title) {
		mTvTitle.setText(Html.fromHtml(title));
	}
	public void setHeaderSubTitle(String title) {
		mTvSubTitle.setText(Html.fromHtml(title));
	}
	public void setBottomProcessByteTxt(String txt) {
		mTvProcessByteTxt.setText(Html.fromHtml(txt));
	}
	public void setBottomProcessPercentTxt(String txt) {
		mTvProcessPercentTxt.setText(Html.fromHtml(txt));
	}

	public void setProgressBarTotalMax(int max){
		mPbTotal.setMax(max);
	}
	public void setProgressBarTotal(int progress){
		mPbTotal.setProgress(progress);
	}
	public void setProgressBarItem(int progress){
		mPbItem.setProgress(progress);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
	
}
