package com.example.demo.extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GlobalExtension implements BeforeAllCallback, BeforeEachCallback {
    ExtensionContext extensionContext;
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("BeforeAll custom dev ");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String nameMethod = extensionContext.getTestMethod().toString();
        System.out.println("I have created you " + nameMethod.substring(nameMethod.lastIndexOf("SomeServicesTest."), nameMethod.indexOf("(")));
    }
}
