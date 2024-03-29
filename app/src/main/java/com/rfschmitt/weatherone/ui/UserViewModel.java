/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rfschmitt.weatherone.ui;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.rfschmitt.weatherone.UserDataSource;
import com.rfschmitt.weatherone.persistence.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * View Model for the {@link MainActivity}
 */
public class UserViewModel extends ViewModel {

    private static final String TAG = UserViewModel.class.getSimpleName();
    private final UserDataSource mDataSource;

    private User mUser;

    public UserViewModel(UserDataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Get the user name of the user.
     *
     * @return a {@link Flowable} that will emit every time the user name has been updated.
     */
    public Flowable<String> getUserName() {
        return mDataSource.getUser()
                // for every emission of the user, get the user name
                .map(user -> {
                    mUser = user;
                    return user.getUserName();
                });

    }

    /**
     * Update the user name.
     *
     * @param userName the new user name
     * @return a {@link Completable} that completes when the user name is updated
     */
    public Completable updateUserName(final String userName) {
        // if there's no user, create a new user.
        // if we already have a user, then, since the user object is immutable,
        // create a new user, with the id of the previous user and the updated user name.
        Log.println(Log.INFO, TAG, "updateUserName userName="+userName);

        mUser = mUser == null
                ? new User(userName)
                : new User(mUser.getId(), userName);
        Log.println(Log.INFO, TAG,
                "updateUserName mUser.getUserName()="+mUser.getUserName()
                        +" mUser.getId()="+mUser.getId()
        );
        return mDataSource.insertOrUpdateUser(mUser);
    }

    /**
     * Update the user name.
     *
     * @return a {@link Completable} that completes when the users are deeleted
     */
    public Completable deleteAllUsers() {
        // if there's no user, create a new user.
        // if we already have a user, then, since the user object is immutable,
        // create a new user, with the id of the previous user and the updated user name.
        Log.println(Log.INFO, TAG, "deleteAllUsers()");
        return mDataSource.deleteAllUsers();

    }
}
