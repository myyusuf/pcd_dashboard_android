package id.co.wika.pcddashboard.adapters;

/**
 * Created by myyusuf on 12/17/17.
 */

public class CashFlow {

    private String name;
    private double rkap;
    private double rencana;
    private double prognosa;
    private double realisasi;

    public CashFlow(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getRkap() {
        return rkap;
    }

    public void setRkap(double rkap) {
        this.rkap = rkap;
    }

    public double getRencana() {
        return rencana;
    }

    public void setRencana(double rencana) {
        this.rencana = rencana;
    }

    public double getPrognosa() {
        return prognosa;
    }

    public void setPrognosa(double prognosa) {
        this.prognosa = prognosa;
    }

    public double getRealisasi() {
        return realisasi;
    }

    public void setRealisasi(double realisasi) {
        this.realisasi = realisasi;
    }
}
