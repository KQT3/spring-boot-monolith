package com.chaincuerealestate.chaincuerealestatereactive.domains

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.util.*

abstract class DomainObject(
    @Id private val id: String = UUID.randomUUID().toString(),
    @Transient private val isNew: Boolean = true
) : Persistable<String> {
    override fun getId() = id
    @JsonIgnore
    override fun isNew() = isNew
}
