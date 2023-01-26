package com.example.royaldropapp.Listener;

import com.example.royaldropapp.Model.ProductModel;

import java.util.List;

public interface IProductLoadListener {
    void onProductLoadSuccess(List<ProductModel> productModelList);
    void onProductLoadFailed(String message);
}
