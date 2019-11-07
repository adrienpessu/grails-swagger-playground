package swagger.boilerplate

import com.agorapulse.gru.Gru
import com.agorapulse.gru.http.Http
import grails.testing.mixin.integration.Integration
import org.junit.Rule
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@Integration(applicationClass = Application)
@SpringBootTest(
        properties = 'server.port=54345',
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class MicronautToGrailsRouterIntegrationSpec extends Specification {

    @Rule Gru<Http> gru = Gru.equip(Http.steal(this))

    void setup() {
        final String serverUrl = "http://localhost:54345"
        gru.prepare {
            baseUri serverUrl
        }
    }

    void 'get simple url'() {
        expect:
            gru.test {
                get '/foo/bar'
                expect {
                    text inline('ok')
                }
            }
    }

    void 'get complex'() {
        expect:
            gru.test {
                get '/foo/bar/vlad/test.png', {
                    params max: 10, offset: 100
                }
                expect {
                    text inline('test vlad with format png, max 10 and offset 100')
                }
            }
    }

    void 'get no optionals'() {
        expect:
            gru.test {
                get '/foo/bar/vlad'
                expect {
                    text inline('hello vlad with format null, max null and offset null')
                }
            }
    }
}
