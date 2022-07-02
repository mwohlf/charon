package net.wohlfart.charon

import com.gargoylesoftware.htmlunit.Page
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.WebResponse
import com.gargoylesoftware.htmlunit.html.HtmlButton
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.util.UriComponentsBuilder



private const val USERNAME = "user"
private const val PASSWORD = "pass"


/*
        //        ?client_id=public-client
        //        &redirect_uri=http%3A%2F%2F127.0.0.1%3A4200
        //        &response_type=code
        //        &scope=openid%20message.read%20message.write
        //        &nonce=3f78c839561b11ae7c5d4eda644d4fb93fhLyHNDZ
        //        &state=f7ed2a0f8b03a4039f7838f8b0e1542dca7ZRZVjo
        //        &code_challenge=_nK6SO7EYw9Wu4TSP4UC9ePwdSFPuHOC-kek3zUsSP8
        //        &code_challenge_method=S256
 */
// see: https://developer.okta.com/blog/2018/04/10/oauth-authorization-code-grant-type
val AUTHORIZATION_REQUEST: String = UriComponentsBuilder
    .fromPath("/oauth2/authorize")
    .queryParam("client_id", "public-client")
    .queryParam("redirect_uri", "http://127.0.0.1:4200")
    .queryParam("response_type", "code")
    .queryParam("scope", "openid message.read message.write")
    .queryParam("nonce", "nonce")
    .queryParam("state", "some-state")
    .queryParam("code_challenge", "_nK6SO7EYw9Wu4TSP4UC9ePwdSFPuHOC-kek3zUsSP8")
    .queryParam("code_challenge_method", "S256")
    .toUriString()


fun assertLoginPage(page: HtmlPage) {
    assertThat(page.url.toString()).endsWith("/login")

    val usernameInput: HtmlInput = page.querySelector("input[name=\"username\"]")
    val passwordInput: HtmlInput = page.querySelector("input[name=\"password\"]")
    val signInButton: HtmlButton = page.querySelector("button")

    assertThat(usernameInput).isNotNull
    assertThat(passwordInput).isNotNull
    assertThat(signInButton.textContent).isEqualTo("Sign in")
}


fun <P : Page> doSignIn(page: HtmlPage, username: String, password: String):  P {
    val usernameInput: HtmlInput = page.querySelector("input[name=\"username\"]")
    val passwordInput: HtmlInput = page.querySelector("input[name=\"password\"]")
    val signInButton: HtmlButton = page.querySelector("button")

    usernameInput.type(username)
    passwordInput.type(password)
    return signInButton.click()
}


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DefaultAuthorizationServerApplicationTests {

    @Autowired
    lateinit var webClient: WebClient

    @BeforeEach
    fun setUp() {
        this.webClient.options.isThrowExceptionOnFailingStatusCode = true
        this.webClient.options.isRedirectEnabled = true
        this.webClient.cookieManager.clearCookies()    // log out
        this.webClient.cache.clear()
    }

    @Test
    fun `when login successful then display not found error`() {
        // there is redirect to login page happening here
        val loginPage: HtmlPage = this.webClient.getPage("/")

        assertLoginPage(loginPage)

        this.webClient.options.isThrowExceptionOnFailingStatusCode = false
        val postLoginPage: Page = doSignIn(loginPage, USERNAME, PASSWORD)
        val signInResponse: WebResponse = postLoginPage.webResponse
        assertThat(signInResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())    // there is no "default" index page
    }

    @Test
    fun `when login fails then display bad credentials`() {
        // there is redirect to login page happening here
        val loginPage: HtmlPage = this.webClient.getPage("/");

        val loginErrorPage: Page = doSignIn(loginPage, USERNAME, "wrong-password")
        assertThat(loginPage.isHtmlPage).isTrue

        val alert: HtmlElement = (loginErrorPage as HtmlPage ).querySelector("div[role=\"alert\"]");
        assertThat(alert).isNotNull
        assertThat(alert.textContent).isEqualTo("Bad credentials")
    }

    @Test
    fun `when logging in and requesting token then redirects to client application`() {

        // login sequence:
        //
        // get config data for oauth server:
        //   http://localhost:9000/issuer/.well-known/openid-configuration
        //
        // initialize login:
        //   GET http://localhost:9000/issuer/oauth2/authorize
        //      ?client_id=public-client
        //      &redirect_uri=http://127.0.0.1:4200
        //      &response_type=code
        //      &scope=openid message.read message.write
        //      &nonce=3f78c839561b11ae7c5d4eda644d4fb93fhLyHNDZ
        //      &state=f7ed2a0f8b03a4039f7838f8b0e1542dca7ZRZVjo
        //      &code_challenge=_nK6SO7EYw9Wu4TSP4UC9ePwdSFPuHOC-kek3zUsSP8
        //      &code_challenge_method=S256
        // --> redirect to http://localhost:9000/issuer/login
        //     GET http://localhost:9000/issuer/login
        //
        // send login data:
        //   POST http://localhost:9000/issuer/login
        //     payload: username=user&password=pass&_csrf=1cbf7c42-1435-4281-a357-e9116234b818
        // --> redirect to
        //     http://localhost:9000/issuer/oauth2/authorize
        //        ?client_id=public-client
        //        &redirect_uri=http%3A%2F%2F127.0.0.1%3A4200
        //        &response_type=code
        //        &scope=openid%20message.read%20message.write
        //        &nonce=3f78c839561b11ae7c5d4eda644d4fb93fhLyHNDZ
        //        &state=f7ed2a0f8b03a4039f7838f8b0e1542dca7ZRZVjo
        //        &code_challenge=_nK6SO7EYw9Wu4TSP4UC9ePwdSFPuHOC-kek3zUsSP8
        //        &code_challenge_method=S256
        //
        // query consent:
        //   GET http://localhost:9000/issuer/oauth2/authorize...
        // --> returns consent page
        //
        // submit consent:
        //   POST http://localhost:9000/issuer/oauth2/authorize
        //     payload: client_id=public-client&state=XjpsB89Bfk80Es_OR3905ZZuPtniKYAxDvfdlsthU_I%3D&scope=message.read&scope=message.write
        // --> redirect to
        //     http://127.0.0.1:4200
        //        ?code=LVrIIxC9MybcJNwRgzDO7bARUIn2oInS71pAqo3_yQ3JIV5BOunzE6SVlX_w1m_KeZYVn1rwKqevJodrT8ONqXut30soZhs5zEi2t4ACxmuEMfUNYd4YWck30fyjpLE6
        //        &state=f7ed2a0f8b03a4039f7838f8b0e1542dca7ZRZVjo
        //
        // submit code:
        //   GET http://127.0.0.1:4200/
        //     ?code=LVrIIxC9MybcJNwRgzDO7bARUIn2oInS71pAqo3_yQ3JIV5BOunzE6SVlX_w1m_KeZYVn1rwKqevJodrT8ONqXut30soZhs5zEi2t4ACxmuEMfUNYd4YWck30fyjpLE6
        //     &state=f7ed2a0f8b03a4039f7838f8b0e1542dca7ZRZVjo
        //

        // Log in
        this.webClient.options.isThrowExceptionOnFailingStatusCode = false
        this.webClient.options.isRedirectEnabled = false

        // call login page
        assertThat(webClient.cookieManager.cookies.size).isEqualTo(0)
        val loginPage = this.webClient.getPage<Page?>("/login")
        assertThat(loginPage.isHtmlPage).isTrue
        assertThat(loginPage.webResponse.statusCode).isEqualTo(200)
        // post login action
        val postLoginRedirect: Page = doSignIn(loginPage as HtmlPage, USERNAME, PASSWORD)
        assertThat(postLoginRedirect.webResponse.statusCode).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value())
        assertThat(postLoginRedirect.webResponse.getResponseHeaderValue("Location")).isEqualTo("/")
        assertThat(webClient.cookieManager.cookies.size).isEqualTo(1)

        // Request token as any proper oauth client would do
        // this might require a consent page
        val requestPage: Page = this.webClient.getPage( AUTHORIZATION_REQUEST)
        assertThat(requestPage.webResponse.statusCode).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value())
        val location: String = requestPage.webResponse.getResponseHeaderValue("Location")
        // assertThat(location).startsWith(REDIRECT_URI)
        assertThat(location).contains("code=")

    }

}
