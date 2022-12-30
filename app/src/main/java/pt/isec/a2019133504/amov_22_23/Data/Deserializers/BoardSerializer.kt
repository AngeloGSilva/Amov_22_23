package pt.isec.a2019133504.amov_22_23.Data.Deserializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import pt.isec.a2019133504.amov_22_23.Data.Board

object BoardSerializer: KSerializer<Board> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntRange", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Board) {
        TODO()
    }

    override fun deserialize(decoder: Decoder): Board {
        TODO()
    }
}