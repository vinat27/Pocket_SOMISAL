package com.sominfor.somisal_app.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.sominfor.somisal_app.fragments.AdresseFacturationFragment;
import com.sominfor.somisal_app.fragments.AdresseLivraisonFragment;
import com.sominfor.somisal_app.fragments.EnteteFragment;
import com.sominfor.somisal_app.fragments.ReglementFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapter extends AbstractFragmentStepAdapter {
    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";
    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }
    @Override
    public Step
    createStep(int position) {
        switch (position){
            case 0:
                final EnteteFragment step1 = new EnteteFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;
            case 1:
                final AdresseFacturationFragment step2 = new AdresseFacturationFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;
            case 2:
                final AdresseLivraisonFragment step3 = new AdresseLivraisonFragment();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;

            case 3:
                final ReglementFragment step4 = new ReglementFragment();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;
        }
        return null;
    }
    @Override
    public int
    getCount() {
        return 4;
    }
    @NonNull
    @Override
    public StepViewModel
    getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position){
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("Tabs 1") //can be a CharSequence instead
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Tabs 2") //can be a CharSequence instead
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Tabs 3") //can be a CharSequence instead
                        .create();
            case 3:
                return new StepViewModel.Builder(context)
                        .setTitle("Tabs 4") //can be a CharSequence instead
                        .create();
        }
        return null;
    }
}