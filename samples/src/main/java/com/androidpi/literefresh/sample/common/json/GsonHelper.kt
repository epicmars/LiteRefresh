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
package com.androidpi.literefresh.sample.common.json

import com.google.gson.*
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object GsonHelper {

    val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    val formater: DateFormat = SimpleDateFormat(DATETIME_FORMAT)

    private var gson: Gson? = null

    fun gson(): Gson {
        if (null == gson) {
            gson = GsonBuilder()
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .registerTypeAdapter(Date::class.java, DateSerializer())
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
        }
        return gson!!
    }

    class DateDeserializer : JsonDeserializer<Date> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
            try {
                return formater.parse(json.asJsonPrimitive.asString)
            } catch (e: ParseException) {
                throw JsonParseException(e)
            }

        }
    }

    class DateSerializer : JsonSerializer<Date> {

        override fun serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(formater.format(src))
        }
    }
}
