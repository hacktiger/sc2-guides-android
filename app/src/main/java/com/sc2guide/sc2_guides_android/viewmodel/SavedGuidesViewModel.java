package com.sc2guide.sc2_guides_android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.repo.SavedGuidesRepository;

import java.util.List;

public class SavedGuidesViewModel extends ViewModel {
    private SavedGuidesRepository savedGuidesRepository;
    private LiveData<List<Guide>> guides;



    public SavedGuidesViewModel() {
    }

    public LiveData<List<Guide>> getGuides(Context context) {
        if (this.savedGuidesRepository == null) {
            // guides = new MutableLiveData<>();
            savedGuidesRepository = new SavedGuidesRepository(context);
            guides = savedGuidesRepository.getAllGuides();
        }
        return guides;
    }
}
