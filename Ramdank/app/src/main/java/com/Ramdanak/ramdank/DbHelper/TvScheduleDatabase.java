package com.Ramdanak.ramdank.DbHelper;

/**
 * Schema of the database
 * Created by mohamed on 4/3/15.
 */
public abstract class TvScheduleDatabase {

    /**
     * The tv shows table
     */
    public static abstract class TvShows {
        public static final String TABLE_NAME = "SHOW";

        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TRAILER = "trailer";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_PREVIOUS_RATING = "previous_rate";
        public static final String COLUMN_NAME_RATING_COUNT_1 = "rating_1";
        public static final String COLUMN_NAME_RATING_COUNT_2 = "rating_2";
        public static final String COLUMN_NAME_RATING_COUNT_3 = "rating_3";
        public static final String COLUMN_NAME_RATING_COUNT_4 = "rating_4";
        public static final String COLUMN_NAME_RATING_COUNT_5 = "rating_5";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_SERVER_ID = "server_id";
    }

    /**
     * channel table
     */
    public static abstract class TvChannels {
        public static final String TABLE_NAME = "CHANNEL";

        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_PREVIOUS_RATE = "previous_rate";
        public static final String COLUMN_NAME_RATING_COUNT_1 = "rating_1";
        public static final String COLUMN_NAME_RATING_COUNT_2 = "rating_2";
        public static final String COLUMN_NAME_RATING_COUNT_3 = "rating_3";
        public static final String COLUMN_NAME_RATING_COUNT_4 = "rating_4";
        public static final String COLUMN_NAME_RATING_COUNT_5 = "rating_5";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_SERVER_ID = "server_id";
    }

    /**
     * channel_show table
     */
    public static abstract class TvRecord {
        public static final String TABLE_NAME = "TV_RECORD";

        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME_SHOW_ID = "show_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_IS_REMINDED="is_reminded";
        public static final String COLUMN_NAME_SERVER_ID="server_id";
    }
}
