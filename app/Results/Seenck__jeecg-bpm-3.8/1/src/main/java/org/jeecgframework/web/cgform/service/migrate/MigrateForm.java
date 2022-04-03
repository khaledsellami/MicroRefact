package org.jeecgframework.web.cgform.service.migrate;
 import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.jeecgframework.core.common.model.common.DBTable;
import org.jeecgframework.core.util.ReflectHelper;
import org.jeecgframework.web.cgform.entity.button.CgformButtonEntity;
import org.jeecgframework.web.cgform.entity.button.CgformButtonSqlEntity;
import org.jeecgframework.web.cgform.entity.cgformftl.CgformFtlEntity;
import org.jeecgframework.web.cgform.entity.enhance.CgformEnhanceJsEntity;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.exception.BusinessException;
import org.jeecgframework.web.cgform.pojo.config.CgFormFieldPojo;
import org.jeecgframework.web.cgform.pojo.config.CgFormHeadPojo;
import org.jeecgframework.web.cgform.pojo.config.CgFormIndexPojo;
import org.jeecgframework.web.cgform.util.PublicUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.NullConverter;
@Service("MigrateForm")
public class MigrateForm {

 private  Logger logger;

 static  InputStream inStream;

 private  String insert;

 private  String values;

 private  List<String> insertList;

 private  String basePath;


public void insertSQL(String tablename,StringBuffer ColumnName,StringBuffer ColumnValue){
    StringBuffer insertSQL = new StringBuffer();
    // 拼装sql语句
    insertSQL.append(insert).append(" ").append(tablename).append("(").append(ColumnName.toString()).append(")").append(values).append("(").append(ColumnValue.toString()).append(");");
    // 放到全局list里面
    insertList.add(insertSQL.toString());
}


public void zip(ZipOutputStream zos,String relativePath,String absolutPath){
    File file = new File(absolutPath);
    if (file.isDirectory()) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File tempFile = files[i];
            if (tempFile.isDirectory()) {
                String newRelativePath = relativePath + tempFile.getName() + File.separator;
                createZipNode(zos, newRelativePath);
                zip(zos, newRelativePath, tempFile.getPath());
            } else {
                zipFile(zos, tempFile, relativePath);
            }
        }
    } else {
        zipFile(zos, file, relativePath);
    }
}


public void processRow(ResultSet rs){
    inStream = rs.getBinaryStream(columnName);
}


public String generateUpdateSql(String tableName,Class<T> clazz,List<String> ignores){
    StringBuffer updateSql = new StringBuffer("update " + tableName + " set ");
    String updateProperties = "";
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
    for (PropertyDescriptor pd : pds) {
        if (null != ignores && ignores.size() > 0) {
            if (ignores.contains(pd.getName()))
                continue;
        }
        if (pd.getName().toLowerCase().equals("id")) {
            // || pd.getPropertyType().equals(List.class)
            continue;
        }
        if (pd.getWriteMethod() != null) {
            if (updateProperties.length() > 0) {
                updateProperties += ",";
            }
            updateProperties += underscoreName(pd.getName()) + "=:" + pd.getName();
        }
    }
    updateSql.append(updateProperties);
    updateSql.append(" where id=:id");
    org.jeecgframework.core.util.LogUtil.info("generate updateSql for " + clazz.getName() + ":" + updateSql.toString());
    return updateSql.toString();
}


public SqlParameterSource generateParameterMap(Object t,List<String> ignores){
    Map<String, Object> paramMap = new HashMap<String, Object>();
    ReflectHelper reflectHelper = new ReflectHelper(t);
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(t.getClass());
    for (PropertyDescriptor pd : pds) {
        if (null != ignores && ignores.contains(pd.getName())) {
            continue;
        }
        paramMap.put(pd.getName(), reflectHelper.getMethodValue(pd.getName()));
    }
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(paramMap);
    return sqlParameterSource;
}


public String underscoreName(String name){
    StringBuilder result = new StringBuilder();
    if (name != null && name.length() > 0) {
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            if (s.equals(s.toUpperCase())) {
                result.append("_");
                result.append(s.toLowerCase());
            } else {
                result.append(s);
            }
        }
    }
    return result.toString();
}


public List<DBTable> buildExportDbTableList(String ids,JdbcTemplate jdbcTemplate){
    // SQL语句列表
    List<DBTable> listTables = new ArrayList<DBTable>();
    listTables.clear();
    String ls_sql = "";
    String ls_tmpsql = "";
    String ls_subid = "";
    String subTable = "";
    List rowsList = null;
    List subRowsList = null;
    Map sqlMap = null;
    Map subSqlMap = null;
    // 获得指定的ID数据
    String[] idList = ids.split(",");
    for (String id : idList) {
        // 获得导出表单
        ls_sql = "select * from cgform_head where id='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_sql, CgFormHeadPojo.class, jdbcTemplate));
        // update-begin--Author:dangzhenghui  Date:20170309 for：TASK #1694 【功能缺陷】Jeecg online配置导入导出，缺少索引配置部分
        // 获得导出索引的字段
        ls_tmpsql = "select * from cgform_index where table_id='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgFormIndexPojo.class, jdbcTemplate));
        // update-end--Author:zhangdaihao  Date:20170309 for：TASK #1694 【功能缺陷】Jeecg online配置导入导出，缺少索引配置部分
        // 获得导出表单的字段
        ls_tmpsql = "select * from cgform_field where table_id='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgFormFieldPojo.class, jdbcTemplate));
        // 获得自定义按钮数据
        ls_tmpsql = "select * from cgform_button where form_id ='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformButtonEntity.class, jdbcTemplate));
        // 获得JS增强数据
        ls_tmpsql = "select * from cgform_enhance_js where form_id ='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformEnhanceJsEntity.class, jdbcTemplate));
        // 获得SQL增强数据
        ls_tmpsql = "select * from cgform_button_sql where form_id ='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformButtonSqlEntity.class, jdbcTemplate));
        // 获得模板数据
        ls_tmpsql = "select * from cgform_ftl where cgform_id ='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformFtlEntity.class, jdbcTemplate));
        // 获得上传文件数据
        ls_tmpsql = "select * from cgform_uploadfiles where cgform_id ='" + id + "'";
        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgUploadEntity.class, jdbcTemplate));
        rowsList = jdbcTemplate.queryForList(ls_sql);
        if (rowsList != null && rowsList.size() > 0) {
            sqlMap = (Map) rowsList.get(0);
            // 获得子表
            subTable = (String) sqlMap.get("sub_table_str");
            if (subTable != null && !"".equals(subTable)) {
                String[] subs = subTable.split(",");
                for (String sub : subs) {
                    // 获得导出子表表单
                    ls_tmpsql = "select * from cgform_head where table_name='" + sub + "'";
                    listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgFormHeadPojo.class, jdbcTemplate));
                    subRowsList = jdbcTemplate.queryForList(ls_tmpsql);
                    if (subRowsList != null && subRowsList.size() > 0) {
                        subSqlMap = (Map) subRowsList.get(0);
                        ls_subid = (String) subSqlMap.get("id");
                        // update-begin--Author:dangzhenghui  Date:20170309 for：TASK #1694 【功能缺陷】Jeecg online配置导入导出，缺少索引配置部分
                        // 获得导出子表索引
                        ls_tmpsql = "select * from cgform_index where table_id='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgFormIndexPojo.class, jdbcTemplate));
                        // update-end--Author:zhangdaihao  Date:20170309 for：TASK #1694 【功能缺陷】Jeecg online配置导入导出，缺少索引配置部分
                        // 获得导出子表字段
                        ls_tmpsql = "select * from cgform_field where table_id='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgFormFieldPojo.class, jdbcTemplate));
                        // 获得子表自定义按钮数据
                        ls_tmpsql = "select * from cgform_button where form_id ='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformButtonEntity.class, jdbcTemplate));
                        // 获得子表JS增强数据
                        ls_tmpsql = "select * from cgform_enhance_js where form_id ='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformEnhanceJsEntity.class, jdbcTemplate));
                        // 获得子表SQL增强数据
                        ls_tmpsql = "select * from cgform_button_sql where form_id ='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformButtonSqlEntity.class, jdbcTemplate));
                        // 获得子表模板数据
                        ls_tmpsql = "select * from cgform_ftl where cgform_id ='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgformFtlEntity.class, jdbcTemplate));
                        // 获得子表上传文件数据
                        ls_tmpsql = "select * from cgform_uploadfiles where cgform_id ='" + ls_subid + "'";
                        listTables.add(bulidDbTableFromSQL(ls_tmpsql, CgUploadEntity.class, jdbcTemplate));
                    }
                }
            }
        }
    }
    return listTables;
}


public void unzip(String zipFilePath,String targetPath){
    OutputStream os = null;
    InputStream is = null;
    ZipFile zipFile = null;
    try {
        zipFile = new ZipFile(zipFilePath);
        String directoryPath = "";
        if (null == targetPath || "".equals(targetPath)) {
            directoryPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
        } else {
            directoryPath = targetPath;
        }
        Enumeration entryEnum = zipFile.getEntries();
        if (null != entryEnum) {
            ZipEntry zipEntry = null;
            while (entryEnum.hasMoreElements()) {
                zipEntry = (ZipEntry) entryEnum.nextElement();
                if (zipEntry.isDirectory()) {
                    directoryPath = directoryPath + File.separator + zipEntry.getName();
                    org.jeecgframework.core.util.LogUtil.info(directoryPath);
                    continue;
                }
                if (zipEntry.getSize() > 0) {
                    // 文件
                    File targetFile = buildFile(directoryPath + File.separator + zipEntry.getName(), false);
                    os = new BufferedOutputStream(new FileOutputStream(targetFile));
                    is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[4096];
                    int readLen = 0;
                    while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                        os.write(buffer, 0, readLen);
                    }
                    os.flush();
                    os.close();
                } else {
                    // 空目录
                    buildFile(directoryPath + File.separator + zipEntry.getName(), true);
                }
            }
        }
    } catch (IOException ex) {
        throw ex;
    } finally {
        if (null != zipFile) {
            zipFile = null;
        }
        if (null != is) {
            is.close();
        }
        if (null != os) {
            os.close();
        }
    }
}


public void zipFile(ZipOutputStream zos,File file,String relativePath){
    ZipEntry entry = new ZipEntry(relativePath + file.getName());
    zos.putNextEntry(entry);
    InputStream is = null;
    try {
        is = new FileInputStream(file);
        int BUFFERSIZE = 2 << 10;
        int length = 0;
        byte[] buffer = new byte[BUFFERSIZE];
        while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
            zos.write(buffer, 0, length);
        }
        zos.flush();
        zos.closeEntry();
    } catch (IOException ex) {
        throw ex;
    } finally {
        if (null != is) {
            is.close();
        }
    }
}


public String createFile(HttpServletRequest request,String ids){
    String savePath = request.getSession().getServletContext().getRealPath("/") + basePath + "/" + ids + "_migrate.sql";
    File file = new File(savePath);
    if (!file.exists()) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.info("创建文件名失败！！");
            e.printStackTrace();
        }
    }
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
        fw = new FileWriter(file);
        bw = new BufferedWriter(fw);
        if (insertList.size() > 0) {
            for (int i = 0; i < insertList.size(); i++) {
                bw.append(insertList.get(i));
                bw.append("\n");
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return savePath;
}


public String getBlob(String id,String tableName,String columnName,JdbcTemplate jdbcTemplate){
    String ls_sql = "select " + columnName + " from " + tableName + " where id='" + id + "'";
    // 查询并获得输入流
    jdbcTemplate.query(ls_sql, new RowCallbackHandler() {

        public void processRow(ResultSet rs) throws SQLException {
            inStream = rs.getBinaryStream(columnName);
        }
    });
    // 读取流数据并转换成16进制字符串
    if (inStream != null) {
        StringBuffer readInBuffer = new StringBuffer();
        readInBuffer.append("0x");
        byte[] b = new byte[4096];
        try {
            for (; (inStream.read(b)) != -1; ) {
                readInBuffer.append(byte2HexStr(b));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ls_return = readInBuffer.toString().trim();
        if ("0x".equals(ls_return)) {
            ls_return = ls_return + "00";
        }
        return ls_return;
    } else {
        return "0x00";
    }
}


public File buildFile(String fileName,boolean isDirectory){
    File target = new File(fileName);
    if (isDirectory) {
        target.mkdirs();
    } else {
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
            target = new File(target.getAbsolutePath());
        }
    }
    return target;
}


public DBTable<T> bulidDbTableFromSQL(String sql,Class<T> clazz,JdbcTemplate jdbcTemplate){
    DBTable<T> dbTable = new DBTable<T>();
    dbTable.setTableName(PublicUtil.getTableName(sql));
    dbTable.setClass1(clazz);
    // -- update-begin author： xugj date:20160103  for: #851 controller 单元测试 升级spring 引起的变化
    List<T> dataList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz));
    // -- update-end author： xugj date:20160103  for: #851 controller 单元测试 升级spring 引起的变化
    dbTable.setTableData(dataList);
    return dbTable;
}


public String byte2HexStr(byte[] b){
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
        if (b[n] == 0)
            // 判断数据结束
            break;
        stmp = (Integer.toHexString(b[n] & 0XFF));
        if (stmp.length() == 1)
            hs = hs + "0" + stmp;
        else
            hs = hs + stmp;
    }
    return hs.toUpperCase();
}


public String generateInsertSql(String tableName,Class<T> clazz,List<String> ignores){
    StringBuffer insertSql = new StringBuffer("insert into " + tableName + "(");
    String tableField = "";
    String clazzProperties = "";
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
    for (PropertyDescriptor pd : pds) {
        if (null != ignores && ignores.size() > 0) {
            if (ignores.contains(pd.getName()))
                continue;
        }
        if (pd.getWriteMethod() != null) {
            if (tableField.length() > 0 && clazzProperties.length() > 0) {
                tableField += ",";
                clazzProperties += ",";
            }
            tableField += underscoreName(pd.getName());
            clazzProperties += ":" + pd.getName();
        }
    }
    insertSql.append(tableField);
    insertSql.append(") values(");
    insertSql.append(clazzProperties);
    insertSql.append(")");
    org.jeecgframework.core.util.LogUtil.info("generate insertSql for " + clazz.getName() + ":" + insertSql.toString());
    return insertSql.toString();
}


@Deprecated
public List<String> createSQL(String ids,JdbcTemplate jdbcTemplate){
    // SQL语句列表
    List<String> listSQL = new ArrayList<String>();
    listSQL.clear();
    // 获得指定的ID数据
    String[] idList = ids.split(",");
    String ls_sql = "";
    String ls_tmpsql = "";
    String ls_subid = "";
    String subTable = "";
    List rowsList = null;
    List subRowsList = null;
    Map sqlMap = null;
    Map subSqlMap = null;
    for (String id : idList) {
        // 获得导出表单
        ls_sql = "select * from cgform_head where id='" + id + "'";
        listSQL.add(ls_sql);
        // 获得导出表单的字段
        ls_tmpsql = "select * from cgform_field where table_id='" + id + "'";
        listSQL.add(ls_tmpsql);
        // 获得自定义按钮数据
        ls_tmpsql = "select * from cgform_button where form_id ='" + id + "'";
        listSQL.add(ls_tmpsql);
        // 获得JS增强数据
        ls_tmpsql = "select * from cgform_enhance_js where form_id ='" + id + "'";
        listSQL.add(ls_tmpsql);
        // 获得SQL增强数据
        ls_tmpsql = "select * from cgform_button_sql where form_id ='" + id + "'";
        listSQL.add(ls_tmpsql);
        // 获得模板数据
        ls_tmpsql = "select * from cgform_ftl where cgform_id ='" + id + "'";
        listSQL.add(ls_tmpsql);
        // 获得上传文件数据
        ls_tmpsql = "select * from cgform_uploadfiles where cgform_id ='" + id + "'";
        listSQL.add(ls_tmpsql);
        rowsList = jdbcTemplate.queryForList(ls_sql);
        if (rowsList != null && rowsList.size() > 0) {
            sqlMap = (Map) rowsList.get(0);
            // 获得子表
            subTable = (String) sqlMap.get("sub_table_str");
            if (subTable != null && !"".equals(subTable)) {
                String[] subs = subTable.split(",");
                for (String sub : subs) {
                    // 获得导出子表表单
                    ls_tmpsql = "select * from cgform_head where table_name='" + sub + "'";
                    listSQL.add(ls_tmpsql);
                    subRowsList = jdbcTemplate.queryForList(ls_tmpsql);
                    if (subRowsList != null && subRowsList.size() > 0) {
                        subSqlMap = (Map) subRowsList.get(0);
                        ls_subid = (String) subSqlMap.get("id");
                        // 获得导出子表字段
                        ls_tmpsql = "select * from cgform_field where table_id='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                        // 获得子表自定义按钮数据
                        ls_tmpsql = "select * from cgform_button where form_id ='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                        // 获得子表JS增强数据
                        ls_tmpsql = "select * from cgform_enhance_js where form_id ='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                        // 获得子表SQL增强数据
                        ls_tmpsql = "select * from cgform_button_sql where form_id ='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                        // 获得子表模板数据
                        ls_tmpsql = "select * from cgform_ftl where cgform_id ='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                        // 获得子表上传文件数据
                        ls_tmpsql = "select * from cgform_uploadfiles where cgform_id ='" + ls_subid + "'";
                        listSQL.add(ls_tmpsql);
                    }
                }
            }
        }
    }
    return listSQL;
}


public void getColumnNameAndColumeValue(List<String> listSQL,JdbcTemplate jdbcTemplate){
    if (listSQL.size() > 0) {
        insertList.clear();
        // 取消外键检查
        insertList.add("SET FOREIGN_KEY_CHECKS=0;");
        SqlRowSet sqlRowSet = null;
        String ls_id = "";
        for (int j = 0; j < listSQL.size(); j++) {
            // 逐条获取sql语句
            String sql = String.valueOf(listSQL.get(j));
            sqlRowSet = jdbcTemplate.queryForRowSet(sql);
            SqlRowSetMetaData sqlRsmd = sqlRowSet.getMetaData();
            // 获得表字段个数
            int columnCount = sqlRsmd.getColumnCount();
            // 获得表名称
            String tableName = sqlRsmd.getTableName(columnCount);
            if (StringUtils.isEmpty(tableName)) {
                tableName = PublicUtil.getTableName(sql);
            }
            String tableId = "";
            while (sqlRowSet.next()) {
                StringBuffer ColumnName = new StringBuffer();
                StringBuffer ColumnValue = new StringBuffer();
                for (int i = 1; i <= columnCount; i++) {
                    String value = sqlRowSet.getString(i);
                    if (value == null || "".equals(value)) {
                        value = "";
                    }
                    Map<String, String> fieldMap = new HashMap<String, String>();
                    fieldMap.put("name", sqlRsmd.getColumnName(i));
                    fieldMap.put("fieldType", String.valueOf(sqlRsmd.getColumnType(i)));
                    // 生成插入数据sql语句
                    if (i == 1) {
                        // 先生成删除指定ID语句，清除现有冲突数据
                        insertList.add("delete from " + tableName + " where " + sqlRsmd.getColumnName(i) + "='" + value + "';");
                        ColumnName.append(sqlRsmd.getColumnName(i));
                        ls_id = value;
                        tableId = value;
                        if (Types.CHAR == sqlRsmd.getColumnType(i) || Types.VARCHAR == sqlRsmd.getColumnType(i)) {
                            ColumnValue.append("'").append(value).append("',");
                        } else if (Types.SMALLINT == sqlRsmd.getColumnType(i) || Types.INTEGER == sqlRsmd.getColumnType(i) || Types.BIGINT == sqlRsmd.getColumnType(i) || Types.FLOAT == sqlRsmd.getColumnType(i) || Types.DOUBLE == sqlRsmd.getColumnType(i) || Types.NUMERIC == sqlRsmd.getColumnType(i) || Types.DECIMAL == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "0";
                            ColumnValue.append(value).append(",");
                        } else if (Types.DATE == sqlRsmd.getColumnType(i) || Types.TIME == sqlRsmd.getColumnType(i) || Types.TIMESTAMP == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "2000-01-01";
                            ColumnValue.append("'").append(value).append("',");
                        } else {
                            ColumnValue.append(value).append(",");
                        }
                    } else if (i == columnCount) {
                        ColumnName.append("," + sqlRsmd.getColumnName(i));
                        if (Types.CHAR == sqlRsmd.getColumnType(i) || Types.VARCHAR == sqlRsmd.getColumnType(i) || Types.LONGVARCHAR == sqlRsmd.getColumnType(i)) {
                            ColumnValue.append("'").append(value).append("'");
                        } else if (Types.SMALLINT == sqlRsmd.getColumnType(i) || Types.INTEGER == sqlRsmd.getColumnType(i) || Types.BIGINT == sqlRsmd.getColumnType(i) || Types.FLOAT == sqlRsmd.getColumnType(i) || Types.DOUBLE == sqlRsmd.getColumnType(i) || Types.NUMERIC == sqlRsmd.getColumnType(i) || Types.DECIMAL == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "0";
                            ColumnValue.append(value);
                        } else if (Types.DATE == sqlRsmd.getColumnType(i) || Types.TIME == sqlRsmd.getColumnType(i) || Types.TIMESTAMP == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "2000-01-01";
                            ColumnValue.append("'").append(value).append("'");
                        } else {
                            ColumnValue.append(value).append("");
                        }
                    } else {
                        ColumnName.append("," + sqlRsmd.getColumnName(i));
                        if (Types.CHAR == sqlRsmd.getColumnType(i) || Types.VARCHAR == sqlRsmd.getColumnType(i) || Types.LONGVARCHAR == sqlRsmd.getColumnType(i)) {
                            ColumnValue.append("'").append(value).append("'").append(",");
                        } else if (Types.SMALLINT == sqlRsmd.getColumnType(i) || Types.INTEGER == sqlRsmd.getColumnType(i) || Types.BIGINT == sqlRsmd.getColumnType(i) || Types.FLOAT == sqlRsmd.getColumnType(i) || Types.DOUBLE == sqlRsmd.getColumnType(i) || Types.NUMERIC == sqlRsmd.getColumnType(i) || Types.DECIMAL == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "0";
                            ColumnValue.append(value).append(",");
                        } else if (Types.DATE == sqlRsmd.getColumnType(i) || Types.TIME == sqlRsmd.getColumnType(i) || Types.TIMESTAMP == sqlRsmd.getColumnType(i)) {
                            if ("".equals(value))
                                value = "2000-01-01";
                            ColumnValue.append("'").append(value).append("',");
                        } else if (Types.BLOB == sqlRsmd.getColumnType(i) || Types.LONGVARCHAR == sqlRsmd.getColumnType(i) || Types.LONGNVARCHAR == sqlRsmd.getColumnType(i) || Types.BINARY == sqlRsmd.getColumnType(i) || Types.LONGVARBINARY == sqlRsmd.getColumnType(i) || Types.VARBINARY == sqlRsmd.getColumnType(i)) {
                            String ls_tmp = getBlob(ls_id, tableName, sqlRsmd.getColumnName(i), jdbcTemplate);
                            ColumnValue.append(ls_tmp).append(",");
                        } else {
                            ColumnValue.append(value).append(",");
                        }
                    }
                }
                // 拼装并放到全局list里面
                insertSQL(tableName, ColumnName, ColumnValue);
                if (tableName.equals("cgform_head")) {
                    // 设为未同步
                    insertList.add("update cgform_head set is_dbsynch='N' where id='" + tableId + "';");
                }
            }
        }
    }
}


public void createZipNode(ZipOutputStream zos,String relativePath){
    ZipEntry zipEntry = new ZipEntry(relativePath);
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
}


public void executeSQL(List<String> listSQL,JdbcTemplate jdbcTemplate){
    getColumnNameAndColumeValue(listSQL, jdbcTemplate);
}


public void generateXmlDataOutFlieContent(List<DBTable> dbTables,String parentDir){
    File file = new File(parentDir);
    if (!file.exists()) {
        buildFile(parentDir, true);
    }
    try {
        XStream xStream = new XStream();
        xStream.registerConverter(new NullConverter());
        xStream.processAnnotations(DBTable.class);
        FileOutputStream outputStream = new FileOutputStream(buildFile(parentDir + "/migrateExport.xml", false));
        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
        xStream.toXML(dbTables, writer);
    } catch (Exception e) {
        throw new BusinessException(e.getMessage());
    }
}


}