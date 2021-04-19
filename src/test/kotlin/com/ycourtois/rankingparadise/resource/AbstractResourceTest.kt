package com.ycourtois.rankingparadise.resource

import com.ycourtois.rankingparadise.di.component.DaggerResourceComponentTest
import com.ycourtois.rankingparadise.di.component.ResourceComponentTest
import com.ycourtois.rankingparadise.di.module.ServiceModuleTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractResourceTest {

    protected lateinit var component: ResourceComponentTest

    @BeforeAll
    fun before() {
        component = DaggerResourceComponentTest.builder()
            .serviceModuleTest(ServiceModuleTest())
            .build()
    }
}