/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package literefresh.sample.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import literefresh.sample.R
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.model.SampleUnsplashPhoto
import literefresh.sample.ui.viewholder.SampleUnsplashPhotoViewHolder
import java.util.*

class SampleContentView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {
    private val recyclerView: RecyclerView
    private val recyclerAdapter: RecyclerAdapter
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (recyclerAdapter.payloads.isEmpty()) {
            recyclerAdapter.setPayloads(photos)
        }
    }

    companion object {
        private val photos: List<SampleUnsplashPhoto> = object : ArrayList<SampleUnsplashPhoto>() {
            init {
                add(SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1))
                add(SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2))
                add(SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6))
                add(SampleUnsplashPhoto("neida zarate", R.mipmap.photo7))
                add(SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1))
                add(SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2))
                add(SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6))
                add(SampleUnsplashPhoto("neida zarate", R.mipmap.photo7))
                add(SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1))
                add(SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2))
                add(SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6))
                add(SampleUnsplashPhoto("neida zarate", R.mipmap.photo7))
            }
        }
    }

    init {
        View.inflate(context, R.layout.view_sample_content, this)
        recyclerAdapter = RecyclerAdapter()
        recyclerAdapter.register(SampleUnsplashPhotoViewHolder::class.java)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = recyclerAdapter
    }
}