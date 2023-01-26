package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.PersonalOrderModel;

import java.util.List;

public interface IPersonalOrderLoadListener {
    void onPersonalOrderLoadSuccess(List<PersonalOrderModel> personalOrderModelList);
    void onPersonalOrderLoadFailed(String message);
}
