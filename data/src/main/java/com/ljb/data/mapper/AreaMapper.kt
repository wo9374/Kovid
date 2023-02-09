package com.ljb.data.mapper

import com.ljb.data.model.AreaResponse
import com.ljb.domain.entity.AreaCovid

object AreaMapper {
    fun mapperToArea(areaResponse: AreaResponse): AreaCovid {
        return AreaCovid(
            countryName = areaResponse.countryName,
            newCase = areaResponse.newCase,
            totalCase = areaResponse.totalCase,
            recovered = areaResponse.recovered,
            death = areaResponse.death,
            percentage = areaResponse.percentage,
            newCcase = areaResponse.newCcase,
            newFcase = areaResponse.newFcase,
        )
    }
}