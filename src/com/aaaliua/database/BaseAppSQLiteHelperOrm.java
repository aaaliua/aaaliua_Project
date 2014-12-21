package com.aaaliua.database;

import java.sql.SQLException;

import com.aaaliua.entity.UserEntity;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;


public class BaseAppSQLiteHelperOrm extends BaseSQLiteHelperOrm {

	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(connectionSource, UserEntity.class);
		} catch (SQLException e) {
		}
	}

	@Override
	public void dropTable() {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(connectionSource, UserEntity.class, true);
		} catch (SQLException e) {
		}
	}

}
