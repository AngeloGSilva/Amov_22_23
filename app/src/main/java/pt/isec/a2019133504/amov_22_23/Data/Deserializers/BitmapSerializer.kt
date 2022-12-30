package pt.isec.a2019133504.amov_22_23.Data.Deserializers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.json.JSONObject
import pt.isec.a2019133504.amov_22_23.ProfileActivity
import java.io.ByteArrayOutputStream
import java.util.*

object BitmapSerializer : KSerializer<Bitmap> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IntRange", PrimitiveKind.STRING)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(encoder: Encoder, value: Bitmap) {
        var baos = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 90, baos)
        encoder.encodeString(Base64.getEncoder().encodeToString(baos.toByteArray()))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): Bitmap {
        val _decoder = Base64.getDecoder().decode(decoder.decodeString())
        return BitmapFactory.decodeByteArray(_decoder, 0, _decoder.size)
    }
}