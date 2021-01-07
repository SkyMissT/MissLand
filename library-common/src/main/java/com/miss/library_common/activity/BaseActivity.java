package com.miss.library_common.activity;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {

    public void switchFragment(FragmentManager fragmentManager, Fragment fragment, Fragment oldFragment, @IdRes int containerViewId) {
        if (fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .hide(oldFragment)
                    .show(fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .hide(oldFragment)
                    .add(containerViewId, fragment)
                    .commit();
        }
    }

}
