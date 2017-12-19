package id.co.wika.pcddashboard.adapters;

import java.math.BigDecimal;

/**
 * Created by myyusuf on 12/17/17.
 */

public class BadProject {

    private String name;

    private double piutangUsaha;
    private double tagihanBruto;
    private double piutangRetensi;
    private double pdp;
    private double bad;

    public BadProject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPiutangUsaha() {
        return piutangUsaha;
    }

    public void setPiutangUsaha(double piutangUsaha) {
        this.piutangUsaha = piutangUsaha;
    }

    public double getTagihanBruto() {
        return tagihanBruto;
    }

    public void setTagihanBruto(double tagihanBruto) {
        this.tagihanBruto = tagihanBruto;
    }

    public double getPiutangRetensi() {
        return piutangRetensi;
    }

    public void setPiutangRetensi(double piutangRetensi) {
        this.piutangRetensi = piutangRetensi;
    }

    public double getPdp() {
        return pdp;
    }

    public void setPdp(double pdp) {
        this.pdp = pdp;
    }

    public double getBad() {
        return bad;
    }

    public void setBad(double bad) {
        this.bad = bad;
    }
}
