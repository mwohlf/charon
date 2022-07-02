package net.wohlfart.charon.config

import org.springframework.stereotype.Component

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse


@Component
class AngularRoutesFilter : Filter {

    val API_PATH = "/api"
    val SWAGGER_CONFIG_PATH = "/v3/api-docs"
    val INDEX_HTML = "/index.html"

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterchain: FilterChain) {
        return if (isAngularRoute(request.servletContext.contextPath)) {
            request.getRequestDispatcher(INDEX_HTML).forward(request,response);
        } else filterchain.doFilter(request, response);
    }

    private fun isAngularRoute(path: String): Boolean {
        val result = (!path.contains(".") // this indicates a html,jpg,js file we don't want to reroute that
            && !path.startsWith(API_PATH)
            && !path.startsWith(SWAGGER_CONFIG_PATH))
        if (result) {
            println("re-routing: '$path' to the $INDEX_HTML resource")
        } else {
            println("serving: '$path'")
        }
        return result
    }

}