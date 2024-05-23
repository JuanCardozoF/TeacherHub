package com.example.teacherhub.util;

import android.widget.EditText;

public class Empty {
    private static final String ERROR_EMPTY_FIELD = "No puede ser vacío";

    public static boolean checkEmptyField(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError(ERROR_EMPTY_FIELD);
            return true;
        } else {
            field.setError(null);
            return false;
        }
    }
}
