package com.synchrony.synchronyswat.utilities;

import lombok.extern.log4j.Log4j2;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Log4j2
public class StringHelper {
    public static String populateJSONSchemaWithData(String jsonSchema, Map<String, String> data){
        log.info("Populate JSON With data");
        String[] json = {jsonSchema};
        data.forEach((key, value) -> json[0] = json[0].replaceAll(key, value));
        return json[0];
    }

    public static String getDateFormat(Date date, String format){
        log.info("Format date to %s" + format);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}

