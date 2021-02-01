package com.sominfor.somisal_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.AddProduitCommandeActivity;
import com.sominfor.somisal_app.activities.AddProduitDevisActivity;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * Créé par vatsou le 01,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class ReglementFragment extends Fragment implements BlockingStep {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reglement, container, false);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(() -> callback.goToNextStep(), 500L);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Intent i = new Intent(this.getActivity(), AddProduitCommandeActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        Toast.makeText(this.getContext(), "Your custom back action. Here you should cancel currently running operations", Toast.LENGTH_SHORT).show();
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}

