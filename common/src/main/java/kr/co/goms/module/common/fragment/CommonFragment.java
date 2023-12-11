package kr.co.goms.module.common.fragment;

import android.content.Context;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CommonFragment  extends Fragment {

    OnBackPressedCallback mOnBackPressedCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };

        getActivity().getOnBackPressedDispatcher().addCallback(this, mOnBackPressedCallback);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mOnBackPressedCallback.remove();
    }
}
