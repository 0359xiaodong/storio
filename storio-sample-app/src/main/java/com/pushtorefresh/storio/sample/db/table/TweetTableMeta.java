package com.pushtorefresh.storio.sample.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sample.db.entity.Tweet;
import com.pushtorefresh.storio.sqlite.operation.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operation.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operation.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operation.get.GetResolver;
import com.pushtorefresh.storio.sqlite.operation.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operation.put.PutResolver;
import com.pushtorefresh.storio.sqlite.query.DeleteQuery;
import com.pushtorefresh.storio.sqlite.query.InsertQuery;
import com.pushtorefresh.storio.sqlite.query.Query;
import com.pushtorefresh.storio.sqlite.query.UpdateQuery;

public class TweetTableMeta {

    private TweetTableMeta() {
        throw new IllegalStateException("No instances please");
    }

    public static final String TABLE = "tweets";

    public static final String COLUMN_ID = "_id";

    /**
     * For example: "artem_zin" without "@"
     */
    public static final String COLUMN_AUTHOR = "author";

    /**
     * For example: "Check out StorIO — modern API for SQLiteDatabase & ContentResolver #androiddev"
     */
    public static final String COLUMN_CONTENT = "content";

    public static final PutResolver<Tweet> PUT_RESOLVER = new DefaultPutResolver<Tweet>() {
        @NonNull
        @Override
        protected InsertQuery mapToInsertQuery(@NonNull Tweet object) {
            return new InsertQuery.Builder()
                    .table(TABLE)
                    .build();
        }

        @NonNull
        @Override
        protected UpdateQuery mapToUpdateQuery(@NonNull Tweet tweet) {
            return new UpdateQuery.Builder()
                    .table(TABLE)
                    .where(COLUMN_ID + " = ?")
                    .whereArgs(tweet.id())
                    .build();
        }

        @NonNull
        @Override
        protected ContentValues mapToContentValues(@NonNull Tweet tweet) {
            final ContentValues contentValues = new ContentValues(3); // wow, such optimization

            contentValues.put(COLUMN_ID, tweet.id());
            contentValues.put(COLUMN_AUTHOR, tweet.author());
            contentValues.put(COLUMN_CONTENT, tweet.content());

            return contentValues;
        }
    };

    public static final GetResolver<Tweet> GET_RESOLVER = new DefaultGetResolver<Tweet>() {
        @NonNull
        @Override
        public Tweet mapFromCursor(@NonNull Cursor cursor) {
            return Tweet.newTweet(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
            );
        }
    };

    public static final DeleteResolver<Tweet> DELETE_RESOLVER = new DefaultDeleteResolver<Tweet>() {
        @NonNull
        @Override
        public DeleteQuery mapToDeleteQuery(@NonNull Tweet object) {
            return new DeleteQuery.Builder()
                    .table(TABLE)
                    .where(COLUMN_ID + " = ?")
                    .whereArgs(object.id())
                    .build();
        }
    };

    public static final Query QUERY_ALL = new Query.Builder()
            .table(TABLE)
            .build();
}
