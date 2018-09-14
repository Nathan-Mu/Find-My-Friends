package edu.monash.fit5046.fit5046a2;

import android.provider.BaseColumns;

/**
 * Created by nathan on 28/4/17.
 */

public class CountryDBStructure {
    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "country";
        public static final String COLUMN_ID = "countryId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ABBREVIATION = "abbreviation";
    }
}
