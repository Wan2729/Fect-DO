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
        jumlahSoalan = 3;

        soalan = "ڤرکاتاءن کيميا براصل دري ڤرکاتاءن ________.";
        jawapan = "عرب";
        senaraiJawapan = new String[]{"عرب", "يوناني", "قدح", "جاوا"};
        senaraiSoalan[0] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "ساتو فکتور يڠ دتتڤکن سڤنجڠ ايکسڤيريمن.";
        jawapan = "دمالرکن";
        senaraiJawapan = new String[]{"دمانيڤولاسي", "دمالرکن", "برݢرق بالس", "دمنسوهکن"};
        senaraiSoalan[1] = new Soalan(soalan, jawapan, senaraiJawapan);

        soalan = "اڤکه اکتيۏيتي يڠ واجر دلاکوکن ترهادڤ سموا جنيس کمالڠن؟";
        jawapan = "لاڤورن";
        senaraiJawapan = new String[]{"باسوه مات", "کلوار معمل", "ݢڠݢم دان تاريق", "لاڤورن"};
        senaraiSoalan[2] = new Soalan(soalan, jawapan, senaraiJawapan);
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
