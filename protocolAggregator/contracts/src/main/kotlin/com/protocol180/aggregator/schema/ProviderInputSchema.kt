package com.protocol180.aggregator.schema

import net.corda.core.schemas.MappedSchema
import javax.persistence.*


object ProviderInputSchema

object ProviderInputSchemaV1 : MappedSchema(schemaFamily = ProviderInputSchema.javaClass,
        version = 1,
        mappedTypes = listOf(ProviderInput::class.java, DataOutput::class.java)) {
    @Entity
    @Table(name = "PROVIDER_INPUT")
    class ProviderInput(@Id
                        @GeneratedValue(strategy = GenerationType.IDENTITY)
                        val id: Int = 0,
                        @Column(name = "public_key", nullable = false)
                        var publicKey: String,
                        @Column(name = "input", nullable = false)
                        val input: ByteArray
    ) {
        constructor() : this(0, "", ByteArray(0))
    }

    @Entity
    @Table(name = "DATA_OUTPUT")
    class DataOutput(@Id
                     @Column(name = "state_ref", nullable = false, unique = true)
                     val stateRef: String,
                     @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
                     val providerInputs: List<ProviderInput>
    ) {
        constructor() : this("", listOf())

    }
}