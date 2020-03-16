package literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.databinding.FragmentVideoCommentBinding
import layoutbinder.annotations.BindLayout

class VideoCommentFragment : BaseFragment()  {

    companion object {
        fun newInstance(): VideoCommentFragment {
            val args = Bundle()
            val fragment = VideoCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @BindLayout(R.layout.fragment_video_comment)
    lateinit var binding: FragmentVideoCommentBinding
    lateinit var adapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerAdapter()
        adapter.attachToRecyclerView(binding.recyclerView)
    }


}