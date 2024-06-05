package com.example.lovelypets.exitalertdialog;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lovelypets.R;

import java.util.Objects;

/**
 * A custom dialog for confirming the exit of the application.
 */
public class ExitDialogActivity extends Dialog {
    private static final String TAG = "ExitDialogActivity";

    /**
     * Constructor to create an ExitDialogActivity.
     *
     * @param context The context in which the dialog should run.
     */
    public ExitDialogActivity(@NonNull Context context) {
        super(context);
    }

    /**
     * Constructor to create an ExitDialogActivity with a specific theme.
     *
     * @param context    The context in which the dialog should run.
     * @param themeResId The resource ID of the theme against which to inflate this dialog.
     */
    public ExitDialogActivity(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * Constructor to create an ExitDialogActivity with a specific theme and a cancel listener.
     *
     * @param context         The context in which the dialog should run.
     * @param cancelable      Whether the dialog is cancelable or not.
     * @param cancelListener  The listener to be invoked when the dialog is canceled.
     */
    protected ExitDialogActivity(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the dialog title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exit_dialog);

        // Initialize UI elements
        TextView cancel = findViewById(R.id.cancel_text);
        TextView close = findViewById(R.id.close_text);

        // Set the cancel button click listener
        cancel.setOnClickListener(v -> {
            Log.d(TAG, "Cancel button clicked. Dismissing the dialog.");
            dismiss();
        });

        // Set the close button click listener
        close.setOnClickListener(v -> {
            Log.d(TAG, "Close button clicked. Closing the application.");
            finishAffinity(Objects.requireNonNull(getOwnerActivity()));
        });
    }
}