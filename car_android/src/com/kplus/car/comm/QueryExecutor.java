package com.kplus.car.comm;

import android.database.sqlite.SQLiteDatabase;

public interface QueryExecutor
{
	public void run(SQLiteDatabase database);
}
