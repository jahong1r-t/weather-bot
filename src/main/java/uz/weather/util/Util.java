package uz.weather.util;

import static uz.weather.util.Button.*;

public interface Util {

    String[][] languageMenu = {{UZ, EN, RU}};

    String[][] mainMenuUz = {
            {today_uz, week_uz},
            {search_uz, subscribe_uz},
            {settings_uz}
    };
    String[][] mainMenuEn = {
            {today_en, week_en},
            {search_en, subscribe_en},
            {settings_en}
    };
    String[][] mainMenuRu = {
            {today_ru, week_ru},
            {search_ru, subscribe_ru},
            {settings_ru}
    };

    String[][] settingsPanelUz = {{changeLangUz, changeLocationUz}, {backUz}};
    String[][] settingsPanelEn = {{changeLangEn, changeLocationEn}, {backEn}};
    String[][] settingsPanelRu = {{changeLangRu, changeLocationRu}, {backRu}};
}
