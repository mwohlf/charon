package net.wohlfart.charon

import com.gargoylesoftware.htmlunit.Page
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.WebResponse
import com.gargoylesoftware.htmlunit.html.HtmlButton
import com.gargoylesoftware.htmlunit.html.HtmlInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner


fun assertLoginPage(page: HtmlPage) {
    assertThat(page.url.toString()).endsWith("/login")

    val usernameInput: HtmlInput = page.querySelector("input[name=\"username\"]")
    val passwordInput: HtmlInput = page.querySelector("input[name=\"password\"]")
    val signInButton: HtmlButton = page.querySelector("button")

    assertThat(usernameInput).isNotNull
    assertThat(passwordInput).isNotNull
    assertThat(signInButton.textContent).isEqualTo("Sign in")
}


fun <P : Page> signIn(page: HtmlPage, username: String, password: String): P {
    val usernameInput: HtmlInput = page.querySelector("input[name=\"username\"]");
    val passwordInput: HtmlInput = page.querySelector("input[name=\"password\"]");
    val signInButton: HtmlButton = page.querySelector("button");

    usernameInput.type(username);
    passwordInput.type(password);
    return signInButton.click();
}


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DefaultAuthorizationServerApplicationTests {

    @Autowired
    lateinit var webClient: WebClient

    @Before
    fun setUp() {
        this.webClient.options.isThrowExceptionOnFailingStatusCode = true
        this.webClient.options.isRedirectEnabled = true
        this.webClient.cookieManager.clearCookies()    // log out
    }

    @Test
    fun whenLoginSuccessfulThenDisplayNotFoundError() {
        val rootPage: HtmlPage = this.webClient.getPage("/")

        assertLoginPage(rootPage)

        this.webClient.options.isThrowExceptionOnFailingStatusCode = false
        val postLoginPage: Page = signIn(rootPage, "user1", "s3cr37")
        val signInResponse: WebResponse = postLoginPage.getWebResponse()
        assertThat(signInResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())    // there is no "default" index page
    }

}
