package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import kr.co.goms.module.common.base.WaterCallBack;

/**
 * 사용법
 * CurvletManager.process(this, null, "water://review");
 */
public class CurvletPlayStoreReview extends CurvletCommon {

    private static final String LOG_TAG = CurvletPlayStoreReview.class.getSimpleName();

    @Override
    protected void onDoGet(WaterCallBack view, Uri uri, String value) {
        Log.d(LOG_TAG, "onDoGet()");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onReview();
            }
        });
    }

    /**
     * 리뷰 팝업 띄우기
     */
    private void onReview() {
        Log.d(LOG_TAG, "onReview()");

        ReviewManager manager = OneQPayReviewManager.createReviewManager(activity);

        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                Log.d(LOG_TAG, "onReview() >> onComplete()");
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "onReview() >> onComplete()-Successful : " + task.toString());
                    ReviewInfo reviewInfo = task.getResult();
                    onReviewInfo(manager.launchReviewFlow(activity, reviewInfo));
                } else {
                    Log.d(LOG_TAG, "onReview() >> onComplete()-Error : " + task.getException());
                }
            }
        });
    }

    /**
     * 유저의 선택 결과 대기 - 나중에 or 제출
     * @param flow
     */
    private void onReviewInfo(Task<Void> flow) {
        Log.d(LOG_TAG, "onReviewInfo()");

        flow.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(LOG_TAG, "onReviewInfo() >> onComplete() : " + task.toString());
                if(task.isSuccessful()) {
                    onToast("리뷰 감사합니다.");
                    // 접속 할 수 있는 횟수가 있음. 1번 인듯 함
                    // 이미 했으면 그냥 이곳으로 들어옴
                    Log.d(LOG_TAG, "onReviewInfo() >> onComplete() >> Successful");
                } else {
                    onToast("리뷰를 하지 못했습니다.");
                    // 접속 할 수 있는 횟수가 있다보니, 테스트가 쉽지 않쿤
                    Log.d(LOG_TAG, "onReviewInfo() >> onComplete() >> Cancel");
                }
            }
        });
    }

    private void onToast(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class OneQPayReviewManager {

        public static ReviewManager createReviewManager(Activity activity) {
            ReviewManager manager;
            manager = ReviewManagerFactory.create(activity);
            return manager;
        }
    }
}
