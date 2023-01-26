package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.NewGallon;

import java.util.List;

public interface INewGallonLoadListener {
    void onNewGallonLoadSuccess(List<NewGallon> newGallonList);
    void onNewGallonLoadFailed(String message);
}
