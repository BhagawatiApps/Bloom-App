package com.example.bloomapp.Utils;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

import com.example.bloomapp.R;
import com.google.android.material.button.MaterialButton;

public class ButtonUtils {

    /**
     * Updates the state and color of a MaterialButton based on whether it is enabled.
     *
     * @param context   The context used to access resources.
     * @param button    The MaterialButton to update.
     * @param isEnabled Whether the button should be enabled.
     */
    public static void updateButtonState(Context context, MaterialButton button, boolean isEnabled) {
        if (button == null || context == null) return;

        button.setEnabled(isEnabled);
        if (isEnabled) {
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.md_theme_primary)));
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.md_theme_inversePrimary_mediumContrast)));
        }
    }
}
