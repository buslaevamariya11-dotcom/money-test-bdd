package ru.netology.bdd.hooks;

import io.cucumber.java.Before;

import static com.codeborne.selenide.Selenide.open;

import ru.netology.web.config.WebDriverProvider;

public class Hooks {

    @Before
    public void setup() {
        WebDriverProvider.setup();
        open("http://localhost:9999");
    }
}