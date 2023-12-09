package com.example.fectdo.Soalan;

public class PengurusSoalan {
    private Soalan[] senaraiSoalan;
    private String[] senaraiJawapan;
    private int jumlahSoalan;

    public PengurusSoalan() {
        senaraiSoalan = new Soalan[10];
        senaraiJawapan = new String[4];
        jumlahSoalan = 0;
    }

    public void setSoalanLalai(){
        String soalan, jawapan;
        jumlahSoalan = 6;

        soalan = "Hitung 72 – 4(8 + 56 ÷ 8)";
        jawapan = "12";
        senaraiJawapan = new String[]{"12", "13", "14", "15"};
        senaraiSoalan[0] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "Bundarkan 75496 kepada ribu yang terdekat.";
        jawapan = "75000";
        senaraiJawapan = new String[]{"80000", "70000", "75000", "76000"};
        senaraiSoalan[1] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "Pak Samad mempunyai 3 orang anak dan Mak Salmah mempunyai 3 orang anak. Berapakan jumlah anak mereka?!!!!";
        jawapan = "3";
        senaraiJawapan = new String[]{"6", "5", "4", "3"};
        senaraiSoalan[2] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "Jika 5³ + 4 × 5 + x = 1043₅, cari nilai bagi x.";
        jawapan = "3";
        senaraiJawapan = new String[]{"4", "3", "1", "0"};
        senaraiSoalan[3] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "Cari nilai bagi digit 1 , dalam asas sepuluh, dalam nombor 4310₅.";
        jawapan = "5";
        senaraiJawapan = new String[]{"1", "5", "25", "125"};
        senaraiSoalan[4] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "100100₂ − 10100₂ = 10000₂";
        jawapan = "3";
        senaraiJawapan = new String[]{"10000₂", "10100₂", "100000₂", "100001₂"};
        senaraiSoalan[5] = new Soalan(soalan, jawapan, senaraiJawapan);
    }

    public String getSenaraiSoalan(int index) {
        return senaraiSoalan[index].getSoalan();
    }

    public String[] getSenaraiJawapan(int index) {
        return senaraiSoalan[index].getPilihanJawapan();
    }

    public int getJumlahSoalan() {
        return jumlahSoalan;
    }

    public String getJawapan(int index){
        return senaraiSoalan[index].getJawapan();
    }
}
