package com.lap.no21docssample.service.detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lap.no21docssample.service.FillTemplateFactory;
import com.lap.no21docssample.utils.ConstantUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class FillTemplateDemoService extends FillTemplateFactory {
    private static final Map<String, String[]> RADIO_BUTTON_OPTIONS = new HashMap<>();
    private static final Map<String, String[]> RADIO_BUTTON_REPLACE_DATA = new HashMap<>();
    Map<String, String> objectWithDataType = new HashMap<>();
    Map<String, String> objectWithDefaultData = new HashMap<>();
    Map<String, String> objectWithLabel = new HashMap<>();
    Map<String, String> shortKeys = new HashMap<>();

    @PostConstruct
    public void init() {
        // --- Declare variables Date ---
        objectWithDataType.put("naikin1_nengappi", ConstantUtils.DATE);
        objectWithDataType.put("naikin2_nengappi", ConstantUtils.DATE);
        objectWithDataType.put("zandaikin_nengappi", ConstantUtils.DATE);
        objectWithDataType.put("honkeiyaku_tekisetsu_nengappi", ConstantUtils.DATE);
        objectWithDataType.put("yushiriyo_naiyo3_yushishonin_shutokukijitsu", ConstantUtils.DATE);
        objectWithDataType.put("yushiriyo_naiyo2_yushishonin_shutokukijitsu", ConstantUtils.DATE);
        objectWithDataType.put("yushiriyo_naiyo1_yushishonin_shutokukijitsu", ConstantUtils.DATE);



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

        shortKeys.put("short_key", "jusetsu.jrb.baibaidaikin");


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
        Map<String, String> replacements = this.buildReplacementMap(data, objectWithDataType, objectWithLabel, RADIO_BUTTON_OPTIONS, RADIO_BUTTON_REPLACE_DATA,tableData);

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
                Map<String, String[][]> tableData = new HashMap<>();
                Map<String, String> replacements = this.buildReplacementMap(data, objectWithDataType, objectWithLabel, RADIO_BUTTON_OPTIONS, RADIO_BUTTON_REPLACE_DATA,tableData);

                return exportDataToWordFile(templateStream, replacements, objectWithDefaultData,tableData,objectWithLabel,shortKeys);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error loading data from URL: " + e.getMessage());
            }

    }




}