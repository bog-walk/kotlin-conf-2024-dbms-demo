package dev.bogwalk.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PlanetInfo(
    val id: Long,
    val name: String,
    val region: Region,
    val distance: Double,
    @Serializable(with = IntRangeSerializer::class)
    val prices: IntRange
)

internal object IntRangeSerializer : KSerializer<IntRange> {
    private val delegate = ListSerializer(Int.serializer())

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor
        get() = SerialDescriptor("IntRange", delegate.descriptor)

    override fun serialize(encoder: Encoder, value: IntRange) {
        val data = listOf(value.first, value.last)
        encoder.encodeSerializableValue(delegate, data)
    }

    override fun deserialize(decoder: Decoder): IntRange {
        val (start, end) = decoder.decodeSerializableValue(delegate)
        return start..end
    }
}
