/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package literefresh.sample.ui.widget.ijkplayer

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.MediaController.MediaPlayerControl
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import literefresh.sample.R
import literefresh.sample.service.MediaPlayerService.Companion.intentToStart
import literefresh.sample.service.MediaPlayerService.Companion.mediaPlayer
import literefresh.sample.ui.widget.ijkplayer.IRenderView.IRenderCallback
import literefresh.sample.ui.widget.ijkplayer.IRenderView.ISurfaceHolder
import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer
import tv.danmaku.ijk.media.player.AndroidMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.TextureMediaPlayer
import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import tv.danmaku.ijk.media.player.misc.ITrackInfo
import tv.danmaku.ijk.media.player.misc.IjkMediaFormat
import java.io.File
import java.io.IOException
import java.util.*

class IjkVideoView : FrameLayout, MediaPlayerControl {
    private val TAG = "IjkVideoView"

    // settable by the client
    private var mUri: Uri? = null
    private var mHeaders: Map<String, String>? = null

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private var mCurrentState = STATE_IDLE
    private var mTargetState = STATE_IDLE

    // All the stuff we need for playing and showing a video
    private var mSurfaceHolder: ISurfaceHolder? = null
    private var mMediaPlayer: IMediaPlayer? = null

    // private int         mAudioSession;
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0
    private var mVideoRotationDegree = 0
    private var mMediaController: IMediaController? = null
    private var mOnCompletionListener: IMediaPlayer.OnCompletionListener? = null
    private var mOnPreparedListener: IMediaPlayer.OnPreparedListener? = null
    private var mCurrentBufferPercentage = 0
    private var mOnErrorListener: IMediaPlayer.OnErrorListener? = null
    private var mOnInfoListener: IMediaPlayer.OnInfoListener? = null
    private var mSeekWhenPrepared = 0 // recording the seek position while preparing = 0
    private val mCanPause = true
    private val mCanSeekBack = true
    private val mCanSeekForward = true
    /** Subtitle rendering widget overlaid on top of the video.  */ // private RenderingWidget mSubtitleWidget;
    /**
     * Listener for changes to subtitle data, used to redraw when needed.
     */
    // private RenderingWidget.OnChangedListener mSubtitlesChangedListener;
    lateinit var mAppContext: Context
    lateinit var mSettings: Settings
    private var mRenderView: IRenderView? = null
    private var mVideoSarNum = 0
    private var mVideoSarDen = 0
    private var mHudViewHolder: InfoHudViewHolder? = null
    private var mPrepareStartTime: Long = 0
    private var mPrepareEndTime: Long = 0
    private var mSeekStartTime: Long = 0
    private var mSeekEndTime: Long = 0
    private var subtitleDisplay: TextView? = null

    constructor(context: Context) : super(context) {
        initVideoView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initVideoView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initVideoView(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initVideoView(context)
    }

    // REMOVED: onMeasure
    // REMOVED: onInitializeAccessibilityEvent
    // REMOVED: onInitializeAccessibilityNodeInfo
    // REMOVED: resolveAdjustedSize
    private fun initVideoView(context: Context) {
        mAppContext = context.applicationContext
        mSettings = Settings(mAppContext)
        initBackground()
        initRenders()
        mVideoWidth = 0
        mVideoHeight = 0
        // REMOVED: getHolder().addCallback(mSHCallback);
        // REMOVED: getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        // REMOVED: mPendingSubtitleTracks = new Vector<Pair<InputStream, MediaFormat>>();
        mCurrentState = STATE_IDLE
        mTargetState = STATE_IDLE
        subtitleDisplay = TextView(context)
        subtitleDisplay!!.textSize = 24f
        subtitleDisplay!!.gravity = Gravity.CENTER
        val layoutParams_txt = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM)
        addView(subtitleDisplay, layoutParams_txt)
    }

    fun setRenderView(renderView: IRenderView?) {
        if (mRenderView != null) {
            if (mMediaPlayer != null) mMediaPlayer!!.setDisplay(null)
            val renderUIView = mRenderView!!.getView()
            mRenderView!!.removeRenderCallback(mSHCallback)
            mRenderView = null
            removeView(renderUIView)
        }
        if (renderView == null) return
        mRenderView = renderView
        renderView.setAspectRatio(mCurrentAspectRatio)
        if (mVideoWidth > 0 && mVideoHeight > 0) renderView.setVideoSize(mVideoWidth, mVideoHeight)
        if (mVideoSarNum > 0 && mVideoSarDen > 0) renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
        val renderUIView = mRenderView!!.getView()
        val lp = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER)
        renderUIView!!.layoutParams = lp
        addView(renderUIView)
        mRenderView!!.addRenderCallback(mSHCallback)
        mRenderView!!.setVideoRotation(mVideoRotationDegree)
    }

    fun setRender(render: Int) {
        when (render) {
            RENDER_NONE -> setRenderView(null)
            RENDER_TEXTURE_VIEW -> {
                val renderView = TextureRenderView(context)
                if (mMediaPlayer != null) {
                    renderView.surfaceHolder.bindToMediaPlayer(mMediaPlayer)
                    renderView.setVideoSize(mMediaPlayer!!.videoWidth, mMediaPlayer!!.videoHeight)
                    renderView.setVideoSampleAspectRatio(mMediaPlayer!!.videoSarNum, mMediaPlayer!!.videoSarDen)
                    renderView.setAspectRatio(mCurrentAspectRatio)
                }
                setRenderView(renderView)
            }
            RENDER_SURFACE_VIEW -> {
                val renderView = SurfaceRenderView(context)
                setRenderView(renderView)
            }
            else -> Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render))
        }
    }

    fun setHudView(tableLayout: TableLayout) {
        mHudViewHolder = InfoHudViewHolder(context, tableLayout)
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    fun setVideoPath(path: String?) {
        setVideoURI(Uri.parse(path))
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    fun setVideoURI(uri: Uri) {
        setVideoURI(uri, null)
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     * Note that the cross domain redirection is allowed by default, but that can be
     * changed with key/value pairs through the headers parameter with
     * "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     * to disallow or allow cross domain redirection.
     */
    private fun setVideoURI(uri: Uri, headers: Map<String, String>?) {
        mUri = uri
        mHeaders = headers
        mSeekWhenPrepared = 0
        openVideo()
        requestLayout()
        invalidate()
    }

    // REMOVED: addSubtitleSource
    // REMOVED: mPendingSubtitleTracks
    fun stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            if (mHudViewHolder != null) mHudViewHolder?.setMediaPlayer(null)
            mCurrentState = STATE_IDLE
            mTargetState = STATE_IDLE
            val am = mAppContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.abandonAudioFocus(null)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false)
        val am = mAppContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        try {
            mMediaPlayer = createPlayer(mSettings.player)

            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            val context = context
            // REMOVED: SubtitleController

            // REMOVED: mAudioSession
            mMediaPlayer!!.setOnPreparedListener(mPreparedListener)
            mMediaPlayer!!.setOnVideoSizeChangedListener(mSizeChangedListener)
            mMediaPlayer!!.setOnCompletionListener(mCompletionListener)
            mMediaPlayer!!.setOnErrorListener(mErrorListener)
            mMediaPlayer!!.setOnInfoListener(mInfoListener)
            mMediaPlayer!!.setOnBufferingUpdateListener(mBufferingUpdateListener)
            mMediaPlayer!!.setOnSeekCompleteListener(mSeekCompleteListener)
            mMediaPlayer!!.setOnTimedTextListener(mOnTimedTextListener)
            mCurrentBufferPercentage = 0
            val scheme = mUri!!.scheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    mSettings.usingMediaDataSource &&
                    (TextUtils.isEmpty(scheme) || scheme.equals("file", ignoreCase = true))) {
                val dataSource: IMediaDataSource = FileMediaDataSource(File(mUri.toString()))
                mMediaPlayer!!.setDataSource(dataSource)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer!!.setDataSource(mAppContext, mUri, mHeaders)
            } else {
                mMediaPlayer!!.dataSource = mUri.toString()
            }
            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder)
            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer!!.setScreenOnWhilePlaying(true)
            mPrepareStartTime = System.currentTimeMillis()
            mMediaPlayer!!.prepareAsync()
            if (mHudViewHolder != null) mHudViewHolder?.setMediaPlayer(mMediaPlayer)

            // REMOVED: mPendingSubtitleTracks

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING
            attachMediaController()
        } catch (ex: IOException) {
            Log.w(TAG, "Unable to open content: $mUri", ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
        } catch (ex: IllegalArgumentException) {
            Log.w(TAG, "Unable to open content: $mUri", ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }

    fun setMediaController(controller: IMediaController?) {
        if (mMediaController != null) {
            mMediaController!!.hide()
        }
        mMediaController = controller
        attachMediaController()
    }

    private fun attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController!!.setMediaPlayer(this)
            val anchorView = if (this.parent is View) this.parent as View else this
            mMediaController!!.setAnchorView(anchorView)
            mMediaController!!.setEnabled(isInPlaybackState)
        }
    }

    var mSizeChangedListener = IMediaPlayer.OnVideoSizeChangedListener { mp, width, height, sarNum, sarDen ->
        mVideoWidth = mp.videoWidth
        mVideoHeight = mp.videoHeight
        mVideoSarNum = mp.videoSarNum
        mVideoSarDen = mp.videoSarDen
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            if (mRenderView != null) {
                mRenderView!!.setVideoSize(mVideoWidth, mVideoHeight)
                mRenderView!!.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
            }
            // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            requestLayout()
        }
    }
    var mPreparedListener = IMediaPlayer.OnPreparedListener { mp ->
        mPrepareEndTime = System.currentTimeMillis()
        mHudViewHolder?.updateLoadCost(mPrepareEndTime - mPrepareStartTime)
        mCurrentState = STATE_PREPARED

        // Get the capabilities of the player for this stream
        // REMOVED: Metadata
        if (mOnPreparedListener != null) {
            mOnPreparedListener!!.onPrepared(mMediaPlayer)
        }
        if (mMediaController != null) {
            mMediaController!!.setEnabled(true)
        }
        mVideoWidth = mp.videoWidth
        mVideoHeight = mp.videoHeight
        val seekToPosition = mSeekWhenPrepared // mSeekWhenPrepared may be changed after seekTo() call
        if (seekToPosition != 0) {
            seekTo(seekToPosition)
        }
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
            // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            if (mRenderView != null) {
                mRenderView!!.setVideoSize(mVideoWidth, mVideoHeight)
                mRenderView!!.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                if (!mRenderView!!.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                    // We didn't actually change the size (it was already at the size
                    // we need), so we won't get a "surface changed" callback, so
                    // start the video here instead of in the callback.
                    if (mTargetState == STATE_PLAYING) {
                        start()
                        if (mMediaController != null) {
                            mMediaController!!.show()
                        }
                    } else if (!isPlaying &&
                            (seekToPosition != 0 || currentPosition > 0)) {
                        if (mMediaController != null) {
                            // Show the media controls when we're paused into a video and make 'em stick.
                            mMediaController!!.show(0)
                        }
                    }
                }
            }
        } else {
            // We don't know the video size yet, but should start anyway.
            // The video size might be reported to us later.
            if (mTargetState == STATE_PLAYING) {
                start()
            }
        }
    }
    private val mCompletionListener = IMediaPlayer.OnCompletionListener {
        mCurrentState = STATE_PLAYBACK_COMPLETED
        mTargetState = STATE_PLAYBACK_COMPLETED
        if (mMediaController != null) {
            mMediaController!!.hide()
        }
        if (mOnCompletionListener != null) {
            mOnCompletionListener!!.onCompletion(mMediaPlayer)
        }
    }
    private val mInfoListener = IMediaPlayer.OnInfoListener { mp, arg1, arg2 ->
        if (mOnInfoListener != null) {
            mOnInfoListener!!.onInfo(mp, arg1, arg2)
        }
        when (arg1) {
            IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:")
            IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:")
            IMediaPlayer.MEDIA_INFO_BUFFERING_START -> Log.d(TAG, "MEDIA_INFO_BUFFERING_START:")
            IMediaPlayer.MEDIA_INFO_BUFFERING_END -> Log.d(TAG, "MEDIA_INFO_BUFFERING_END:")
            IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH -> Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: $arg2")
            IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:")
            IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE -> Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:")
            IMediaPlayer.MEDIA_INFO_METADATA_UPDATE -> Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:")
            IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE -> Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:")
            IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT -> Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:")
            IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> {
                mVideoRotationDegree = arg2
                Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: $arg2")
                if (mRenderView != null) mRenderView!!.setVideoRotation(arg2)
            }
            IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START -> Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:")
        }
        true
    }
    private val mErrorListener = IMediaPlayer.OnErrorListener { mp, framework_err, impl_err ->
        Log.d(TAG, "Error: $framework_err,$impl_err")
        mCurrentState = STATE_ERROR
        mTargetState = STATE_ERROR
        if (mMediaController != null) {
            mMediaController!!.hide()
        }

        /* If an error handler has been supplied, use it and finish. */if (mOnErrorListener != null) {
        if (mOnErrorListener!!.onError(mMediaPlayer, framework_err, impl_err)) {
            return@OnErrorListener true
        }
    }

        /* Otherwise, pop up an error dialog so the user knows that
                 * something bad has happened. Only try and pop up the dialog
                 * if we're attached to a window. When we're going away and no
                 * longer have a window, don't bother showing the user an error.
                 */if (windowToken != null) {
        val r = mAppContext!!.resources
        val messageId: Int
        messageId = if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
            R.string.VideoView_error_text_invalid_progressive_playback
        } else {
            R.string.VideoView_error_text_unknown
        }
        AlertDialog.Builder(context)
                .setMessage(messageId)
                .setPositiveButton(R.string.VideoView_error_button
                ) { dialog, whichButton -> /* If we get here, there is no onError listener, so
                                             * at least inform them that the video is over.
                                             */
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener!!.onCompletion(mMediaPlayer)
                    }
                }
                .setCancelable(false)
                .show()
    }
        true
    }
    private val mBufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { mp, percent -> mCurrentBufferPercentage = percent }
    private val mSeekCompleteListener = IMediaPlayer.OnSeekCompleteListener {
        mSeekEndTime = System.currentTimeMillis()
        mHudViewHolder?.updateSeekCost(mSeekEndTime - mSeekStartTime)
    }
    private val mOnTimedTextListener = IMediaPlayer.OnTimedTextListener { mp, text ->
        if (text != null) {
            subtitleDisplay!!.text = text.text
        }
    }

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    fun setOnPreparedListener(l: IMediaPlayer.OnPreparedListener?) {
        mOnPreparedListener = l
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    fun setOnCompletionListener(l: IMediaPlayer.OnCompletionListener?) {
        mOnCompletionListener = l
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    fun setOnErrorListener(l: IMediaPlayer.OnErrorListener?) {
        mOnErrorListener = l
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    fun setOnInfoListener(l: IMediaPlayer.OnInfoListener?) {
        mOnInfoListener = l
    }

    // REMOVED: mSHCallback
    private fun bindSurfaceHolder(mp: IMediaPlayer?, holder: ISurfaceHolder?) {
        if (mp == null) return
        if (holder == null) {
            mp.setDisplay(null)
            return
        }
        holder.bindToMediaPlayer(mp)
    }

    var mSHCallback: IRenderCallback = object : IRenderCallback {
        override fun onSurfaceChanged(holder: ISurfaceHolder, format: Int, w: Int, h: Int) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceChanged: unmatched render callback\n")
                return
            }
            mSurfaceWidth = w
            mSurfaceHeight = h
            val isValidState = mTargetState == STATE_PLAYING
            val hasValidSize = !mRenderView!!.shouldWaitForResize() || mVideoWidth == w && mVideoHeight == h
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared)
                }
                start()
            }
        }

        override fun onSurfaceCreated(holder: ISurfaceHolder, width: Int, height: Int) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
                return
            }
            mSurfaceHolder = holder
            if (mMediaPlayer != null) bindSurfaceHolder(mMediaPlayer, holder) else openVideo()
        }

        override fun onSurfaceDestroyed(holder: ISurfaceHolder) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceDestroyed: unmatched render callback\n")
                return
            }

            // after we return from this we can't use the surface any more
            mSurfaceHolder = null
            // REMOVED: if (mMediaController != null) mMediaController.hide();
            // REMOVED: release(true);
            releaseWithoutStop()
        }
    }

    fun releaseWithoutStop() {
        if (mMediaPlayer != null) mMediaPlayer!!.setDisplay(null)
    }

    /*
     * release the media player in any state
     */
    fun release(cleartargetstate: Boolean) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE
            if (cleartargetstate) {
                mTargetState = STATE_IDLE
            }
            val am = mAppContext!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.abandonAudioFocus(null)
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (isInPlaybackState && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }

    override fun onTrackballEvent(ev: MotionEvent): Boolean {
        if (isInPlaybackState && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_VOLUME_MUTE && keyCode != KeyEvent.KEYCODE_MENU && keyCode != KeyEvent.KEYCODE_CALL && keyCode != KeyEvent.KEYCODE_ENDCALL
        if (isInPlaybackState && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                } else {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer!!.isPlaying) {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                }
                return true
            } else {
                toggleMediaControlsVisiblity()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun toggleMediaControlsVisiblity() {
        if (mMediaController!!.isShowing()) {
            mMediaController!!.hide()
        } else {
            mMediaController!!.show()
        }
    }

    override fun start() {
        if (isInPlaybackState) {
            mMediaPlayer!!.start()
            mCurrentState = STATE_PLAYING
        }
        mTargetState = STATE_PLAYING
    }

    override fun pause() {
        if (isInPlaybackState) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.pause()
                mCurrentState = STATE_PAUSED
            }
        }
        mTargetState = STATE_PAUSED
    }

    fun suspend() {
        release(false)
    }

    fun resume() {
        openVideo()
    }

    override fun getDuration(): Int {
        return if (isInPlaybackState) {
            mMediaPlayer!!.duration.toInt()
        } else -1
    }

    override fun getCurrentPosition(): Int {
        return if (isInPlaybackState) {
            mMediaPlayer!!.currentPosition.toInt()
        } else 0
    }

    override fun seekTo(msec: Int) {
        if (isInPlaybackState) {
            mSeekStartTime = System.currentTimeMillis()
            mMediaPlayer!!.seekTo(msec.toLong())
            mSeekWhenPrepared = 0
        } else {
            mSeekWhenPrepared = msec
        }
    }

    override fun isPlaying(): Boolean {
        return isInPlaybackState && mMediaPlayer!!.isPlaying
    }

    override fun getBufferPercentage(): Int {
        return if (mMediaPlayer != null) {
            mCurrentBufferPercentage
        } else 0
    }

    private val isInPlaybackState: Boolean
        private get() = mMediaPlayer != null && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING

    override fun canPause(): Boolean {
        return mCanPause
    }

    override fun canSeekBackward(): Boolean {
        return mCanSeekBack
    }

    override fun canSeekForward(): Boolean {
        return mCanSeekForward
    }

    override fun getAudioSessionId(): Int {
        return 0
    }

    private var mCurrentAspectRatioIndex = 0
    private var mCurrentAspectRatio = s_allAspectRatio[0]
    fun toggleAspectRatio(): Int {
        mCurrentAspectRatioIndex++
        mCurrentAspectRatioIndex %= s_allAspectRatio.size
        mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex]
        if (mRenderView != null) mRenderView!!.setAspectRatio(mCurrentAspectRatio)
        return mCurrentAspectRatio
    }

    private val mAllRenders: MutableList<Int> = ArrayList()
    private var mCurrentRenderIndex = 0
    private var mCurrentRender = RENDER_NONE
    private fun initRenders() {
        mAllRenders.clear()
        if (mSettings.enableSurfaceView) mAllRenders.add(RENDER_SURFACE_VIEW)
        if (mSettings.enableTextureView && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) mAllRenders.add(RENDER_TEXTURE_VIEW)
        if (mSettings.enableNoView) mAllRenders.add(RENDER_NONE)
        if (mAllRenders.isEmpty()) mAllRenders.add(RENDER_SURFACE_VIEW)
        mCurrentRender = mAllRenders[mCurrentRenderIndex]
        setRender(mCurrentRender)
    }

    fun toggleRender(): Int {
        mCurrentRenderIndex++
        mCurrentRenderIndex %= mAllRenders.size
        mCurrentRender = mAllRenders[mCurrentRenderIndex]
        setRender(mCurrentRender)
        return mCurrentRender
    }

    //-------------------------
    // Extend: Player
    //-------------------------
    fun togglePlayer(): Int {
        if (mMediaPlayer != null) mMediaPlayer!!.release()
        if (mRenderView != null) mRenderView!!.getView()!!.invalidate()
        openVideo()
        return mSettings.player
    }

    fun createPlayer(playerType: Int): IMediaPlayer? {
        var mediaPlayer: IMediaPlayer? = null
        when (playerType) {
            Settings.Companion.PV_PLAYER__IjkExoMediaPlayer -> {
                val IjkExoMediaPlayer = IjkExoMediaPlayer(mAppContext)
                mediaPlayer = IjkExoMediaPlayer
            }
            Settings.Companion.PV_PLAYER__AndroidMediaPlayer -> {
                val androidMediaPlayer = AndroidMediaPlayer()
                mediaPlayer = androidMediaPlayer
            }
            Settings.Companion.PV_PLAYER__IjkMediaPlayer -> {
                var ijkMediaPlayer: IjkMediaPlayer? = null
                if (mUri != null) {
                    ijkMediaPlayer = IjkMediaPlayer()
                    IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                    if (mSettings.usingMediaCodec) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
                        if (mSettings.usingMediaCodecAutoRotate) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0)
                        }
                        if (mSettings.mediaCodecHandleResolutionChange) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0)
                        }
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                    }
                    if (mSettings.usingOpenSLES) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                    }
                    val pixelFormat = mSettings.pixelFormat
                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat)
                    }
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
                }
                mediaPlayer = ijkMediaPlayer
            }
            else -> {
                var ijkMediaPlayer: IjkMediaPlayer? = null
                if (mUri != null) {
                    ijkMediaPlayer = IjkMediaPlayer()
                    IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                    if (mSettings.usingMediaCodec) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
                        if (mSettings.usingMediaCodecAutoRotate) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0)
                        }
                        if (mSettings.mediaCodecHandleResolutionChange) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0)
                        }
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                    }
                    if (mSettings.usingOpenSLES) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                    }
                    val pixelFormat = mSettings.pixelFormat
                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat)
                    }
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
                }
                mediaPlayer = ijkMediaPlayer
            }
        }
        if (mSettings.enableDetachedSurfaceTextureView) {
            mediaPlayer = TextureMediaPlayer(mediaPlayer)
        }
        return mediaPlayer
    }

    //-------------------------
    // Extend: Background
    //-------------------------
    var isBackgroundPlayEnabled = false
        private set

    private fun initBackground() {
        isBackgroundPlayEnabled = mSettings.enableBackgroundPlay
        if (isBackgroundPlayEnabled) {
            intentToStart(context)
            mMediaPlayer = mediaPlayer
            if (mHudViewHolder != null) mHudViewHolder?.setMediaPlayer(mMediaPlayer)
        }
    }

    fun enterBackground() {
        mediaPlayer = mMediaPlayer
    }

    fun stopBackgroundPlay() {
        mediaPlayer = null
    }

    //-------------------------
    // Extend: Background
    //-------------------------
    fun showMediaInfo() {
        if (mMediaPlayer == null) return
        val selectedVideoTrack = MediaPlayerCompat.getSelectedTrack(mMediaPlayer, ITrackInfo.MEDIA_TRACK_TYPE_VIDEO)
        val selectedAudioTrack = MediaPlayerCompat.getSelectedTrack(mMediaPlayer, ITrackInfo.MEDIA_TRACK_TYPE_AUDIO)
        val selectedSubtitleTrack = MediaPlayerCompat.getSelectedTrack(mMediaPlayer, ITrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT)
        val builder = TableLayoutBinder(context)
        builder.appendSection(R.string.mi_player)
        builder.appendRow2(R.string.mi_player, MediaPlayerCompat.getName(mMediaPlayer))
        builder.appendSection(R.string.mi_media)
        builder.appendRow2(R.string.mi_resolution, buildResolution(mVideoWidth, mVideoHeight, mVideoSarNum, mVideoSarDen))
        builder.appendRow2(R.string.mi_length, buildTimeMilli(mMediaPlayer!!.duration))
        val trackInfos = mMediaPlayer!!.trackInfo
        if (trackInfos != null) {
            var index = -1
            for (trackInfo in trackInfos) {
                index++
                val trackType = trackInfo.trackType
                if (index == selectedVideoTrack) {
                    builder.appendSection(context.getString(R.string.mi_stream_fmt1, index) + " " + context.getString(R.string.mi__selected_video_track))
                } else if (index == selectedAudioTrack) {
                    builder.appendSection(context.getString(R.string.mi_stream_fmt1, index) + " " + context.getString(R.string.mi__selected_audio_track))
                } else if (index == selectedSubtitleTrack) {
                    builder.appendSection(context.getString(R.string.mi_stream_fmt1, index) + " " + context.getString(R.string.mi__selected_subtitle_track))
                } else {
                    builder.appendSection(context.getString(R.string.mi_stream_fmt1, index))
                }
                builder.appendRow2(R.string.mi_type, buildTrackType(trackType))
                builder.appendRow2(R.string.mi_language, buildLanguage(trackInfo.language))
                val mediaFormat = trackInfo.format
                if (mediaFormat == null) {
                } else if (mediaFormat is IjkMediaFormat) {
                    when (trackType) {
                        ITrackInfo.MEDIA_TRACK_TYPE_VIDEO -> {
                            builder.appendRow2(R.string.mi_codec, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI))
                            builder.appendRow2(R.string.mi_profile_level, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI))
                            builder.appendRow2(R.string.mi_pixel_format, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PIXEL_FORMAT_UI))
                            builder.appendRow2(R.string.mi_resolution, mediaFormat.getString(IjkMediaFormat.KEY_IJK_RESOLUTION_UI))
                            builder.appendRow2(R.string.mi_frame_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_FRAME_RATE_UI))
                            builder.appendRow2(R.string.mi_bit_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI))
                        }
                        ITrackInfo.MEDIA_TRACK_TYPE_AUDIO -> {
                            builder.appendRow2(R.string.mi_codec, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_LONG_NAME_UI))
                            builder.appendRow2(R.string.mi_profile_level, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CODEC_PROFILE_LEVEL_UI))
                            builder.appendRow2(R.string.mi_sample_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_SAMPLE_RATE_UI))
                            builder.appendRow2(R.string.mi_channels, mediaFormat.getString(IjkMediaFormat.KEY_IJK_CHANNEL_UI))
                            builder.appendRow2(R.string.mi_bit_rate, mediaFormat.getString(IjkMediaFormat.KEY_IJK_BIT_RATE_UI))
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        val adBuilder = builder.buildAlertDialogBuilder()
        adBuilder!!.setTitle(R.string.media_information)
        adBuilder.setNegativeButton(R.string.close, null)
        adBuilder.show()
    }

    private fun buildResolution(width: Int, height: Int, sarNum: Int, sarDen: Int): String {
        val sb = StringBuilder()
        sb.append(width)
        sb.append(" x ")
        sb.append(height)
        if (sarNum > 1 || sarDen > 1) {
            sb.append("[")
            sb.append(sarNum)
            sb.append(":")
            sb.append(sarDen)
            sb.append("]")
        }
        return sb.toString()
    }

    private fun buildTimeMilli(duration: Long): String {
        val total_seconds = duration / 1000
        val hours = total_seconds / 3600
        val minutes = total_seconds % 3600 / 60
        val seconds = total_seconds % 60
        if (duration <= 0) {
            return "--:--"
        }
        return if (hours >= 100) {
            String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
        } else if (hours > 0) {
            String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }
    }

    private fun buildTrackType(type: Int): String {
        val context = context
        return when (type) {
            ITrackInfo.MEDIA_TRACK_TYPE_VIDEO -> context.getString(R.string.TrackType_video)
            ITrackInfo.MEDIA_TRACK_TYPE_AUDIO -> context.getString(R.string.TrackType_audio)
            ITrackInfo.MEDIA_TRACK_TYPE_SUBTITLE -> context.getString(R.string.TrackType_subtitle)
            ITrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT -> context.getString(R.string.TrackType_timedtext)
            ITrackInfo.MEDIA_TRACK_TYPE_METADATA -> context.getString(R.string.TrackType_metadata)
            ITrackInfo.MEDIA_TRACK_TYPE_UNKNOWN -> context.getString(R.string.TrackType_unknown)
            else -> context.getString(R.string.TrackType_unknown)
        }
    }

    private fun buildLanguage(language: String): String {
        return if (TextUtils.isEmpty(language)) "und" else language
    }

    val trackInfo: Array<ITrackInfo>?
        get() = if (mMediaPlayer == null) null else mMediaPlayer!!.trackInfo

    fun selectTrack(stream: Int) {
        MediaPlayerCompat.selectTrack(mMediaPlayer, stream)
    }

    fun deselectTrack(stream: Int) {
        MediaPlayerCompat.deselectTrack(mMediaPlayer, stream)
    }

    fun getSelectedTrack(trackType: Int): Int {
        return MediaPlayerCompat.getSelectedTrack(mMediaPlayer, trackType)
    }

    companion object {
        // all possible internal states
        private const val STATE_ERROR = -1
        private const val STATE_IDLE = 0
        private const val STATE_PREPARING = 1
        private const val STATE_PREPARED = 2
        private const val STATE_PLAYING = 3
        private const val STATE_PAUSED = 4
        private const val STATE_PLAYBACK_COMPLETED = 5

        // REMOVED: getAudioSessionId();
        // REMOVED: onAttachedToWindow();
        // REMOVED: onDetachedFromWindow();
        // REMOVED: onLayout();
        // REMOVED: draw();
        // REMOVED: measureAndLayoutSubtitleWidget();
        // REMOVED: setSubtitleWidget();
        // REMOVED: getSubtitleLooper();
        //-------------------------
        // Extend: Aspect Ratio
        //-------------------------
        private val s_allAspectRatio = intArrayOf(
                IRenderView.AR_ASPECT_FIT_PARENT,
                IRenderView.AR_ASPECT_FILL_PARENT,
                IRenderView.AR_ASPECT_WRAP_CONTENT,  // IRenderView.AR_MATCH_PARENT,
                IRenderView.AR_16_9_FIT_PARENT,
                IRenderView.AR_4_3_FIT_PARENT)

        //-------------------------
        // Extend: Render
        //-------------------------
        const val RENDER_NONE = 0
        const val RENDER_SURFACE_VIEW = 1
        const val RENDER_TEXTURE_VIEW = 2
        fun getRenderText(context: Context, render: Int): String {
            val text: String
            text = when (render) {
                RENDER_NONE -> context.getString(R.string.VideoView_render_none)
                RENDER_SURFACE_VIEW -> context.getString(R.string.VideoView_render_surface_view)
                RENDER_TEXTURE_VIEW -> context.getString(R.string.VideoView_render_texture_view)
                else -> context.getString(R.string.N_A)
            }
            return text
        }

        fun getPlayerText(context: Context, player: Int): String {
            val text: String
            text = when (player) {
                Settings.Companion.PV_PLAYER__AndroidMediaPlayer -> context.getString(R.string.VideoView_player_AndroidMediaPlayer)
                Settings.Companion.PV_PLAYER__IjkMediaPlayer -> context.getString(R.string.VideoView_player_IjkMediaPlayer)
                Settings.Companion.PV_PLAYER__IjkExoMediaPlayer -> context.getString(R.string.VideoView_player_IjkExoMediaPlayer)
                else -> context.getString(R.string.N_A)
            }
            return text
        }
    }
}