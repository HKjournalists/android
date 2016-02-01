package com.client.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HbaseUtils {
	protected static final Logger log = LogManager.getLogger(HbaseUtils.class);
	private static final String TABLE_NAME = "log";
	private static final String CF_DEFAULT = "info";
	private static Connection connection = null;

	public static void createOrOverwrite(Admin admin, HTableDescriptor table)
			throws IOException {
		if (admin.tableExists(table.getTableName())) {
			admin.disableTable(table.getTableName());
			admin.deleteTable(table.getTableName());
		}
		admin.createTable(table);
	}

	public static void createTable() throws Exception {
		try (Admin admin = getConection().getAdmin()) {
			HTableDescriptor table = new HTableDescriptor(
					TableName.valueOf(TABLE_NAME));
			table.addFamily(new HColumnDescriptor(CF_DEFAULT));
			log.info("创建表");
			createOrOverwrite(admin, table);
			log.info("处理完毕");
		}
	}

	public static void modifyTable() throws Exception {
		try (Admin admin = getConection().getAdmin()) {
			TableName tableName = TableName.valueOf(TABLE_NAME);
			if (!admin.tableExists(tableName)) {
				log.info("找不到该表");
				return;
			}
			HTableDescriptor table = new HTableDescriptor(tableName);
			HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
			newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
			admin.addColumn(tableName, newColumn);
			HColumnDescriptor existingColumn = new HColumnDescriptor(CF_DEFAULT);
			existingColumn.setCompactionCompressionType(Algorithm.GZ);
			existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
			table.modifyFamily(existingColumn);
			admin.modifyTable(tableName, table);
			admin.disableTable(tableName);
			admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"));
			admin.deleteTable(tableName);
		}
	}

	public static void put(String rowinfo) throws Exception {
		Admin admin = getConection().getAdmin();
		TableName tableName = TableName.valueOf(TABLE_NAME);
		if (admin.tableExists(tableName)) {
			Table table = getConection().getTable(tableName);
			Put put = new Put(UUID.randomUUID().toString().getBytes("utf-8"));
			put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("info"),
					UUID.randomUUID().toString().getBytes("utf-8"));
			table.put(put);
		}
	}

	public static <T> List<T> scan() throws Exception {
		Admin admin = getConection().getAdmin();
		TableName tableName = TableName.valueOf(TABLE_NAME);
		if (admin.tableExists(tableName)) {
			Table table = getConection().getTable(tableName);
			Scan scan = new Scan();
			ResultScanner resultscanner = table.getScanner(scan);
			Iterator<Result> it = resultscanner.iterator();
			while (it.hasNext()) {
				Result result = it.next();
				Cell[] cells = result.rawCells();
				for (Cell cell : cells) {
					System.out.println(cell);
				}
			}
		}
		return null;
	}

	public static void get() throws Exception {
		Admin admin = getConection().getAdmin();
		TableName tableName = TableName.valueOf(TABLE_NAME);
		if (admin.tableExists(tableName)) {
			Table table = getConection().getTable(tableName);
			Get get = new Get("1".getBytes("utf-8"));
			Result result = table.get(get);
			System.out.println(result);
		}
	}

	public static Connection getConection() throws Exception {
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.master", "hbasemaster:16010");
		config.set("hbase.zookeeper.quorum", "hbasemaster");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		if (connection == null)
			connection = ConnectionFactory.createConnection(config);
		// for (int i = 0; i < 10000; i++)
		// put(connection);
		// get(connection);
		// scan(connection);
		return connection;
	}
}