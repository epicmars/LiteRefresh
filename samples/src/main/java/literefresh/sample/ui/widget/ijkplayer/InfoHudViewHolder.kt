package literefresh.sample.ui.widget.ijkplayer

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.SparseArray
import android.view.View
import android.widget.TableLayout
import literefresh.sample.R
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.MediaPlayerProxy
import java.util.*

class InfoHudViewHolder(context: Context, tableLayout: TableLayout) {
    private val mTableLayoutBinder: TableLayoutBinder
    private val mRowMap = SparseArray<View?>()
    private var mMediaPlayer: IMediaPlayer? = null
    private var mLoadCost: Long = 0
    private var mSeekCost: Long = 0
    private fun appendSection(nameId: Int) {
        mTableLayoutBinder.appendSection(nameId)
    }

    private fun appendRow(nameId: Int) {
        val rowView = mTableLayoutBinder.appendRow2(nameId, null)
        mRowMap.put(nameId, rowView)
    }

    private fun setRowValue(id: Int, value: String) {
        var rowView = mRowMap[id]
        if (rowView == null) {
            rowView = mTableLayoutBinder.appendRow2(id, value)
            mRowMap.put(id, rowView)
        } else {
            mTableLayoutBinder.setValueText(rowView, value)
        }
    }

    fun setMediaPlayer(mp: IMediaPlayer?) {
        mMediaPlayer = mp
        if (mMediaPlayer != null) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500)
        } else {
            mHandler.removeMessages(MSG_UPDATE_HUD)
        }
    }

    fun updateLoadCost(time: Long) {
        mLoadCost = time
    }

    fun updateSeekCost(time: Long) {
        mSeekCost = time
    }

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_HUD -> {
                    val holder = this@InfoHudViewHolder
                    var mp: IjkMediaPlayer? = null
                    if (mMediaPlayer == null) return
                    if (mMediaPlayer is IjkMediaPlayer) {
                        mp = mMediaPlayer as IjkMediaPlayer?
                    } else if (mMediaPlayer is MediaPlayerProxy) {
                        val proxy = mMediaPlayer as MediaPlayerProxy
                        val internal = proxy.internalMediaPlayer
                        if (internal != null && internal is IjkMediaPlayer) mp = internal
                    }
                    if (mp == null) return
                    val vdec = mp.videoDecoder
                    when (vdec) {
                        IjkMediaPlayer.FFP_PROPV_DECODER_AVCODEC -> setRowValue(R.string.vdec, "avcodec")
                        IjkMediaPlayer.FFP_PROPV_DECODER_MEDIACODEC -> setRowValue(R.string.vdec, "MediaCodec")
                        else -> setRowValue(R.string.vdec, "")
                    }
                    val fpsOutput = mp.videoOutputFramesPerSecond
                    val fpsDecode = mp.videoDecodeFramesPerSecond
                    setRowValue(R.string.fps, String.format(Locale.US, "%.2f / %.2f", fpsDecode, fpsOutput))
                    val videoCachedDuration = mp.videoCachedDuration
                    val audioCachedDuration = mp.audioCachedDuration
                    val videoCachedBytes = mp.videoCachedBytes
                    val audioCachedBytes = mp.audioCachedBytes
                    val tcpSpeed = mp.tcpSpeed
                    val bitRate = mp.bitRate
                    val seekLoadDuration = mp.seekLoadDuration
                    setRowValue(R.string.v_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(videoCachedDuration), formatedSize(videoCachedBytes)))
                    setRowValue(R.string.a_cache, String.format(Locale.US, "%s, %s", formatedDurationMilli(audioCachedDuration), formatedSize(audioCachedBytes)))
                    setRowValue(R.string.load_cost, String.format(Locale.US, "%d ms", mLoadCost))
                    setRowValue(R.string.seek_cost, String.format(Locale.US, "%d ms", mSeekCost))
                    setRowValue(R.string.seek_load_cost, String.format(Locale.US, "%d ms", seekLoadDuration))
                    setRowValue(R.string.tcp_speed, String.format(Locale.US, "%s", formatedSpeed(tcpSpeed, 1000)))
                    setRowValue(R.string.bit_rate, String.format(Locale.US, "%.2f kbs", bitRate / 1000f))
                    this.removeMessages(MSG_UPDATE_HUD)
                    sendEmptyMessageDelayed(MSG_UPDATE_HUD, 500)
                }
            }
        }
    }

    companion object {
        private fun formatedDurationMilli(duration: Long): String {
            return if (duration >= 1000) {
                String.format(Locale.US, "%.2f sec", duration.toFloat() / 1000)
            } else {
                String.format(Locale.US, "%d msec", duration)
            }
        }

        private fun formatedSpeed(bytes: Long, elapsed_milli: Long): String {
            if (elapsed_milli <= 0) {
                return "0 B/s"
            }
            if (bytes <= 0) {
                return "0 B/s"
            }
            val bytes_per_sec = bytes.toFloat() * 1000f / elapsed_milli
            return if (bytes_per_sec >= 1000 * 1000) {
                String.format(Locale.US, "%.2f MB/s", bytes_per_sec / 1000 / 1000)
            } else if (bytes_per_sec >= 1000) {
                String.format(Locale.US, "%.1f KB/s", bytes_per_sec / 1000)
            } else {
                String.format(Locale.US, "%d B/s", bytes_per_sec.toLong())
            }
        }

        private fun formatedSize(bytes: Long): String {
            return if (bytes >= 100 * 1000) {
                String.format(Locale.US, "%.2f MB", bytes.toFloat() / 1000 / 1000)
            } else if (bytes >= 100) {
                String.format(Locale.US, "%.1f KB", bytes.toFloat() / 1000)
            } else {
                String.format(Locale.US, "%d B", bytes)
            }
        }

        private const val MSG_UPDATE_HUD = 1
    }

    init {
        mTableLayoutBinder = TableLayoutBinder(context, tableLayout)
    }
}