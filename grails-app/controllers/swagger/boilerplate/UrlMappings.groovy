package swagger.boilerplate

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "/first"(controller: "first", action: "index", method: "GET")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
