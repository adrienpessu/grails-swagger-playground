package swagger.boilerplate

import grails.core.GrailsApplication
import grails.core.GrailsControllerClass
import grails.web.mapping.UrlMapping
import grails.web.mapping.UrlMappings
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.inject.MethodExecutionHandle
import io.micronaut.web.router.MethodBasedRoute
import io.micronaut.web.router.Router
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener

@Slf4j
@CompileStatic
class MicronautToGrailsRouter implements ApplicationListener<ApplicationStartedEvent> {

    private final GrailsApplication grailsApplication
    private final UrlMappings urlMappings
    private final Router router

    MicronautToGrailsRouter(GrailsApplication grailsApplication, UrlMappings urlMappings, Router router) {
        this.grailsApplication = grailsApplication
        this.urlMappings = urlMappings
        this.router = router
    }

    @Override
    void onApplicationEvent(ApplicationStartedEvent event) {
        createRoutes()
    }

    @CompileDynamic
    private void createRoutes() {
        router.uriRoutes().forEach({ r ->
            if (!(r instanceof MethodBasedRoute)) {
                return
            }

            MethodBasedRoute mbr = (MethodBasedRoute) r
            MethodExecutionHandle targetMethod = mbr.targetMethod

            GrailsControllerClass controllerClass = grailsApplication.getArtefact('Controller', targetMethod.declaringType.name) as GrailsControllerClass

            if (!controllerClass) {
                return
            }

            MappingVariables variables = MappingVariables.convert(r.uriMatchTemplate)

            Collection<UrlMapping> mappings = urlMappings.addMappings {
                for (String variableName in variables.variableNames) {
                    delegate."$variableName"
                }
                delegate."$variables.mappingUrl"(controller: controllerClass.logicalPropertyName, action: targetMethod.methodName, method: r.httpMethod.name())
            }

            println mappings
        })


    }
}
