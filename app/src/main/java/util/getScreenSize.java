package util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by psj on 2016/5/19.
 */
class GetScreenSize {

    public static int getwidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }
}
