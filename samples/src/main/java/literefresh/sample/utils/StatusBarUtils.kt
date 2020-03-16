package literefresh.sample.utils

import android.content.Context
import android.util.TypedValue

object StatusBarUtils {
    fun getStatusBarHeight(context: Context): Int {
        var result = Math.round(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 24f,
                        context.resources.displayMetrics))
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private const val VERSION_7 = 7
}