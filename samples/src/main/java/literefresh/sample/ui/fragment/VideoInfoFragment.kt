package literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.BaseViewHolder
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.base.ui.ViewBinder
import literefresh.sample.databinding.FragmentVideoInfoBinding
import literefresh.sample.databinding.VhVideoInfoHeaderBinding
import literefresh.sample.databinding.VhVideoInfoRecommendBinding
import layoutbinder.annotations.BindLayout

class VideoInfoFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_video_info)
    lateinit var binding: FragmentVideoInfoBinding

    lateinit var adapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerAdapter()
        adapter.attachToRecyclerView(binding.recyclerView)
        adapter.register(VideoInfoHeaderViewHolder::class.java,
                VideoInfoRecommendViewHolder::class.java)
        adapter.addPayload(VideoInfoHeader())
        adapter.addPayload(VideoRecommend())
        adapter.addPayload(VideoRecommend())
    }
}

@ViewBinder(R.layout.vh_video_info_header, dataTypes = [VideoInfoHeader::class])
class VideoInfoHeaderViewHolder(itemView: View) : BaseViewHolder<VhVideoInfoHeaderBinding>(itemView) {

    override fun <T> onBind(data: T, position: Int) {
    }
}

@ViewBinder(R.layout.vh_video_info_recommend, dataTypes = [VideoRecommend::class])
class VideoInfoRecommendViewHolder(itemView: View) : BaseViewHolder<VhVideoInfoRecommendBinding>(itemView) {

    override fun <T> onBind(data: T, position: Int) {

    }
}

class VideoInfoHeader {

}

class VideoRecommend {

}

