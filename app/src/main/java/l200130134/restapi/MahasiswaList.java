package l200130134.restapi;

/**
 * Created by User-PRO on 14/07/2017.
 */

public class MahasiswaList {

    private String nim;
    private String nama;
    private String jenjang;
    private String pt;
    private String prodi;


    public MahasiswaList(String nim, String nama, String jenjang, String pt, String prodi) {
        this.nim = nim;
        this.nama = nama;
        this.jenjang = jenjang;
        this.pt = pt;
        this.prodi = prodi;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenjang() {
        return jenjang;
    }

    public void setJenjang(String jenjang) {
        this.jenjang = jenjang;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

}
