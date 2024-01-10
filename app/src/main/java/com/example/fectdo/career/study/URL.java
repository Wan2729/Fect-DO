package com.example.fectdo.career.study;

import java.util.ArrayList;
import java.util.List;

public class URL {
    public String link;
    public static final String SPM_SEMASA = "http://surl.li/pcfeb";
    public static final String SPM_BUKAN_SEMASA = "http://surl.li/pcfei";
    public static final String STPM_SAINS = "http://surl.li/pcfek";
    public static final String STPM_SASTERA ="http://surl.li/pcfeb";
    public static final String ASASI_SAINS ="https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/N";
    public static final String PERAKAUNAN = "https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/P";
    public static final String TESL = "https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/L";
    public static final String LAW = "https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/U";
    public static final String ASASI_KEJUTERAAN = "https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/K";
    public static final String MATRIKULASI_KUJUTERAAN = "https://online.mohe.gov.my/epanduan/ProgramPengajian/kategoriCalon/J";

    public URL(String link) {
        this.link = link;
    }

    String getUrl() {
        String linkWebsite = "";

        switch(link){

            case "SPM SEMASA":linkWebsite=SPM_SEMASA;
                break;

            case "SPM BUKAN SEMASA":linkWebsite=SPM_BUKAN_SEMASA;
                break;

            case "STPM SAINS":linkWebsite=STPM_SAINS;
                break;

            case "STPM SASTERA": linkWebsite=STPM_SASTERA;
                break;
            case "ASASI SAINS": linkWebsite=ASASI_SAINS;
                break;
            case "PERAKAUNAN": linkWebsite=PERAKAUNAN;
                break;
            case "TESL": linkWebsite=TESL;
                break;
            case "LAW": linkWebsite=LAW;
                break;
            case "ASASI KEJUTERAAN": linkWebsite=ASASI_KEJUTERAAN;
                break;
            case "MATRIKULASI KUJUTERAAN": linkWebsite=MATRIKULASI_KUJUTERAAN;
                break;

        }

        return linkWebsite;
    }

}
