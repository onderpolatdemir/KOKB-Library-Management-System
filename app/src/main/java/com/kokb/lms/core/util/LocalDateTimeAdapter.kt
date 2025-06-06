package com.kokb.lms.core.util

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        try {
            return json?.asString?.let {
                LocalDateTime.parse(it, formatter)
            }
        } catch (e: DateTimeParseException) {
            // If standard ISO format fails, try parsing with a more lenient format
            try {
                val lenientFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
                return json?.asString?.let {
                    LocalDateTime.parse(it, lenientFormatter)
                }
            } catch (e: DateTimeParseException) {
                e.printStackTrace()
                return null
            }
        }
    }
} 