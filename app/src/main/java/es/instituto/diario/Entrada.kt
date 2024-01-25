package es.instituto.diario

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


@Serializable
@Root(name = "entrada")
data class Entrada(
    @SerialName("fecha")
    @Serializable(DateSerializer::class)
    @field:Element(name = "fecha")
    var fecha: Date,
    @SerialName("texto")
    @field:Element(name = "texto")
    var texto: String
)

object DateSerializer: KSerializer<Date> {

    override val descriptor: SerialDescriptor= PrimitiveSerialDescriptor("Date",PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Date=Date(decoder.decodeLong())
    override fun serialize(encoder: Encoder, value: Date) =encoder.encodeLong(value.time)
}
