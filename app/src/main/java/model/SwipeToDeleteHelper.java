package model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import com.example.luminar.R;

public class SwipeToDeleteHelper implements View.OnTouchListener {

    private float startX;
    private float startY;
    private boolean isSwiping = false;
    private static final float SWIPE_THRESHOLD = 200f;
    private OnSwipeListener listener;

    public interface OnSwipeListener {
        void onSwipeToDelete(int position);
    }

    public SwipeToDeleteHelper(OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                isSwiping = false;
                return false;

            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - startX;
                float deltaY = Math.abs(event.getY() - startY);

                // Only swipe horizontally
                if (Math.abs(deltaX) > 50 && deltaY < 50) {
                    isSwiping = true;
                    view.setTranslationX(Math.max(deltaX, -SWIPE_THRESHOLD * 2));

                    // Show delete background
                    if (deltaX < -50) {
                        view.setBackgroundColor(Color.parseColor("#FF5252"));
                    }
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isSwiping) {
                    float finalX = event.getX() - startX;

                    if (finalX < -SWIPE_THRESHOLD) {
                        // Trigger delete
                        int position = (int) view.getTag();
                        if (listener != null) {
                            listener.onSwipeToDelete(position);
                        }
                    } else {
                        // Reset position
                        view.animate()
                                .translationX(0)
                                .setDuration(200)
                                .start();
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }
                    isSwiping = false;
                    return true;
                }
                break;
        }
        return false;
    }
}