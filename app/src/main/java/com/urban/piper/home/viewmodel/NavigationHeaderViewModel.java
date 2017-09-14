package com.urban.piper.home.viewmodel;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.urban.piper.app.Constants;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.manager.SessionManager;
import com.urban.piper.utility.LogUtility;

import java.io.File;
import java.io.IOException;

public class NavigationHeaderViewModel extends BaseViewModel {
    public ObservableField<Bitmap> profileImgBitmap;
    private static final String TAG = NavigationHeaderViewModel.class.getSimpleName();

    public NavigationHeaderViewModel() {
        this.profileImgBitmap = new ObservableField<>(null);
        setImageBackground();
    }

    private void setImageBackground() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(Constants.FIREBASE_CLOUD_URL).child(Constants.BACKGROUND_IMAGE);
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    setProfileImgBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    LogUtility.d(TAG, exception.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProfileImgBitmap(Bitmap profileImgBitmap) {
        this.profileImgBitmap.set(profileImgBitmap);
    }

    public String getName() {
        return SessionManager.getUserName();
    }

    public String getEmailId() {
        return SessionManager.getEmail();
    }

    public String getProfileImgUrl() {
        return SessionManager.getProfileImageUrl();
    }

    public void refresh() {
        notifyChange();
    }
}
