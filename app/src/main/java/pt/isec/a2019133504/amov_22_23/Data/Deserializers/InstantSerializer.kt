package pt.isec.a2019133504.amov_22_23.Data.Deserializers

import android.graphics.Bitmap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.time.Instant

typealias _Instant = @Serializable(InstantSerializer::class) Instant

class InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(Json.encodeToString(value.toEpochMilli()))
    }

    override fun deserialize(decoder: Decoder): Instant {
        val value = Json.decodeFromString<Long>(decoder.decodeString())
        return Instant.ofEpochMilli(value)
    }
}