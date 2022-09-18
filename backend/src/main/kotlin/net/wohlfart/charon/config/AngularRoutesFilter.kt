package net.wohlfart.charon.config

import net.wohlfart.charon.CharonProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import java.util.logging.Logger

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


@Component
class AngularRoutesFilter(val charonProperties: CharonProperties) : Filter {
    var logger = Logger.getLogger(this.javaClass.name)

    val API_PATH = "/api"
    val SWAGGER_CONFIG_PATH = "/v3/api-docs"
    val ACTUATOR_PATH = "/actuator"
    val ASSETS_PATH = "/assets"
    val INDEX_HTML = "/index.html"

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterchain: FilterChain) {
        if (request !is HttpServletRequest) {
            filterchain.doFilter(request, response)
            return
        }
        if (RequestMethod.GET.name != request.method) {
            filterchain.doFilter(request, response)
            return
        }
        // checking for
        // Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
        val mediaTypes = MediaType.parseMediaTypes(request.getHeaders(HttpHeaders.ACCEPT).toList())
        if (!mediaTypes.contains(MediaType.TEXT_HTML) && !mediaTypes.contains(MediaType.APPLICATION_XHTML_XML)) {
            filterchain.doFilter(request, response)
            return
        }
        val route = request.requestURI
        if (!isAngularRoute(route)) {
            filterchain.doFilter(request, response)
            return
        }
        logger.info("re-routing: '$route' to ${charonProperties.webjarBase}$INDEX_HTML")
        request.getRequestDispatcher(charonProperties.webjarBase + INDEX_HTML).forward(request, response)
    }

    private fun isAngularRoute(path: String?): Boolean {
        if (path == null) {
            return false
        }
        if (path.contains(".")) {
            return false // this indicates a html,jpg,js file we don't want to reroute that
        }
        for (prefix in listOf(API_PATH, ACTUATOR_PATH, SWAGGER_CONFIG_PATH, ASSETS_PATH)) {
            if (path.startsWith(prefix)) {
                return false // we don't want to re-route api traffic or asset loading
            }
        }

        return true
    }

}
