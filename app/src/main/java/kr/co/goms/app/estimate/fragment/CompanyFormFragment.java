package kr.co.goms.app.estimate.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.manager.AdIdHelper;
import kr.co.goms.app.estimate.manager.GlideHelper;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.util.FileUtil;
import kr.co.goms.module.common.util.StringUtil;

public class CompanyFormFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CompanyFormFragment.class.getSimpleName();

    public CompanyFormFragment(){}

    private Toolbar mToolbar;

    private EditText mEtComName, mEtBizNum, mEtCeoName, mEtUpTae, mEtUpJong, mEtManagerName, mEtTel, mEtFax, mEtHp, mEtEmail, mEtZipCode, mEtAddress01, mEtAddress02, mEtAccoundNum;
    private ImageView mIvStamp, mIvLogo;
    private LottieAnimationView mLavStemp, mLavLogo;

    private DIALOG_TYPE mDialogType = DIALOG_TYPE.STAMP;
    private enum DIALOG_TYPE{
        STAMP,
        LOGO,
    }

    private Uri mPhotoStampUri, mPhotoLogoUri;


    private String mComIdx = "";

    public static CompanyFormFragment getFragment(int comIdx){
        CompanyFormFragment fragment = new CompanyFormFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("comIdx", comIdx);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_company_form, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mComIdx = getArguments().getString("comIdx");
        }catch(NullPointerException e){

        }

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView tvToolBarTitle = view.findViewById(R.id.tv_toolbar_title);
        tvToolBarTitle.setText("나의 회사 정보");

        //mComIdx = "1";
        initData(view, mComIdx);

        this.setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_save){
            if(checkValue()){
                String comName = mEtComName.getText().toString();
                String bizNum = mEtBizNum.getText().toString();
                String ceoName = mEtCeoName.getText().toString();
                String upTae = mEtUpTae.getText().toString();
                String upJong  = mEtUpJong.getText().toString();
                String managerName  = mEtManagerName.getText().toString();
                String tel = mEtTel.getText().toString();
                String fax  = mEtFax.getText().toString();
                String hp  = mEtHp.getText().toString();
                String email = mEtEmail.getText().toString();
                String zipCode = mEtZipCode.getText().toString();
                String address01 = mEtAddress01.getText().toString();
                String address02 = mEtAddress02.getText().toString();
                String accoundNum = mEtAccoundNum.getText().toString();

                CompanyBeanTB companyBeanTB = new CompanyBeanTB();
                companyBeanTB.setUser_ad_id(AdIdHelper.I().getAdID());
                companyBeanTB.setCom_name(comName);
                companyBeanTB.setCom_biz_num(bizNum);
                companyBeanTB.setCom_ceo_name(ceoName);
                companyBeanTB.setCom_uptae(upTae);
                companyBeanTB.setCom_upjong(upJong);
                companyBeanTB.setCom_manager_name(managerName);
                companyBeanTB.setCom_tel_num(tel);
                companyBeanTB.setCom_fax_num(fax);
                companyBeanTB.setCom_hp_num(hp);
                companyBeanTB.setCom_email(email);
                companyBeanTB.setCom_zipcode(zipCode);
                companyBeanTB.setCom_address_01(address01);
                companyBeanTB.setCom_address_02(address02);
                companyBeanTB.setCom_account_num(accoundNum);

                if(mPhotoStampUri != null) {
                    companyBeanTB.setCom_stamp_path(mPhotoStampUri.getPath());
                }
                if(mPhotoLogoUri != null) {
                    companyBeanTB.setCom_logo_path(mPhotoLogoUri.getPath());
                }

                MyApplication.getInstance().getDBHelper().insertCompany(companyBeanTB);

            }
        }
    }

    private void goAlbum(DIALOG_TYPE dialogType){
        mDialogType = dialogType;
        ((MainActivity)getActivity()).getAlbumLauncher().launch("image/*");
    }

    private boolean checkValue(){
        String comName = mEtComName.getText().toString();
        String bizNum = mEtBizNum.getText().toString();
        if(StringUtil.isEmpty(comName)){
            CurvletManager.process(getActivity(), null, "water://toast?text=제목을 넣어주세요");
            return false;
        }
        if(comName.length() <= 1){
            CurvletManager.process(getActivity(), null, "water://toast?text=회사명을 2자리 이상 넣어주세요");
            return false;
        }

        if(StringUtil.isEmpty(bizNum)){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호를 넣어주세요");
            return false;
        }
        if(bizNum.length() != 10){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호 10자리를 넣어주세요");
            return false;
        }
        /*
        if(!StringUtil.isBizNumValid(bizNum)){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호를 정확히 넣어주세요");
            return false;
        }
        */
        return true;
    }


    /**
     * 사진대지 > 앨범 선택 후 setAlbumPhoto 처리
     * @param photoUri
     */
    public void setAlbumPhoto(Uri photoUri){
        if(DIALOG_TYPE.STAMP == mDialogType) {
            mPhotoStampUri = photoUri;
            GlideHelper.I().setImageView(requireActivity(), photoUri,mIvStamp, 100, 100);
            mIvStamp.setVisibility(View.VISIBLE);
            mLavStemp.setVisibility(View.GONE);
            //ExcelManager.I(getActivity()).setPhotoUri(ExcelManager.PHOTO_TYPE.AROUND, mPhotoAroundUri);

            resizeAndSaveImage(mDialogType, mPhotoStampUri);
        }else if(DIALOG_TYPE.LOGO == mDialogType) {
            mPhotoLogoUri = photoUri;
            GlideHelper.I().setImageView(requireActivity(), photoUri,mIvLogo, 100, 100);
            mIvStamp.setVisibility(View.VISIBLE);
            mLavLogo.setVisibility(View.GONE);
            //ExcelManager.I(getActivity()).setPhotoUri(ExcelManager.PHOTO_TYPE.OUTER, mPhotoOuterUri);
            resizeAndSaveImage(mDialogType, mPhotoLogoUri);
        }
    }

    private void resizeAndSaveImage(DIALOG_TYPE dialogType, Uri imageUri) {
        try {
            Bitmap originalBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            int newWidth = AppConstant.PHOTO_WIDTH;
            int newHeight = AppConstant.PHOTO_HEIGHT;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);

            String prefix = AppConstant.APP_NAME + dialogType.name().toLowerCase();
            File resizedFile = FileUtil.createFile(getActivity(), prefix, Environment.DIRECTORY_PICTURES, dialogType.name().toLowerCase(), ".jpg");
            FileOutputStream outputStream = new FileOutputStream(resizedFile);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            imageUri = Uri.fromFile(resizedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initData(View view, String comIdx){

        mEtComName = view.findViewById(R.id.et_com_name);
        mEtBizNum= view.findViewById(R.id.et_biz_num);
        mEtCeoName = view.findViewById(R.id.et_ceo_name);
        mEtUpTae = view.findViewById(R.id.et_uptae);
        mEtUpJong = view.findViewById(R.id.et_upjong);
        mEtManagerName = view.findViewById(R.id.et_manager_name);
        mEtTel = view.findViewById(R.id.et_tel);
        mEtFax = view.findViewById(R.id.et_fax);
        mEtHp = view.findViewById(R.id.et_hp);
        mEtEmail = view.findViewById(R.id.et_email);
        mEtZipCode = view.findViewById(R.id.et_zipcode);
        mEtAddress01 = view.findViewById(R.id.et_address_01);
        mEtAddress02 = view.findViewById(R.id.et_address_02);
        mEtAccoundNum = view.findViewById(R.id.et_account_num);
        mIvStamp = view.findViewById(R.id.iv_stamp);
        mIvLogo = view.findViewById(R.id.iv_logo);
        mLavStemp = view.findViewById(R.id.lav_stamp);
        mLavLogo = view.findViewById(R.id.lav_logo);

        view.findViewById(R.id.btn_save).setOnClickListener(this);

        if(StringUtil.isNotNull(comIdx)) {
            CompanyBeanTB companyBeanTB = MyApplication.getInstance().getDBHelper().getCompany(comIdx);

            mEtComName.setText(companyBeanTB.getCom_name());
            mEtBizNum.setText(companyBeanTB.getCom_biz_num());
            mEtCeoName.setText(companyBeanTB.getCom_ceo_name());
            mEtUpTae.setText(companyBeanTB.getCom_uptae());
            mEtUpJong.setText(companyBeanTB.getCom_upjong());
            mEtManagerName.setText(companyBeanTB.getCom_manager_name());
            mEtTel.setText(companyBeanTB.getCom_tel_num());
            mEtFax.setText(companyBeanTB.getCom_fax_num());
            mEtHp.setText(companyBeanTB.getCom_hp_num());
            mEtEmail.setText(companyBeanTB.getCom_email());
            mEtZipCode.setText(companyBeanTB.getCom_zipcode());
            mEtAddress01.setText(companyBeanTB.getCom_address_01());
            mEtAddress02.setText(companyBeanTB.getCom_address_02());
            mEtAccoundNum.setText(companyBeanTB.getCom_account_num());
        }
    }

}