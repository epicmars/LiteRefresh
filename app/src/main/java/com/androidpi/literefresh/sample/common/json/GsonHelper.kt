package com.androidpi.literefresh.sample.common.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

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
