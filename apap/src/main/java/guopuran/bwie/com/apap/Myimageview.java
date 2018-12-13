package guopuran.bwie.com.apap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class Myimageview extends ImageView {
    public Myimageview(Context context) {
        super(context);
    }

    public Myimageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Myimageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
