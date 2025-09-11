package com.lap.no21docssample.service.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lap.no21docssample.service.FillTemplateFactory;
import com.lap.no21docssample.utils.ConstantUtils;
import jakarta.annotation.PostConstruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class FillTemplateDemoService extends FillTemplateFactory {
    private static final Map<String, String[]> RADIO_BUTTON_OPTIONS = new HashMap<>();
    private static final Map<String, String[]> RADIO_BUTTON_REPLACE_DATA = new HashMap<>();
    Map<String, String> objectWithDataType = new HashMap<>();
    Map<String, String> objectWithDefaultData = new HashMap<>();
    Map<String, String> objectWithLabel = new HashMap<>();
    Map<String, String> shortKeys = new HashMap<>();
    Map<String, String> dateWithCheckBox = new HashMap<>();
    Map<String, String> dateMonthYearWithCheckBox = new HashMap<>();

    @PostConstruct
    public void init() {
        // --------------------- DATE AND CHECKBOX ---------------------


        dateWithCheckBox.put("baikei.bpm.zandaikin_nengappi", "baikei.bpm.zandaikin_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bsc.honkeiyaku_tekisetsu_nengappi", "baikei.bsc.honkeiyaku_tekisetsu_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bes.kigen_nengappi", "baikei.bes.kigen_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bes.shuruigashakuchiken_kigen", "baikei.bes.shuruigashakuchiken_kigen_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuchiken_setteikeiyakusho", "baikei.bes.teikishakuchiken_setteikeiyakusho_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuchiken_kakuningoisho", "baikei.bes.teikishakuchiken_kakuningoisho_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuteiken_jotokeiyakusho", "baikei.bes.teikishakuteiken_jotokeiyakusho_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuchiken_koseishosho", "baikei.bes.teikishakuchiken_koseishosho_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuteiken_kakuninbi", "baikei.bes.teikishakuteiken_kakuninbi_checkbox");
        dateWithCheckBox.put("baikei.bes.teikishakuteiken_kigeni", "baikei.bes.teikishakuteiken_kigen_checkbox");

        dateWithCheckBox.put("baikei.bpm.naikin1_nengappi", "baikei.bpm.naikin1_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bpm.naikin2_nengappi", "baikei.bpm.naikin2_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bpm.yushiriyo_naiyo1_yushishonin_shutokukijitsu", "baikei.bpm.yushiriyo_naiyo1_yushishonin_shutokukijitsu_checkbox");
        dateWithCheckBox.put("baikei.bpm.yushiriyo_naiyo2_yushishonin_shutokukijitsu", "baikei.bpm.yushiriyo_naiyo2_yushishonin_shutokukijitsu_checkbox");
        dateWithCheckBox.put("baikei.bpm.yushiriyo_naiyo3_yushishonin_shutokukijitsu", "baikei.bpm.yushiriyo_naiyo3_yushishonin_shutokukijitsu_checkbox");

        dateWithCheckBox.put("jusetsu.jtm.setsumeishojyuryo_nengappi", "jusetsu.jtm.setsumeishojyuryo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jtm.jisha_menkyo_nengappi", "jusetsu.jtm.jisha_menkyo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jtm.urinushi_gyosha_menkyo_nengappi", "jusetsu.jtm.urinushi_gyosha_menkyo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jtm.tatakkengyoshajoho[0].menkyo_nengappi", "jusetsu.jtm.tatakkengyoshajoho[0].menkyo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jtm.tatakkengyoshajoho[1].menkyo_nengappi", "jusetsu.jtm.tatakkengyoshajoho[1].menkyo_nengappi_checkbox");

        dateWithCheckBox.put("jusetsu.jes.sokuryozumen_hakkobi", "jusetsu.jes.sokuryozumen_hakkobi_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.tokijiko_taisho1_shomeinengappi", "jusetsu.jrb.tokijiko_taisho1_shomeinengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.tokijiko_taisho2_shomeinengappi", "jusetsu.jrb.tokijiko_taisho2_shomeinengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kizontakuchi_bango_nengappi", "jusetsu.jrb.kizontakuchi_bango_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kyoka_bango_nengappi", "jusetsu.jrb.kyoka_bango_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kensa_bango_nengappi", "jusetsu.jrb.kensa_bango_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kokoku_bango_nengappi", "jusetsu.jrb.kokoku_bango_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.doroichishitei_nengappi", "jusetsu.jrb.doroichishitei_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.karikanchishitei_nengappi", "jusetsu.jrb.karikanchishitei_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kanchishobun_kokoku", "jusetsu.jrb.kanchishobun_kokoku_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.denki2_kanonashisetsu_ikkatsujuden_keiyakukikan", "jusetsu.jrb.denki2_kanonashisetsu_ikkatsujuden_keiyakukikan_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.kensazumisho_nengappi", "jusetsu.jrb.kensazumisho_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kakuninzumisho_nengappi", "jusetsu.jrb.kakuninzumisho_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kanrihi_taino_johokaiji_taino_nengappi", "jusetsu.jrb.kanrihi_taino_johokaiji_taino_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kanrihi_taino_nengappi", "jusetsu.jrb.kanrihi_taino_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.kanrihi_nengappi", "jusetsu.jrb.kanrihi_nengappi_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi2", "jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi2_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi", "jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.shuzentsumitatekin_taino_nengappi", "jusetsu.jrb.shuzentsumitatekin_taino_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.shuzentsumitatekin_shuzen_tsumitatekin_nengappi", "jusetsu.jrb.shuzentsumitatekin_shuzen_tsumitatekin_nengappi_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.shikichi_shakuchiken_sonzokukikan_nengappi", "jusetsu.jrb.shikichi_shakuchiken_sonzokukikan_nengappi_checkbox");

        dateWithCheckBox.put("jusetsu.jrb.tetsukekaijo_nengappi", "jusetsu.jrb.tetsukekaijo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.yushiriyo_tokuyaku_kaijo_nengappi", "jusetsu.jrb.yushiriyo_tokuyaku_kaijo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.jotoshodaku_tokuyaku_kaijo_nengappi", "jusetsu.jrb.jotoshodaku_tokuyaku_kaijo_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.assen1_shodakushonin_shutokukijitsu", "jusetsu.jrb.assen1_shodakushonin_shutokukijitsu_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.assen2_shodakushonin_shutokukijitsu", "jusetsu.jrb.assen2_shodakushonin_shutokukijitsu_checkbox");
        dateWithCheckBox.put("jusetsu.jrb.assen3_shodakushonin_shutokukijitsu", "jusetsu.jrb.assen3_shodakushonin_shutokukijitsu_checkbox");
        dateWithCheckBox.put("jusetsu.jle.kikan_kaishi_nengappi", "jusetsu.jle.kikan_kaishi_nengappi_checkbox");
        dateWithCheckBox.put("jusetsu.jle.kikan_shuryo_nengappi", "jusetsu.jle.kikan_shuryo_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bpm.taishakukenjotoshodakukeiyaku_kaijokijitsu", "baikei.bpm.taishakukenjotoshodakukeiyaku_kaijokijitsu_checkbox");
        dateWithCheckBox.put("baikei.bpm.yushikeiyaku_kaijokijitsu", "baikei.bpm.yushikeiyaku_kaijokijitsu_checkbox");
        dateWithCheckBox.put("baikei.bpm.zandaikin_nengappi", "baikei.bpm.zandaikin_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bpm.futekigosenkinin_kikankisan_sentaku2_nengappi", "baikei.bpm.futekigosenkinin_kikankisan_sentaku2_nengappi_checkbox");
        dateWithCheckBox.put("baikei.bpm.tetsukekin_kaijokijitsu", "baikei.bpm.tetsukekin_kaijokijitsu_checkbox");



        dateMonthYearWithCheckBox.put("jusetsu.jes.tatemono_kenchikujikiu", "jusetsu.jes.tatemono_kenchikujikiu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jes.tatemono_zokaichikujikan", "jusetsu.jes.tatemono_zokaichikujikan_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jes.tatemono_senyububun_kenchikujiki", "jusetsu.jes.tatemono_senyububun_kenchikujiki_checkbox");

        dateMonthYearWithCheckBox.put("jusetsu.jrb.inyomizu1_seibiyotei_nengetsu", "jusetsu.jrb.inyomizu1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.denki1_seibiyotei_nengetsu", "jusetsu.jrb.denki1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.gasu1_seibiyotei_nengetsu", "jusetsu.jrb.gasu1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.osui1_seibiyotei_nengetsu", "jusetsu.jrb.osui1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.zatsuhaisui1_seibiyotei_nengetsu", "jusetsu.jrb.zatsuhaisui1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.amamizu1_seibiyotei_nengetsu", "jusetsu.jrb.amamizu1_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.inyomizu2_seibiyotei_nengetsu", "jusetsu.jrb.inyomizu2_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.gasu2_seibiyotei_nengetsu", "jusetsu.jrb.gasu2_seibiyotei_nengetsu_checkbox");
        dateMonthYearWithCheckBox.put("jusetsu.jrb.haisui2_seibiyotei_nengetsu", "jusetsu.jrb.haisui2_seibiyotei_nengetsu_checkbox");


        // --------------------- CHECKBOX ---------------------

        // --- Declare variables Checkbox ---
        objectWithDataType.put("hidari_torikeitai_chukaikeitai_baikai", ConstantUtils.CHECKBOX);
        objectWithDataType.put("hidari_torikeitai_chukaikeitai_dairi", ConstantUtils.CHECKBOX);
        objectWithDataType.put("hidari_torikeitai_chukaikeitai_urinushi", ConstantUtils.CHECKBOX);
        objectWithDataType.put("migi_torikeitai_chukaikeitai_baikai", ConstantUtils.CHECKBOX);
        objectWithDataType.put("migi_torikeitai_chukaikeitai_dairi", ConstantUtils.CHECKBOX);
        objectWithDataType.put("migi_torikeitai_chukaikeitai_urinushi", ConstantUtils.CHECKBOX);

        // --- Declare variables default Checkbox  ---
        objectWithDefaultData.put("hidari_torikeitai_chukaikeitai_baikai", ConstantUtils.STRING_UNCHECKED);
        objectWithDefaultData.put("hidari_torikeitai_chukaikeitai_dairi", ConstantUtils.STRING_UNCHECKED);
        objectWithDefaultData.put("hidari_torikeitai_chukaikeitai_urinushi", ConstantUtils.STRING_UNCHECKED);
        objectWithDefaultData.put("migi_torikeitai_chukaikeitai_baikai", ConstantUtils.STRING_UNCHECKED);
        objectWithDefaultData.put("migi_torikeitai_chukaikeitai_dairi", ConstantUtils.STRING_UNCHECKED);
        objectWithDefaultData.put("migi_torikeitai_chukaikeitai_urinushi", ConstantUtils.STRING_UNCHECKED);


        // --------------------- RADIOBUTTON ---------------------

        // --- Declare variables RadioButton  ---
        objectWithDataType.put("hidari_torikeitai_torihikikeitai", ConstantUtils.RADIOBUTTON);
        objectWithDataType.put("migi_torikeitai_torihikikeitai", ConstantUtils.RADIOBUTTON);
        objectWithDataType.put("tochibaibaitaishomenseki_sentaku", ConstantUtils.RADIOBUTTON);
        objectWithDataType.put("iyakukingaku_sentaku", ConstantUtils.RADIOBUTTON);
        objectWithDataType.put("hikiwatashibi_sentaku", ConstantUtils.RADIOBUTTON);

        // --- Declare variables default RadioButton  ---
        objectWithDefaultData.put("hidari_torikeitai_torihikikeitai", "☐ 売買 \n ☐ 交換");
        objectWithDefaultData.put("migi_torikeitai_torihikikeitai", "☐ 売買 \n ☐ 交換");
        objectWithDefaultData.put("tochibaibaitaishomenseki_sentaku", " ☐ 1. 登記簿（公簿）面積による \n ☐ 2. 実測面積による \n ☐ 3.");
        objectWithDefaultData.put("iyakukingaku_sentaku", "☐ 1.手付金の額 \n☐ 2.売買代金の相当額 \n☐ 3.円");
        objectWithDefaultData.put("hikiwatashibi_sentaku", "☐ 1．売買代金全額受領日\n" + "☐ 2．");


        objectWithDefaultData.put("jusetsu.jes.jukyo_hyoji","");
        objectWithDefaultData.put( "jusetsu.jes.tatemono_jushohyoji","");
        objectWithDefaultData.put("jusetsu.jes.ittiotatemono_chibanmae","");
        objectWithDefaultData.put("jusetsu.jes.ittiotatemono_chibanato","");
        objectWithDefaultData.put("jusetsu.jes.tochi1[0].chibanmae","");
        objectWithDefaultData.put("jusetsu.jes.tochi1[0].chibanato","");


        // --- Declare Option RadioButton  ---
        RADIO_BUTTON_OPTIONS.put("hidari_torikeitai_torihikikeitai", new String[]{"売買", "交換"});
        RADIO_BUTTON_OPTIONS.put("migi_torikeitai_torihikikeitai", new String[]{"売買", "交換"});
        RADIO_BUTTON_OPTIONS.put("tochibaibaitaishomenseki_sentaku", new String[]{
                "1. 登記簿（公簿）面積による",
                "2. 実測面積による",
                "3. %s "
        });
        RADIO_BUTTON_OPTIONS.put("hikiwatashibi_sentaku", new String[]{
                "1．売買代金全額受領日" ,
                        "2．%s"
        });

        RADIO_BUTTON_OPTIONS.put("iyakukingaku_sentaku", new String[]{
                "1. 手付金の額",
                "2. 売買代金の %s％相当額",
                "3. %s 円"
        });

        // --- Declare variables need replace Data RadioButton  ---
        RADIO_BUTTON_REPLACE_DATA.put("iyakukingaku_sentaku", new String[]{"iyakukingaku_sotogaku", "iyakukingaku3_nyuryoku"});
        RADIO_BUTTON_REPLACE_DATA.put("tochibaibaitaishomenseki_sentaku", new String[]{"tochibaibaitaishomenseki_sentaku3_nyuryoku"});
        RADIO_BUTTON_REPLACE_DATA.put("hikiwatashibi_sentaku", new String[]{"hikiwatashibi2_nyuryoku"});

        // --- Block futekigosenkinin_kikankisan_sentaku  ---
        objectWithDataType.put("futekigosenkinin_kikankisan_sentaku", ConstantUtils.RADIOBUTTON);
        objectWithDefaultData.put("futekigosenkinin_kikankisan_sentaku",
                "☐ 1．第7条第2項の引渡完了日\n" +
                "☐ 2．住宅新築請負契約の請負人から売主への引渡しの時（）\n" +
                "☐ 3．");

        RADIO_BUTTON_OPTIONS.put("futekigosenkinin_kikankisan_sentaku", new String[]{
                "1．第7条第2項の引渡完了日" ,
                "2．住宅新築請負契約の請負人から売主への引渡しの時（%s）" ,
                "3．％s"
        });

        RADIO_BUTTON_REPLACE_DATA.put("futekigosenkinin_kikankisan_sentaku", new String[]{"futekigosenkinin_kikankisan_sentaku2_nengappi","futekigosenkinin_kikankisan_sentaku3_nyuryoku"});


        // --- Block futekigosenkinin_kikankisan_sentaku  ---
        objectWithDataType.put("seisantaishoutochi", ConstantUtils.RADIOBUTTON);
        objectWithDefaultData.put("seisantaishoutochi",
                        "☐ 1．私道負担（道路境界線後部分を含む）のない場合、上記土地全体\n" +
                        "☐ 2．私道負担（道路境界線後部分を含む）のある場合、それを除く土地部分\n" +
                        "☐ 3．");

        RADIO_BUTTON_OPTIONS.put("seisantaishoutochi", new String[]{
               "1．私道負担（道路境界線後部分を含む）のない場合、上記土地全体" ,
                       "2．私道負担（道路境界線後部分を含む）のある場合、それを除く土地部分",
                       "3．%s"
        });

        RADIO_BUTTON_REPLACE_DATA.put("seisantaishoutochi", new String[]{"seisantaishoutochi_sentaku3nyuryoku"});



        // --------------------- OBJECT WITH LABEL ---------------------


        objectWithLabel.put("label:jusetsu.jrb.baibaidaikin", "%s:媒介");
        objectWithLabel.put("label:short_key", "%s:媒介");
        objectWithLabel.put("label:baikei.bes.tochi", "Tochi: \n %s");

        // --------------------- SHORT KEY OBJECT WITH LABEL ---------------------

        shortKeys.put("M007-01_KEY1", "jusetsu.jtm.jisha_shogo");
        shortKeys.put("M007-01_KEY2", "jusetsu.jtm.tatakkengyoshajoho[0].shogo");
        shortKeys.put("M005-02_KEY3", "baikei.bpm.zandaikin_nengappi");
        shortKeys.put("M005-03_KEY1", "baikei.bsc.honkeiyaku_tekisetsu_nengappi");
        shortKeys.put("M007-02_KEY1", "jusetsu.jes.jukyo_hyoji");
        shortKeys.put("M007-02_KEY2", "jusetsu.jes.tatemono_jushohyoji");
        shortKeys.put("M007-02_KEY3", "jusetsu.jes.ittiotatemono_chibanmae");
        shortKeys.put("M007-02_KEY4", "jusetsu.jes.ittiotatemono_chibanato");
        shortKeys.put("M007-09_KEY1", "jusetsu.jrb.kingaku1");
        shortKeys.put("M007-02_KEY8", "jusetsu.jes.ittiotatemono_shozai");
        shortKeys.put("M007-02_KEY9", "jusetsu.jes.tatemonomeisho");
        shortKeys.put("M007-01_KEY3", "jusetsu.jtm.jisha_shutaru_jimusho_shozaichi");
        shortKeys.put("M007-01_KEY4", "jusetsu.jtm.jisha_shogo");
        shortKeys.put("M007-02_KEY10", "jusetsu.jes.tochi1_gokeimenseki");
        shortKeys.put("M007-02_KEY13", "jusetsu.jes.tatemono_yukamenseki_gokei");
        shortKeys.put("M007-09_KEY2", "jusetsu.jrb.baibaidaikin");
        shortKeys.put("M007-02_KEY14", "jusetsu.jes.senyububun_meisho");
        shortKeys.put("M005-02_KEY1", "baikei.bpm.hikiwatashibi_sentaku");
        shortKeys.put("M005-02_KEY2", "baikei.bpm.hikiwatashibi2_nyuryoku");
        shortKeys.put("M007-02_KEY7", "jusetsu.jes.tochi1[0].shozai");
        shortKeys.put("M007-02_KEY5", "jusetsu.jes.tochi1[0].chibanmae");
        shortKeys.put("M007-02_KEY6", "jusetsu.jes.tochi1[0].chibanato");
        shortKeys.put("M007-02_KEY11", "jusetsu.jes.shakuchitaisho_menseki");
        shortKeys.put("M007-02_KEY12", "jusetsu.jes.ittiotatemono_nobetokomenseki");

        shortKeys.put("M005-01_KEY1", "baikei.bes.kigen_nengappi");
        shortKeys.put("M005-01_KEY2", "baikei.bes.shuruigashakuchiken_kigen");
        shortKeys.put("M005-01_KEY3", "baikei.bes.teikishakuchiken_setteikeiyakusho");
        shortKeys.put("M005-01_KEY4", "baikei.bes.teikishakuchiken_kakuningoisho");
        shortKeys.put("M005-01_KEY5", "baikei.bes.teikishakuteiken_jotokeiyakusho");
        shortKeys.put("M005-01_KEY6", "baikei.bes.teikishakuchiken_koseishosho");
        shortKeys.put("M005-01_KEY7", "baikei.bes.teikishakuteiken_kakuninbi");
        shortKeys.put("M005-01_KEY8", "baikei.bes.teikishakuteiken_kigeni");

        shortKeys.put("M005-02_KEY4", "baikei.bpm.naikin1_nengappi");
        shortKeys.put("M005-02_KEY5", "baikei.bpm.naikin2_nengappi");
        shortKeys.put("M005-02_KEY6", "baikei.bpm.yushiriyo_naiyo1_yushishonin_shutokukijitsu");
        shortKeys.put("M005-02_KEY7", "baikei.bpm.yushiriyo_naiyo2_yushishonin_shutokukijitsu");
        shortKeys.put("M005-02_KEY8", "baikei.bpm.yushiriyo_naiyo3_yushishonin_shutokukijitsu");

        shortKeys.put("M007-01_KEY5", "jusetsu.jtm.setsumeishojyuryo_nengappi");
        shortKeys.put("M007-01_KEY6", "jusetsu.jtm.jisha_menkyo_nengappi");
        shortKeys.put("M007-01_KEY7", "jusetsu.jtm.urinushi_gyosha_menkyo_nengappi");
        shortKeys.put("M007-01_KEY8", "jusetsu.jtm.tatakkengyoshajoho[0].menkyo_nengappi");
        shortKeys.put("M007-01_KEY9", "jusetsu.jtm.tatakkengyoshajoho[1].menkyo_nengappi");

        shortKeys.put("M007-02_KEY15", "jusetsu.jes.sokuryozumen_hakkobi");
        shortKeys.put("M007-02_KEY16", "jusetsu.jes.tatemono_kenchikujikiu");
        shortKeys.put("M007-02_KEY17", "jusetsu.jes.tatemono_zokaichikujikan");
        shortKeys.put("M007-02_KEY18", "jusetsu.jes.tatemono_senyububun_kenchikujiki");

        shortKeys.put("M007-04_KEY1", "jusetsu.jrb.tokijiko_taisho1_shomeinengappi");
        shortKeys.put("M007-04_KEY2", "jusetsu.jrb.tokijiko_taisho2_shomeinengappi");

        shortKeys.put("M007-05_KEY1", "jusetsu.jrb.kizontakuchi_bango_nengappi");
        shortKeys.put("M007-05_KEY2", "jusetsu.jrb.kyoka_bango_nengappi");
        shortKeys.put("M007-05_KEY3", "jusetsu.jrb.kensa_bango_nengappi");
        shortKeys.put("M007-05_KEY4", "jusetsu.jrb.kokoku_bango_nengappi");
        shortKeys.put("M007-05_KEY5", "jusetsu.jrb.doroichishitei_nengappi");
        shortKeys.put("M007-05_KEY6", "jusetsu.jrb.karikanchishitei_nengappi");
        shortKeys.put("M007-05_KEY7", "jusetsu.jrb.kanchishobun_kokoku");

        shortKeys.put("M007-07_KEY1", "jusetsu.jrb.inyomizu1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY2", "jusetsu.jrb.denki1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY3", "jusetsu.jrb.gasu1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY4", "jusetsu.jrb.osui1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY5", "jusetsu.jrb.zatsuhaisui1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY6", "jusetsu.jrb.amamizu1_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY7", "jusetsu.jrb.inyomizu2_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY8", "jusetsu.jrb.gasu2_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY9", "jusetsu.jrb.haisui2_seibiyotei_nengetsu");
        shortKeys.put("M007-07_KEY10", "jusetsu.jrb.denki2_kanonashisetsu_ikkatsujuden_keiyakukikan");

        shortKeys.put("M007-08_KEY1", "jusetsu.jrb.kensazumisho_nengappi");
        shortKeys.put("M007-08_KEY2", "jusetsu.jrb.kakuninzumisho_nengappi");
        shortKeys.put("M007-08_KEY3", "jusetsu.jrb.kanrihi_taino_johokaiji_taino_nengappi");
        shortKeys.put("M007-08_KEY4", "jusetsu.jrb.kanrihi_taino_nengappi");
        shortKeys.put("M007-08_KEY5", "jusetsu.jrb.kanrihi_nengappi");
        shortKeys.put("M007-08_KEY6", "jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi2");
        shortKeys.put("M007-08_KEY7", "jusetsu.jrb.shuzentsumitatekin_tsumitategaku_nengappi");
        shortKeys.put("M007-08_KEY8", "jusetsu.jrb.shuzentsumitatekin_taino_nengappi");
        shortKeys.put("M007-08_KEY9", "jusetsu.jrb.shuzentsumitatekin_shuzen_tsumitatekin_nengappi");
        shortKeys.put("M007-08_KEY10", "jusetsu.jrb.shikichi_shakuchiken_sonzokukikan_nengappi");

        shortKeys.put("M007-10_KEY1", "jusetsu.jrb.tetsukekaijo_nengappi");
        shortKeys.put("M007-10_KEY2", "jusetsu.jrb.yushiriyo_tokuyaku_kaijo_nengappi");
        shortKeys.put("M007-10_KEY3", "jusetsu.jrb.jotoshodaku_tokuyaku_kaijo_nengappi");

        shortKeys.put("M007-14_KEY1", "jusetsu.jrb.assen1_shodakushonin_shutokukijitsu");
        shortKeys.put("M007-14_KEY2", "jusetsu.jrb.assen2_shodakushonin_shutokukijitsu");
        shortKeys.put("M007-14_KEY3", "jusetsu.jrb.assen3_shodakushonin_shutokukijitsu");

        shortKeys.put("M007-19_KEY1", "jusetsu.jle.kikan_kaishi_nengappi");
        shortKeys.put("M007-19_KEY2", "jusetsu.jle.kikan_shuryo_nengappi");

        shortKeys.put("M005-02_KEY9", "baikei.bpm.taishakukenjotoshodakukeiyaku_kaijokijitsu");
        shortKeys.put("M005-02_KEY10", "baikei.bpm.yushikeiyaku_kaijokijitsu");
        shortKeys.put("M005-02_KEY11", "baikei.bpm.futekigosenkinin_kikankisan_sentaku2_nengappi");
        shortKeys.put("M005-02_KEY12", "baikei.bpm.tetsukekin_kaijokijitsu");

    }

    public Map<String, Object> loadData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("data.json");
        return mapper.readValue(resource.getInputStream(), Map.class);
    }


    @Override
    public byte[] fillDocxTemplate(String  urlString) throws IOException {
        Map<String, Object> data = loadData();
        URL url = new URL(urlString);
        try (InputStream templateStream = url.openStream()) {
        Map<String, String[][]> tableData = new HashMap<>();
        Map<String, String> replacements = this.buildReplacementMap(data, objectWithDataType, objectWithLabel, RADIO_BUTTON_OPTIONS, RADIO_BUTTON_REPLACE_DATA,tableData,dateWithCheckBox,dateMonthYearWithCheckBox);

        return exportDataToWordFile(templateStream, replacements, objectWithDefaultData,tableData,objectWithLabel,shortKeys);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error loading data from URL: " + e.getMessage());
        }
    }

    @Override
    public byte[] fillDocxTemplateFile(MultipartFile file) throws IOException {

            Map<String, Object> data = loadData();
            try (InputStream templateStream = file.getInputStream()) {
                byte[] fileData = templateStream.readAllBytes();

                try (InputStream checkStream = new ByteArrayInputStream(fileData)) {
                    validateWordFile(checkStream);
                }

                Map<String, String[][]> tableData = new HashMap<>();
                Map<String, String> replacements = this.buildReplacementMap(data, objectWithDataType, objectWithLabel, RADIO_BUTTON_OPTIONS, RADIO_BUTTON_REPLACE_DATA,tableData,dateWithCheckBox,dateMonthYearWithCheckBox);

                return exportDataToWordFile(new ByteArrayInputStream(fileData), replacements, objectWithDefaultData,tableData,objectWithLabel,shortKeys);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error loading data from URL: " + e.getMessage());
            }



    }
    public static void validateWordFile(InputStream inputStream) throws IOException {
        try {
            byte[] data = inputStream.readAllBytes();
            try (OPCPackage pkg = OPCPackage.open(new ByteArrayInputStream(data))) {

                // Word phải có /word/document.xml
                if (pkg.getPartsByName(Pattern.compile("/word/document.xml")).isEmpty()) {
                    throw new IOException("Error loading data from URL: Not a valid Word file");
                }
            }
        } catch (Exception e) {
            throw new IOException("Error loading data from URL: " + e.getMessage(), e);
        }
    }




}