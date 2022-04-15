package literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import literefresh.LiteRefresh
import literefresh.OnScrollListener
import literefresh.behavior.RefreshContentBehavior
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.databinding.FragmentVideoPlayerBinding
import literefresh.sample.ui.widget.ijkplayer.AndroidMediaController
import literefresh.sample.ui.widget.ijkplayer.IMediaController
import literefresh.sample.utils.StatusBarUtils
import kotlinx.coroutines.*
import layoutbinder.annotations.BindLayout
import literefresh.behavior.Configuration
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class VideoPlayerFragment : BaseFragment(), CoroutineScope by MainScope() {

    companion object {
        val titles = arrayOf("简介", "评论 1281")
    }

    @BindLayout(R.layout.fragment_video_player)
    lateinit var binding: FragmentVideoPlayerBinding

    lateinit var mediaController: IMediaController

    lateinit var contentBehavior: RefreshContentBehavior<View>

    var videoWidth = 0
    var videoHeight = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaController = AndroidMediaController(context)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setHudView(binding.hudView)

        binding.viewPager.adapter = VideoPlayerPagerAdapter(childFragmentManager)
        binding.pagerTabs.setupWithViewPager(binding.viewPager)

        binding.videoView.post {
            val params = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = StatusBarUtils.getStatusBarHeight(requireContext())
            binding.videoView.layoutParams = params
        }

        binding.videoView.setOnPreparedListener(IMediaPlayer.OnPreparedListener {
            videoWidth = it.videoWidth
            videoHeight = it.videoHeight
            binding.clHeader.post {
                val width = binding.clHeader.width
                val height = (((width.toFloat() / videoWidth.toFloat()) * videoHeight).toInt() * 3) shr 2
                updateHeaderHeight(height)

                val minHeight = (width * 9) shr 4
                contentBehavior.with(requireContext())
                        .minOffset(minHeight)
                        .maxOffset(height)
                        .config<RefreshContentBehavior<View>>()
            }
        })

        contentBehavior = LiteRefresh.getContentBehavior(binding.viewPager)
        contentBehavior.addOnScrollListener(object : OnScrollListener {
            override fun onStartScroll(
                parent: CoordinatorLayout?,
                view: View?,
                config: Configuration,
                type: Int
            ) {
            }

            override fun onPreScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                type: Int
            ) {
                updateHeaderHeight(current)
            }

            override fun onScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                delta: Int,
                type: Int
            ) {
                updateHeaderHeight(current)
            }

            override fun onStopScroll(
                parent: CoordinatorLayout?,
                view: View?,
                config: Configuration,
                type: Int
            ) {

            }
        })
    }

    private fun updateHeaderHeight(height : Int) {
            val params = binding.clHeader.layoutParams as CoordinatorLayout.LayoutParams
            val width = binding.clHeader.width
            params.height = height
            binding.clHeader.layoutParams = params
    }

    suspend fun copyVideoFile() : File {
        val outFile = File(context?.filesDir, "ontheway.mp4")
        withContext(Dispatchers.IO) {
            val input = BufferedInputStream(resources.openRawResource(R.raw.ontheway))
            var output = BufferedOutputStream(FileOutputStream(outFile))
            input.use { input ->
                output.use {
                    val buf = ByteArray(4096)
                    var len = 0
                    do {
                        len = input.read(buf)
                        if (len < 0)
                            break
                        it.write(buf, 0, len)
                    } while (true)
                }
            }
        }
        return outFile
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            binding.videoView.pause()
        } else {
            binding.videoView.start()
        }
    }

    override fun onStart() {
        super.onStart()
        launch {
            val file = copyVideoFile()
            binding.videoView.setVideoPath(file.path)
            binding.videoView.start()
        }
    }

    override fun onStop() {
        super.onStop()
        binding.videoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.stopPlayback()
        binding.videoView.release(true)
        binding.videoView.stopBackgroundPlay()
    }

    class VideoPlayerPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> VideoInfoFragment()
                else -> VideoCommentFragment()
            }
        }

        override fun getCount(): Int {
            return titles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}