package swagger.boilerplate

import groovy.transform.CompileDynamic
import io.micronaut.http.uri.UriMatchTemplate
import spock.lang.Specification
import spock.lang.Unroll

@CompileDynamic
class MappingVariablesSpec extends Specification {

    @Unroll
    void 'convert uri template #template to #mappingUri with variables #variables'() {
        given:
            MappingVariables mappingVariables = MappingVariables.convert(UriMatchTemplate.of(template))
        expect:
            mappingVariables
            mappingVariables.mappingUrl == mappingUri
            mappingVariables.variableNames == variables
        where:
            template                                                | mappingUri                | variables
            '/foo/bar'                                              | '/foo/bar'                | []
            '/foo/{bar}'                                            | '/foo/(*)'                | ['bar']
            '/foo/{bar:2}'                                          | '/foo/(*)'                | ['bar']
            '/foo{/bar}'                                            | '/foo/(*)?'               | ['bar']
            '/foo{/path:.*}{.ext}'                                  | '/foo/(*)**(.(*))?'       | ['path', 'ext']
            '/foo/{path:.*}{.ext}'                                  | '/foo/(*)**(.(*))?'       | ['path', 'ext']
            '/bar/{who}{/greeting:[a-zA-Z]+}{.ext}{?max,offset}'    | '/bar/(*)/(*)?(.(*))?'    | ['who', 'greeting', 'ext']

    }

}
