/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

/**
 *
 * @author Kruno
 */
public class MeteoPodatak {

    private String zipZahtjevani;
    private String zipVraceni;
    private String grad;
    private String temperatura;
    private String vlaga;
    private String tlak;
    private String vjetar_brzina;
    private String vjetar_smjer;
    private String vrem_zona;
    private String kisa_stopa;
    private String vrijeme_preuzimanja;
    private String broj_preuzimanja;

    public MeteoPodatak(String zipZahtjevani, String zipVraceni, String grad, String temperatura, String vlaga, String tlak, String vjetar_brzina, String vjetar_smjer, String vrem_zona, String kisa_stopa, String vrijeme_preuzimanja, String broj_preuzimanja) {
        this.zipZahtjevani = zipZahtjevani;
        this.zipVraceni = zipVraceni;
        this.grad = grad;
        this.temperatura = temperatura;
        this.vlaga = vlaga;
        this.tlak = tlak;
        this.vjetar_brzina = vjetar_brzina;
        this.vjetar_smjer = vjetar_smjer;
        this.vrem_zona = vrem_zona;
        this.kisa_stopa = kisa_stopa;
        this.vrijeme_preuzimanja = vrijeme_preuzimanja;
        this.broj_preuzimanja = broj_preuzimanja;
    }

    public MeteoPodatak() {
    }

    public String getVrijeme_preuzimanja() {
        return vrijeme_preuzimanja;
    }

    public void setVrijeme_preuzimanja(String vrijeme_preuzimanja) {
        this.vrijeme_preuzimanja = vrijeme_preuzimanja;
    }

    public String getZipZahtjevani() {
        return zipZahtjevani;
    }

    public void setZipZahtjevani(String zipZahtjevani) {
        this.zipZahtjevani = zipZahtjevani;
    }

    public String getZipVraceni() {
        return zipVraceni;
    }

    public void setZipVraceni(String zipVraceni) {
        this.zipVraceni = zipVraceni;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getVlaga() {
        return vlaga;
    }

    public void setVlaga(String vlaga) {
        this.vlaga = vlaga;
    }

    public String getTlak() {
        return tlak;
    }

    public void setTlak(String tlak) {
        this.tlak = tlak;
    }

    public String getVjetar_brzina() {
        return vjetar_brzina;
    }

    public void setVjetar_brzina(String vjetar_brzina) {
        this.vjetar_brzina = vjetar_brzina;
    }

    public String getVjetar_smjer() {
        return vjetar_smjer;
    }

    public void setVjetar_smjer(String vjetar_smjer) {
        this.vjetar_smjer = vjetar_smjer;
    }

    public String getVrem_zona() {
        return vrem_zona;
    }

    public void setVrem_zona(String vrem_zona) {
        this.vrem_zona = vrem_zona;
    }

    public String getKisa_stopa() {
        return kisa_stopa;
    }

    public void setKisa_stopa(String kisa_stopa) {
        this.kisa_stopa = kisa_stopa;
    }

    public String getBroj_preuzimanja() {
        return broj_preuzimanja;
    }

    public void setBroj_preuzimanja(String broj_preuzimanja) {
        this.broj_preuzimanja = broj_preuzimanja;
    }
}
