package gov.cipam.gi.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Database extends SQLiteAssetHelper{

    private static final String DB_NAME="brainstrom.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context,DB_NAME,null,DB_VER);

    }

}
