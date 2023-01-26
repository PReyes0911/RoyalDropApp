package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.HistoryModel;

import java.util.List;

public interface IHistoryLoadListener {
    void onHistoryLoadSuccess(List<HistoryModel> historyModelList);
    void onHistoryLoadFailed(String message);
}
