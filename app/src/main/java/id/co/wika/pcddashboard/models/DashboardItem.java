package id.co.wika.pcddashboard.models;

import java.math.BigDecimal;

/**
 * Created by myyusuf on 12/25/16.
 */
public class DashboardItem {

    private String title;
    private BigDecimal ok;
    private BigDecimal op;
    private BigDecimal lsp;
    private BigDecimal lk;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getOk() {
        return ok;
    }

    public void setOk(BigDecimal ok) {
        this.ok = ok;
    }

    public BigDecimal getOp() {
        return op;
    }

    public void setOp(BigDecimal op) {
        this.op = op;
    }

    public BigDecimal getLsp() {
        return lsp;
    }

    public void setLsp(BigDecimal lsp) {
        this.lsp = lsp;
    }

    public BigDecimal getLk() {
        return lk;
    }

    public void setLk(BigDecimal lk) {
        this.lk = lk;
    }
}
