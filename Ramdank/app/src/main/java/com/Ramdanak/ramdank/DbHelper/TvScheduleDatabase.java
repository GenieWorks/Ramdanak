package com.Ramdanak.ramdank.DbHelper;

/**
 * Schema of the database
 * Created by root on 4/3/15.
 */
public final class TvScheduleDatabase {
    public TvScheduleDatabase() {}

    /**
     * The tv shows table
     */
    public static abstract class TvShows {
        public static final String TABLE_NAME = "SHOW";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TRAILER = "trailer";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_RATING_COUNT = "rating_count";
        public static final String COLUMN_NAME_RATING = "rating";


    }

    /**
     * channel table
     */
    public static abstract class TvChannels {
        public static final String TABLE_NAME = "CHANNEL";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_RATING_COUNT = "rating_count";
        public static final String COLUMN_NAME_RATING = "rating";

        public static final String COLUMN_NAME_FREQUENCY = "frequency";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_VERTICAL = "vertical";
    }

    /**
     * Image table
     */
    public static abstract class Images {
        public static final String TABLE_NAME = "IMAGE";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LOGO = "logo";
    }

    /**
     * channel_show table
     */
    public static abstract class TvRecord {
        public static final String TABLE_NAME = "TV_RECORD";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_SHOW_ID = "show_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
    }
}
