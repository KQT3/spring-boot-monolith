package com.chaincuerealestate.chaincuerealestatereactive.domains

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable

//abstract  class DomainObject : Persistable<String> {
//    private val id: String
////    override fun getId() = id
////
////    @JsonIgnore
////    override fun isNew() = isNew
//}


class DomainObject(
    @Id
    private val id: String,
    @Transient
    private val isNew: Boolean = true
) : Persistable<String> {
    override fun getId(): String = id
    override fun isNew(): Boolean = isNew
}
