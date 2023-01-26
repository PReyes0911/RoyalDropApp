package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.DeliverModel;

import java.util.List;

public interface IDeliverLoadListener {
    void onDeliverLoadSuccess(List<DeliverModel> deliverModelList);
    void onDeliverLoadFailed(String message);
}
