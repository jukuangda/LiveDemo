package tv.newtv.demo.livedemo.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DividerDecoration(private var space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                    outRect.bottom = 0
                } else {
                    outRect.bottom = space
                }
            } else {
                if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                    outRect.right = 0
                } else {
                    outRect.right = space
                }
            }
        }
    }
}
