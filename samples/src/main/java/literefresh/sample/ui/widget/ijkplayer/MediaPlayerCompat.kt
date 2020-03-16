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

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.MediaPlayerProxy
import tv.danmaku.ijk.media.player.TextureMediaPlayer

object MediaPlayerCompat {
    fun getName(mp: IMediaPlayer?): String {
        return if (mp == null) {
            "null"
        } else if (mp is TextureMediaPlayer) {
            val sb = StringBuilder("TextureMediaPlayer <")
            val internalMediaPlayer = mp.internalMediaPlayer
            if (internalMediaPlayer == null) {
                sb.append("null>")
            } else {
                sb.append(internalMediaPlayer.javaClass.simpleName)
                sb.append(">")
            }
            sb.toString()
        } else {
            mp.javaClass.simpleName
        }
    }

    fun getIjkMediaPlayer(mp: IMediaPlayer?): IjkMediaPlayer? {
        var ijkMediaPlayer: IjkMediaPlayer? = null
        if (mp == null) {
            return null
        }
        if (mp is IjkMediaPlayer) {
            ijkMediaPlayer = mp
        } else if (mp is MediaPlayerProxy && mp.internalMediaPlayer is IjkMediaPlayer) {
            ijkMediaPlayer = mp.internalMediaPlayer as IjkMediaPlayer
        }
        return ijkMediaPlayer
    }

    fun selectTrack(mp: IMediaPlayer?, stream: Int) {
        val ijkMediaPlayer = getIjkMediaPlayer(mp) ?: return
        ijkMediaPlayer.selectTrack(stream)
    }

    fun deselectTrack(mp: IMediaPlayer?, stream: Int) {
        val ijkMediaPlayer = getIjkMediaPlayer(mp) ?: return
        ijkMediaPlayer.deselectTrack(stream)
    }

    fun getSelectedTrack(mp: IMediaPlayer?, trackType: Int): Int {
        val ijkMediaPlayer = getIjkMediaPlayer(mp) ?: return -1
        return ijkMediaPlayer.getSelectedTrack(trackType)
    }
}