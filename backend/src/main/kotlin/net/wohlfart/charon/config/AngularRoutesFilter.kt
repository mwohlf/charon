package net.wohlfart.charon.config

import net.wohlfart.charon.CharonProperties
import org.springframework.stereotype.Component
import java.util.logging.Logger

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


fun requestUrl(request: ServletRequest?): String? {
    return if (request !is HttpServletRequest) null else (request as HttpServletRequest?)?.requestURL?.toString()
}

@Component
class AngularRoutesFilter(val charonProperties: CharonProperties) : Filter {
    var logger = Logger.getLogger(this.javaClass.name)

    val API_PATH = "/api"
    val SWAGGER_CONFIG_PATH = "/v3/api-docs"
    val INDEX_HTML = "/index.html"

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterchain: FilterChain) {
        return if (isAngularRoute(requestUrl(request))) {
            logger.info("re-routing: '" + requestUrl(request) + "' to " + charonProperties.webjarBase + INDEX_HTML)
            request.getRequestDispatcher(charonProperties.webjarBase + INDEX_HTML).forward(request, response);
        } else filterchain.doFilter(request, response);
    }

    private fun isAngularRoute(path: String?): Boolean {
        if (path == null) {
            return false
        }
        if (path.contains(".")) {
            return false // this indicates a html,jpg,js file we don't want to reroute that
        }
        if (path.startsWith(API_PATH)) {
            return false // we don't want to re-route api traffic
        }
        if (path.startsWith(SWAGGER_CONFIG_PATH)) {
            return false // we don't want to re-route api traffic
        }
        return true;
    }

}
