package com.ljb.data.mapper

import com.ljb.data.remote.model.AreaResponse
import com.ljb.domain.entity.Area

object AreaMapper {
    fun mapperToArea(areaResponse: AreaResponse): Area {
        return Area(
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