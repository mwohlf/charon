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
private const val REDIRECT_URI: String = "http://127.0.0.1:8080/authorized"

val AUTHORIZATION_REQUEST: String = UriComponentsBuilder
    .fromPath("/oauth2/authorize")
    .queryParam("response_type", "code")
    .queryParam("client_id", "messaging-client")
    .queryParam("scope", "openid")
    .queryParam("state", "some-state")
    .queryParam("redirect_uri", REDIRECT_URI)
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


fun doSignIn(page: HtmlPage, username: String, password: String): Page {
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
    fun whenLoginSuccessfulThenDisplayNotFoundError() {
        // there is redirect to login page happening here
        val loginPage: HtmlPage = this.webClient.getPage("/")

        assertLoginPage(loginPage)

        this.webClient.options.isThrowExceptionOnFailingStatusCode = false
        val postLoginPage: Page = doSignIn(loginPage, USERNAME, PASSWORD)
        val signInResponse: WebResponse = postLoginPage.webResponse
        assertThat(signInResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())    // there is no "default" index page
    }

    @Test
    fun whenLoginFailsThenDisplayBadCredentials() {
        // there is redirect to login page happening here
        val loginPage: HtmlPage = this.webClient.getPage("/");

        val loginErrorPage: Page = doSignIn(loginPage, USERNAME, "wrong-password")
        assertThat(loginPage.isHtmlPage).isTrue

        val alert: HtmlElement = (loginErrorPage as HtmlPage ).querySelector("div[role=\"alert\"]");
        assertThat(alert).isNotNull
        assertThat(alert.textContent).isEqualTo("Bad credentials")
    }

    @Test
    fun whenLoggingInAndRequestingTokenThenRedirectsToClientApplication() {
        // Log in
        this.webClient.options.isThrowExceptionOnFailingStatusCode = false
        this.webClient.options.isRedirectEnabled = false

        val loginPage = this.webClient.getPage<Page?>("/login")
        assertThat(loginPage.isHtmlPage).isTrue
        assertThat(loginPage.webResponse.statusCode).isEqualTo(200)

        val postLoginRedirect = doSignIn(loginPage as HtmlPage, USERNAME, PASSWORD)
        assertThat(postLoginRedirect.webResponse.statusCode).isEqualTo(301)
        assertThat(postLoginRedirect.webResponse.getResponseHeaderValue("Location")).isEqualTo("/")

        // Request token as any proper oauth client would do
        val requestPage: HtmlPage = this.webClient.getPage(AUTHORIZATION_REQUEST)
        val response = requestPage.webResponse

        assertThat(response.statusCode).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value())
        val location: String = response.getResponseHeaderValue("location")
        assertThat(location).startsWith(REDIRECT_URI)
        assertThat(location).contains("code=")

    }

}
