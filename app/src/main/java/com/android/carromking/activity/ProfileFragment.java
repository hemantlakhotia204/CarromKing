package com.android.carromking.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.carromking.ApiService;
import com.android.carromking.MyApiEndpointInterface;
import com.android.carromking.R;
import com.android.carromking.models.profile.ProfileResponseDataModel;
import com.android.carromking.models.profile.ProfileResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    final String TAG = getString(R.string.TAG);

    SharedPreferences sp;
    ProfileResponseDataModel dataModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = view.getContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);

        getProfileData();
        if(dataModel!=null) {
            ///Connect UI Here
        } else {
            //Error Handling
        }
    }

    void getProfileData() {
        ApiService apiService = new ApiService();
        MyApiEndpointInterface apiEndpointInterface = apiService.getApiServiceForInterceptor(apiService.getInterceptor(sp.getString("token", null)));

        apiEndpointInterface.getProfileData()
                .enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileResponseModel> call, @NonNull Response<ProfileResponseModel> response) {
                        if(response.body()!=null) {
                            if(response.body().isStatus()) {
                                dataModel = response.body().getData();
                                Log.d(TAG, "onResponse: Profile " + dataModel.getUserData().getUsername());

                                /*
                                {dataModel.getUserData().getMobileNumber()}
                                and
                                {dataModel.getUserData().getToken()}
                                is null here
                                Don't use it for this api
                                 */

                            } else {
                                Toast.makeText(getContext(), response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileResponseModel> call, @NonNull Throwable t) {

                    }
                });
    }
}
