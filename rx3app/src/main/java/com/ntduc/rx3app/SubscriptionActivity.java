/*
 *
 *  *    Copyright (C) 2016 Amit Shekhar
 *  *    Copyright (C) 2011 Android Open Source Project
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.ntduc.rx3app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ntduc.rx3androidnetworking.Rx3AndroidNetworking;
import com.ntduc.rx3app.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Created by amitshekhar on 31/07/16.
 */
public class SubscriptionActivity extends AppCompatActivity {

    private static final String TAG = SubscriptionActivity.class.getSimpleName();
    private static final String URL = "http://i.imgur.com/AtbX9iX.png";
    private String dirPath;
    private String fileName = "imgurimage.png";
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        dirPath = Utils.getRootDirPath(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    public Completable getCompletable() {
        return Rx3AndroidNetworking.download(URL, dirPath, fileName)
                .build()
                .getDownloadCompletable();
    }

    private DisposableCompletableObserver getDisposableObserver() {

        return new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.d(TAG, "onError " + throwable.getMessage());
            }
        };

    }

    public void downloadFile(View view) {
        disposables.add(getCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableObserver()));
    }
}