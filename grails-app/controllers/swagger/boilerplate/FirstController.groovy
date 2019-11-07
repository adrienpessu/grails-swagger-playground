package swagger.boilerplate

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation

import javax.annotation.Nullable

@Controller("/foo")
class FirstController {

    @Get('/bar')
    @Operation(summary = "Greeting someone")
    def index() {
        render("ok")
    }

    @Get('/bar/{who}{/greeting:[a-zA-Z]+}{.format}{?max,offset}')
    @Operation(
            summary = "Greeting someone by name"
    )
    def greet(String who, @Nullable String greeting, @Nullable String format, @Nullable Integer max, @Nullable Integer offset) {
        render("${greeting ?: 'hello'} $who with format $format, max $max and offset $offset")
    }

}
