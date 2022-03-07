package me.ikvarxt.halo.network.converters

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

class CalendarGsonConverter : JsonDeserializer<Calendar> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = json?.asLong!!
        }
    }
}