package id.co.wika.pcddashboard.models;

import java.math.BigDecimal;

/**
 * Created by myyusuf on 12/25/16.
 */
public class UmurPiutangItem {

    private String title;
    private BigDecimal value1;
    private BigDecimal value2;
    private BigDecimal value3;
    private BigDecimal value4;
    private BigDecimal value5;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getValue1() {
        return value1;
    }

    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }

    public BigDecimal getValue2() {
        return value2;
    }

    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }

    public BigDecimal getValue3() {
        return value3;
    }

    public void setValue3(BigDecimal value3) {
        this.value3 = value3;
    }

    public BigDecimal getValue4() {
        return value4;
    }

    public void setValue4(BigDecimal value4) {
        this.value4 = value4;
    }

    public BigDecimal getValue5() {
        return value5;
    }

    public void setValue5(BigDecimal value5) {
        this.value5 = value5;
    }

    public BigDecimal getTotal() {
        return getValue1().add(getValue2()).add(getValue3()).add(getValue4()).add(getValue5());
    }
}
