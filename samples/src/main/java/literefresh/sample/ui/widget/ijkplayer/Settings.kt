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

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import literefresh.sample.R

class Settings(context: Context?) {
    private val mAppContext: Context
    private val mSharedPreferences: SharedPreferences
    val enableBackgroundPlay: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_enable_background_play)
            return mSharedPreferences.getBoolean(key, false)
        }

    val player: Int
        get() {
            val key = mAppContext.getString(R.string.pref_key_player)
            val value = mSharedPreferences.getString(key, "")
            return try {
                Integer.valueOf(value).toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }

    val usingMediaCodec: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_using_media_codec)
            return mSharedPreferences.getBoolean(key, false)
        }

    val usingMediaCodecAutoRotate: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_using_media_codec_auto_rotate)
            return mSharedPreferences.getBoolean(key, false)
        }

    val mediaCodecHandleResolutionChange: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_media_codec_handle_resolution_change)
            return mSharedPreferences.getBoolean(key, false)
        }

    val usingOpenSLES: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_using_opensl_es)
            return mSharedPreferences.getBoolean(key, false)
        }

    val pixelFormat: String
        get() {
            val key = mAppContext.getString(R.string.pref_key_pixel_format)
            return mSharedPreferences.getString(key, "")
        }

    val enableNoView: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_enable_no_view)
            return mSharedPreferences.getBoolean(key, false)
        }

    val enableSurfaceView: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_enable_surface_view)
            return mSharedPreferences.getBoolean(key, false)
        }

    val enableTextureView: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_enable_texture_view)
            return mSharedPreferences.getBoolean(key, false)
        }

    val enableDetachedSurfaceTextureView: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_enable_detached_surface_texture)
            return mSharedPreferences.getBoolean(key, false)
        }

    val usingMediaDataSource: Boolean
        get() {
            val key = mAppContext.getString(R.string.pref_key_using_mediadatasource)
            return mSharedPreferences.getBoolean(key, false)
        }

    var lastDirectory: String?
        get() {
            val key = mAppContext.getString(R.string.pref_key_last_directory)
            return mSharedPreferences.getString(key, "/")
        }
        set(path) {
            val key = mAppContext.getString(R.string.pref_key_last_directory)
            mSharedPreferences.edit().putString(key, path).apply()
        }

    companion object {
        const val PV_PLAYER__Auto = 0
        const val PV_PLAYER__AndroidMediaPlayer = 1
        const val PV_PLAYER__IjkMediaPlayer = 2
        const val PV_PLAYER__IjkExoMediaPlayer = 3
    }

    init {
        mAppContext = context!!.applicationContext
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppContext)
    }
}