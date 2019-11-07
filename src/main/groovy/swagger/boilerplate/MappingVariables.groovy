package swagger.boilerplate

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.micronaut.http.uri.UriMatchTemplate
import io.micronaut.http.uri.UriMatchVariable

@ToString
@CompileStatic
@EqualsAndHashCode
class MappingVariables {

    final String mappingUrl;
    final List<String> variableNames;

    MappingVariables(String mappingUrl, List<String> variableNames) {
        this.mappingUrl = mappingUrl
        this.variableNames = variableNames
    }

    static MappingVariables convert(UriMatchTemplate template) {
        List<UriMatchVariable> nonQueryVariables = template.variables.findAll {
            !it.query
        }

        String mappingUri = template.toPathString()

        mappingUri = clearQueryVariables(mappingUri)
        mappingUri = convertCapturingVariable(nonQueryVariables, mappingUri)
        mappingUri = convertOptionalVariables(nonQueryVariables, mappingUri)
        mappingUri = convertRegularVariables(nonQueryVariables, mappingUri)

        return new MappingVariables(mappingUri, nonQueryVariables*.name)
    }

    private static String convertRegularVariables(List<UriMatchVariable> nonQueryVariables, String mappingUri) {
        nonQueryVariables.findAll { !it.optional && !it.exploded && !it.query }.each {
            mappingUri = mappingUri.replaceAll("\\{${it.name}.*?}", "(*)")
            mappingUri = mappingUri.replaceAll("\\{\\.${it.name}.*?}", "(.(*))?")
        }
        return mappingUri
    }

    private static String convertOptionalVariables(List<UriMatchVariable> nonQueryVariables, String mappingUri) {
        nonQueryVariables.findAll { it.optional }.each {
            mappingUri = mappingUri.replaceAll("\\{/${it.name}.*?}", "/(*)?")
            mappingUri = mappingUri.replaceAll("\\{.${it.name}.*?}", "(*)?")
        }
        return mappingUri
    }

    private static String convertCapturingVariable(List<UriMatchVariable> nonQueryVariables, String mappingUri) {
        nonQueryVariables.each {
            mappingUri = mappingUri.replaceAll("\\{${it.name}\\:\\.\\*}", "(*)**")
            mappingUri = mappingUri.replaceAll("\\{/${it.name}\\:\\.\\*}", "/(*)**")
        }
        return mappingUri
    }

    private static String clearQueryVariables(String mappingUri) {
        return mappingUri.replaceAll(/\{\?.*?}/, '')
    }

}
