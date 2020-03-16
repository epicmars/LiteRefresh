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

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import tv.danmaku.ijk.media.player.IMediaPlayer

interface IRenderView {
    fun getView(): View?
    fun shouldWaitForResize(): Boolean
    fun setVideoSize(videoWidth: Int, videoHeight: Int)
    fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int)
    fun setVideoRotation(degree: Int)
    fun setAspectRatio(aspectRatio: Int)
    fun addRenderCallback(callback: IRenderCallback)
    fun removeRenderCallback(callback: IRenderCallback)
    interface ISurfaceHolder {
        fun bindToMediaPlayer(mp: IMediaPlayer?)
        fun getRenderView(): IRenderView
        fun getSurfaceHolder(): SurfaceHolder?
        fun openSurface(): Surface?
        fun getSurfaceTexture(): SurfaceTexture?
    }

    interface IRenderCallback {
        /**
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        fun onSurfaceCreated(holder: ISurfaceHolder, width: Int, height: Int)

        /**
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        fun onSurfaceChanged(holder: ISurfaceHolder, format: Int, width: Int, height: Int)
        fun onSurfaceDestroyed(holder: ISurfaceHolder)
    }

    companion object {
        const val AR_ASPECT_FIT_PARENT = 0 // without clip
        const val AR_ASPECT_FILL_PARENT = 1 // may clip
        const val AR_ASPECT_WRAP_CONTENT = 2
        const val AR_MATCH_PARENT = 3
        const val AR_16_9_FIT_PARENT = 4
        const val AR_4_3_FIT_PARENT = 5
    }
}