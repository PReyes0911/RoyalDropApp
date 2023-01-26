package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.RiderModel;

import java.util.List;

public interface IRiderLoadListener {
    void onRiderLoadSuccess(List<RiderModel> riderModelList);
    void onRiderLoadFailed(String message);
}
