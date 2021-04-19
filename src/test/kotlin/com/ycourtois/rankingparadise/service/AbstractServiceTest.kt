package com.ycourtois.rankingparadise.service

import com.ycourtois.rankingparadise.di.component.DaggerServiceComponentTest
import com.ycourtois.rankingparadise.di.component.ServiceComponentTest
import com.ycourtois.rankingparadise.di.module.DaoModuleTest
import com.ycourtois.rankingparadise.di.module.MapperModule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractServiceTest {

    protected lateinit var component: ServiceComponentTest

    @BeforeAll
    fun before() {
        component = DaggerServiceComponentTest.builder()
            .daoModuleTest(DaoModuleTest())
            .mapperModule(MapperModule())
            .build()
    }
}