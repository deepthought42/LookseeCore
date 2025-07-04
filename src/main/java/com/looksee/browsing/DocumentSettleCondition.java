package com.looksee.browsing;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Licensed under MIT License
 * Wraps a condition so that it returns only after the document state has settled for a given time, the default being 2 seconds. The
 * document is considered settled when the "document.readyState" field stays "complete" and the URL in the browser stops changing.
 */
public class DocumentSettleCondition<T> implements ExpectedCondition<T> {
    /**
     * The condition
     */
    private final ExpectedCondition<T> condition;

    /**
     * The settle time in milliseconds
     */
    private final long settleTimeInMillis;

    /**
     * The last complete time
     */
    private long lastComplete = 0L;

    /**
     * The last URL
     */
    private String lastUrl;

    /**
     * Constructs a {@link DocumentSettleCondition} object
     *
     * @param condition the condition
     * @param settleTimeInMillis the settle time in milliseconds
     */
    public DocumentSettleCondition(ExpectedCondition<T> condition, long settleTimeInMillis) {
        this.condition = condition;
        this.settleTimeInMillis = settleTimeInMillis;
    }

    /**
     * Constructs a {@link DocumentSettleCondition} object
     *
     * @param condition the condition
     */
    public DocumentSettleCondition(ExpectedCondition<T> condition) {
        this(condition, 2000L);
    }

    /**
     * Get the settle time in millis.
     * 
     * @return the settle time in millis
     */
    public long getSettleTime() {
        return settleTimeInMillis;
    }

    /**
     * Applies the condition to the driver
     *
     * @param driver the driver
     * @return the result of the condition
     */
    @Override
    public T apply(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            String currentUrl = driver.getCurrentUrl();
            String readyState = String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"));
            boolean complete = readyState.equalsIgnoreCase("complete");
            if (!complete) {
                lastComplete = 0L;
                return null;
            }

            if (lastUrl != null && !lastUrl.equals(currentUrl)) {
                lastComplete = 0L;
            }
            lastUrl = currentUrl;

            if (lastComplete == 0L) {
                lastComplete = System.currentTimeMillis();
                return null;
            }
            long settleTime = System.currentTimeMillis() - lastComplete;
            if (settleTime < this.settleTimeInMillis) {
                return null;
            }
        }
        return condition.apply(driver);
    }

    /**
     * Returns a string representation of the document settle condition
     *
     * @return a string representation of the document settle condition
     */
    @Override
    public String toString() {
        return "Document settle @" + settleTimeInMillis + "ms for " + condition;
    }
}
