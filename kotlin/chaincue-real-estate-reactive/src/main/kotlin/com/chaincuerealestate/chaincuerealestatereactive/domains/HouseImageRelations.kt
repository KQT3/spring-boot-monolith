package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.relational.core.mapping.Table


@Table("house_image_relations")
data class HouseImageRelations(
    var houseId: String,
    var imageId: String
) : AbstractDomain() {
    companion object {
        fun create(houseId: String, imageId: String): HouseImageRelations {
            return HouseImageRelations(
                houseId,
                imageId
            )
        }
    }
}
