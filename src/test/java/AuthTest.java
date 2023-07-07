import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    @BeforeEach
    void setUpAll() { open("http://localhost:9999/");}

    @Test
    @DisplayName("Should successfully login registered user")
    void shouldSuccessfullyLoginRegisteredUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(exactText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Enter a non-existing user")
    void EnterANonExistingUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("blocked");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                    .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                    .shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Error message when logging in with a blocked user")
    void ErrorMessageWhenLoggingInWithABlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"))
                .shouldBe(visible, Duration.ofSeconds(10));
    }
    @Test
    @DisplayName("Logging in with the wrong username")
    void LoggingInWithTheWrongUsername() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(10));
    }
    @Test
    @DisplayName("Log in with the wrong password")
    void LogInWithTheWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(10));
    }
}
