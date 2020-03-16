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

import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

class FileMediaDataSource(file: File?) : IMediaDataSource {
    private var mFile: RandomAccessFile?
    private var mFileSize: Long

    @Throws(IOException::class)
    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
        if (mFile!!.filePointer != position) mFile!!.seek(position)
        return if (size == 0) 0 else mFile!!.read(buffer, 0, size)
    }

    @Throws(IOException::class)
    override fun getSize(): Long {
        return mFileSize
    }

    @Throws(IOException::class)
    override fun close() {
        mFileSize = 0
        mFile!!.close()
        mFile = null
    }

    init {
        mFile = RandomAccessFile(file, "r")
        mFileSize = mFile!!.length()
    }
}