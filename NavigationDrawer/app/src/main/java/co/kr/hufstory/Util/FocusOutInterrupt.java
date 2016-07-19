package co.kr.hufstory.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Hyeong Wook on 2016-06-19.
 */
abstract public class FocusOutInterrupt {
    static public void editText(EditText v, Activity activity){
        v.clearFocus();

        InputMethodManager imm= (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    static public View.OnClickListener getEditTextFocusOutClickListener(EditText v, Activity activity){
        return new EditTextFocusOutClickListener(v, activity);
    }

    static private class EditTextFocusOutClickListener implements View.OnClickListener{
        static private EditText editText;
        static private Activity activity;

        public EditTextFocusOutClickListener(EditText editText, Activity activity){
            this.editText = editText;
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            editText.clearFocus();

            InputMethodManager imm= (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
