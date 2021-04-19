package com.ycourtois.rankingparadise

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ycourtois.rankingparadise.cli.DynamodbDeleteCommand
import com.ycourtois.rankingparadise.cli.DynamodbInitCommand
import com.ycourtois.rankingparadise.configuration.RankingParadiseConfig
import com.ycourtois.rankingparadise.di.component.DaggerRankingParadiseComponent
import com.ycourtois.rankingparadise.di.component.RankingParadiseComponent
import com.ycourtois.rankingparadise.di.module.ConfigurationModule
import com.ycourtois.rankingparadise.di.module.ServiceModule
import com.ycourtois.rankingparadise.exception.PlayerAlreadyExistsException
import io.dropwizard.Application
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper


class RankingParadiseApp : Application<RankingParadiseConfig>() {

    /*
     * Object expressions create objects of anonymous classes, that is, classes that aren't explicitly declared with the class declaration.
     * @see https://kotlinlang.org/docs/object-declarations.html
     */
    // singleton instance
    companion object {
        // make the method static
        @JvmStatic
        fun main(args: Array<String>) = RankingParadiseApp().run(*args)
    }

    override fun run(configuration: RankingParadiseConfig, environment: Environment) {

        val component: RankingParadiseComponent = DaggerRankingParadiseComponent.builder()
            .serviceModule(ServiceModule())
            .configurationModule(ConfigurationModule(configuration))
            .build()
        environment.jersey().register(component.playerResource())
        environment.jersey().register(component.tournamentResource())
        environment.healthChecks().register("database health check", component.databaseHealthCheck())

        // exception responses mapping
        registerExceptionMappings(environment)
    }

    override fun addDefaultCommands(bootstrap: Bootstrap<RankingParadiseConfig>?) {
        super.addDefaultCommands(bootstrap)

        // add custom commands to create and delete dynamodb table
        bootstrap!!.addCommand(DynamodbInitCommand(bootstrap.application))
        bootstrap.addCommand(DynamodbDeleteCommand(bootstrap.application))
    }

    override fun initialize(bootstrap: Bootstrap<RankingParadiseConfig>?) {
        // allows the application to find a given configuration file in the resource directory
        // and to substitute configuration settings with the value of environment variables
        bootstrap!!.configurationSourceProvider =
            SubstitutingSourceProvider(ResourceConfigurationSourceProvider(), EnvironmentVariableSubstitutor(false))
        bootstrap.objectMapper.registerModule(KotlinModule())
    }

    private fun registerExceptionMappings(environment: Environment) {
        // Dynamodb not found mapping exception
        environment.jersey().register(object : ExceptionMapper<ResourceNotFoundException> {
            override fun toResponse(exception: ResourceNotFoundException): Response {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(exception.message)
                    .type("application/json")
                    .build()
            }
        })

        // Player already exists mapping exception
        environment.jersey().register(object : ExceptionMapper<PlayerAlreadyExistsException> {
            override fun toResponse(exception: PlayerAlreadyExistsException): Response {
                return Response.status(Response.Status.CONFLICT)
                    .entity(exception.message)
                    .type("application/json")
                    .build()
            }
        })
    }
}

// Use this if you do not want to pass custom args
//fun main(args: Array<String>) {
//    RankingParadiseApp().run("server", "dw-config.yml")
//}

