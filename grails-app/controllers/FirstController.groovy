import io.micronaut.http.annotation.Controller
import io.swagger.v3.oas.annotations.Operation

@Controller("/")
class FirstController {

    @Operation(summary = "Greeting someone")
    def index() {
        render("<html><body>ok</body></html>")
    }
}
