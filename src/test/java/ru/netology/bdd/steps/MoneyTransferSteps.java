package ru.netology.bdd.steps;

import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;

import lombok.val;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.DashboardPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboard;

    @Пусть("пользователь залогинен с логином {string} и паролем {string}")
    public void loginUser(String login, String password) {
        val loginPage = new LoginPage();
        val authInfo = new DataHelper.AuthInfo(login, password);
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboard = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты {string} на первую карту")
    public void doTransfer(int amount, String fromCardNumber) {

        String firstMasked = DataHelper.getMasked(DataHelper.getFirstCard().getCardNumber());

        val topUpPage = dashboard.selectCardToTopUp(firstMasked);
        dashboard = topUpPage.doValidTransfer(amount, fromCardNumber);
    }

    @Тогда("баланс первой карты должен быть {int} рублей")
    public void checkBalance(int expectedBalance) {

        String firstMasked = DataHelper.getMasked(DataHelper.getFirstCard().getCardNumber());
        int actualBalance = dashboard.getCardBalance(firstMasked);

        assertEquals(expectedBalance, actualBalance);
    }
}