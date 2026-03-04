package ru.netology.web.steps;

import io.cucumber.java.ru.*;
import lombok.val;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboard;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void login(String login, String password) {
        open("http://localhost:9999");

        val loginPage = new LoginPage();
        val authInfo = new DataHelper.AuthInfo(login, password);
        val verificationPage = loginPage.validLogin(authInfo);

        val code = DataHelper.getVerificationCodeFor(authInfo);
        dashboard = verificationPage.validVerify(code);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void transfer(int amount, String fromCardNumber, int targetCardIndex) {

        val firstCard = DataHelper.getFirstCard();
        val secondCard = DataHelper.getSecondCard();

        val targetCard = (targetCardIndex == 1) ? firstCard : secondCard;
        val sourceCard = fromCardNumber.endsWith("0001") ? firstCard : secondCard;

        val transferPage = dashboard.selectCard(targetCard);

        dashboard = transferPage.validTransfer(
                amount,
                sourceCard.getNumber().replaceAll("\\s+", "")
        );
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен составлять {int} рублей")
    public void checkBalance(int cardIndex, int expectedBalance) {

        val firstCard = DataHelper.getFirstCard();
        val secondCard = DataHelper.getSecondCard();

        val card = (cardIndex == 1) ? firstCard : secondCard;

        int actualBalance = dashboard.getCardBalance(card);

        assertEquals(expectedBalance, actualBalance);
    }
}
