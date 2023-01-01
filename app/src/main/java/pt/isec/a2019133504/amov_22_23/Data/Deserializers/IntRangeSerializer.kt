package pt.isec.a2019133504.amov_22_23.Data.Deserializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Level

typealias _IntRange = @Serializable(IntRangeSerializer::class) IntRange

object IntRangeSerializer : KSerializer<IntRange> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntRange", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IntRange) {
        encoder.encodeString(Json.encodeToString(listOf<Int>(value.start, value.last)))
    }

    override fun deserialize(decoder: Decoder): IntRange {
        val list = Json.decodeFromString<List<Int>>(decoder.decodeString())
        return IntRange(list[0],list[1])
    }
}