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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import literefresh.sample.R

class TableLayoutBinder {
    private var mContext: Context
    var mTableView: ViewGroup
    var mTableLayout: TableLayout

    @JvmOverloads
    constructor(context: Context, layoutResourceId: Int = R.layout.table_media_info) {
        mContext = context
        mTableView = LayoutInflater.from(mContext).inflate(layoutResourceId, null) as ViewGroup
        mTableLayout = mTableView.findViewById<View>(R.id.table) as TableLayout
    }

    constructor(context: Context, tableLayout: TableLayout) {
        mContext = context
        mTableView = tableLayout
        mTableLayout = tableLayout
    }

    fun appendRow1(name: String?, value: String?): View {
        return appendRow(R.layout.table_media_info_row1, name, value)
    }

    fun appendRow1(nameId: Int, value: String?): View {
        return appendRow1(mContext.getString(nameId), value)
    }

    fun appendRow2(name: String?, value: String?): View {
        return appendRow(R.layout.table_media_info_row2, name, value)
    }

    fun appendRow2(nameId: Int, value: String?): View {
        return appendRow2(mContext.getString(nameId), value)
    }

    fun appendSection(name: String?): View {
        return appendRow(R.layout.table_media_info_section, name, null)
    }

    fun appendSection(nameId: Int): View {
        return appendSection(mContext.getString(nameId))
    }

    fun appendRow(layoutId: Int, name: String?, value: String?): View {
        val rowView = LayoutInflater.from(mContext).inflate(layoutId, mTableLayout, false) as ViewGroup
        setNameValueText(rowView, name, value)
        mTableLayout.addView(rowView)
        return rowView
    }

    fun obtainViewHolder(rowView: View): ViewHolder {
        var viewHolder: ViewHolder? = null
        if (rowView.tag != null) {
           viewHolder = rowView.tag as ViewHolder
        }
        if (viewHolder == null) {
            viewHolder = ViewHolder()
            viewHolder.mNameTextView = rowView.findViewById<View>(R.id.name) as TextView
            viewHolder.mValueTextView = rowView.findViewById<View>(R.id.value) as TextView
            rowView.tag = viewHolder
        }
        return viewHolder
    }

    fun setNameValueText(rowView: View, name: String?, value: String?) {
        val viewHolder = obtainViewHolder(rowView)
        viewHolder.setName(name)
        viewHolder.setValue(value)
    }

    fun setValueText(rowView: View, value: String?) {
        val viewHolder = obtainViewHolder(rowView)
        viewHolder.setValue(value)
    }

    fun buildLayout(): ViewGroup {
        return mTableView
    }

    fun buildAlertDialogBuilder(): AlertDialog.Builder {
        val dlgBuilder = AlertDialog.Builder(mContext)
        dlgBuilder.setView(buildLayout())
        return dlgBuilder
    }

    class ViewHolder {
        var mNameTextView: TextView? = null
        var mValueTextView: TextView? = null
        fun setName(name: String?) {
            if (mNameTextView != null) {
                mNameTextView!!.text = name
            }
        }

        fun setValue(value: String?) {
            if (mValueTextView != null) {
                mValueTextView!!.text = value
            }
        }
    }
}