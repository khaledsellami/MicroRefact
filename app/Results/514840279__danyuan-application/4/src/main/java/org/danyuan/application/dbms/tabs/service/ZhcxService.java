package org.danyuan.application.dbms.tabs.service;
 import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.danyuan.application.common.config.MultiDatasourceConfig;
import org.danyuan.application.dbms.tabs.po.SysDbmsTabsColsInfo;
import org.danyuan.application.dbms.tabs.vo.MulteityParam;
import org.danyuan.application.dbms.tabs.vo.SysDbmsTabsColsInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class ZhcxService {

 private  Logger logger;

@Autowired
 private JdbcTemplate jdbcTemplate;

@Autowired
 private MultiDatasourceConfig multiDatasourceConfig;


public void resultMap(String sqlString,Map<String,String> params,SysDbmsTabsColsInfoVo vo,Map<String,Object> resultMap){
    StringBuilder pageSql = new StringBuilder();
    if ("ORACLE".equals(vo.dbType.toUpperCase())) {
        pageSql.append(" select *  ");
        pageSql.append(" from (select tp.*,    ");
        pageSql.append("   rownum as tp_rownum ");
        pageSql.append("    from (    ");
        pageSql.append(" " + sqlString.toString() + "    ");
        pageSql.append("   ) tp   ");
        pageSql.append("   where rownum <= " + (vo.getPageNumber().intValue()) * vo.getPageSize().intValue() + "");
        pageSql.append(" )                           	   ");
        pageSql.append(" where tp_rownum > " + (vo.getPageNumber().intValue() - 1) * vo.getPageSize().intValue() + "  ");
    } else if ("MYSQL".equals(vo.dbType.toUpperCase())) {
        pageSql.append(sqlString.toString() + " limit " + (vo.getPageNumber().intValue() - 1) * vo.getPageSize().intValue() + "," + vo.getPageSize().intValue());
    }
    String pageSqlstr = pageSql.toString();
    if (params != null) {
        Set<String> set = params.keySet();
        Iterator<String> iterable = set.iterator();
        while (iterable.hasNext()) {
            String nameString = iterable.next();
            pageSqlstr = pageSqlstr.replace(":" + nameString, "'" + params.get(nameString) + "'");
        }
    }
    try {
        Connection connection = multiDatasourceConfig.getConnection(vo.getJdbcUuid());
        Statement statement = connection.createStatement();
        // Map<String, DataSource> multiDatasource = multiDatasourceConfig.multiDatasource();
        // List<Map<String, Object>> list = jdbcTemplate.queryForList(pageSql.toString());
        // NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(multiDatasource.get(vo.getJdbcUuid()));
        // List<Map<String, Object>> list = template.queryForList(pageSql.toString(), param);
        logger.debug(pageSqlstr, ZhcxService.class);
        ResultSet resultSet = statement.executeQuery(pageSqlstr);
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                String metadata = resultSetMetaData.getColumnName(i + 1);
                switch(resultSetMetaData.getColumnType(i + 1)) {
                    case java.sql.Types.TIMESTAMP:
                        map.put(metadata, resultSet.getDate(i + 1));
                        break;
                    case java.sql.Types.CLOB:
                        try {
                            Reader is = resultSet.getClob(i + 1).getCharacterStream();
                            // 得到流
                            BufferedReader buff = new BufferedReader(is);
                            String line = buff.readLine();
                            StringBuffer sb = new StringBuffer();
                            while (line != null) {
                                // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                                sb.append(line);
                                line = buff.readLine();
                            }
                            map.put(metadata, sb.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        map.put(metadata, resultSet.getObject(i + 1));
                        break;
                }
            }
            list.add(map);
        }
        statement.close();
        resultMap.put("list", list);
        // if (list != null) {
        // resultMap.put("list", list);
        // } else {
        // resultMap.put("list", new ArrayList<>());
        // }
        if ("单表多条件查询".equals(vo.getType()) || "单表多条件更多查询".equals(vo.getType())) {
            String countsql = "";
            if ("ORACLE".equals(vo.dbType.toUpperCase())) {
                countsql = "select count(1) as total from (" + sqlString.toString() + "  and rownum < 500  ) count";
            } else {
                countsql = "select count(1) as total from (" + sqlString.toString() + "  limit 0, 500  ) count";
            }
            if ((vo.getTotal() == null || "0".equals(vo.getTotal().toString()))) {
                statement = connection.createStatement();
                // Map<String, Object> total = jdbcTemplate.queryForMap(countsql);
                // long count = template.queryForObject(countsql, param, Long.class);
                logger.debug(countsql, ZhcxService.class);
                resultSet = statement.executeQuery(countsql);
                resultSet.next();
                resultMap.put("total", resultSet.getLong(1));
                statement.close();
            } else {
                resultMap.put("total", vo.getTotal().intValue());
            }
        }
        connection.close();
    // multiDatasourceConfig.destroyMultiDatasource(multiDatasource);
    } catch (Exception e) {
    }
}


public List<List<SysDbmsTabsColsInfo>> sortByUserIndex(List<SysDbmsTabsColsInfo> list){
    if (list == null) {
        return null;
    }
    List<SysDbmsTabsColsInfo> tempList = new ArrayList<>();
    List<List<SysDbmsTabsColsInfo>> listlist = new ArrayList<>();
    List<SysDbmsTabsColsInfo> groupList = null;
    for (int i = 0; i < list.size(); i++) {
        SysDbmsTabsColsInfo sysZhcxCol = list.get(i);
        if (null != sysZhcxCol.getUserIndex() && !"".equals(sysZhcxCol.getUserIndex())) {
            tempList.add(sysZhcxCol);
        }
    }
    while (tempList.size() > 0) {
        groupList = new ArrayList<>();
        for (SysDbmsTabsColsInfo sysZhcxCol : tempList) {
            if (groupList.size() == 0) {
                groupList.add(sysZhcxCol);
            } else {
                if (groupList.get(0).getUserIndex().equals(sysZhcxCol.getUserIndex())) {
                    groupList.add(sysZhcxCol);
                }
            }
        }
        tempList.removeAll(groupList);
        listlist.add(groupList);
    }
    return listlist;
}


public void searchSqlByParams(StringBuffer sql,List<List<SysDbmsTabsColsInfo>> groupListList,List<MulteityParam> paramList,Map<String,String> params){
    if (groupListList == null) {
        return;
    }
    for (List<SysDbmsTabsColsInfo> groupList : groupListList) {
        StringBuffer sqltem = new StringBuffer();
        boolean exists = false;
        for (int i = 0; i < groupList.size(); i++) {
            SysDbmsTabsColsInfo sysZhcxCol = groupList.get(i);
            if (sysZhcxCol.getUserIndex() != null && !"".equals(sysZhcxCol.getUserIndex())) {
                if (paramList != null && paramList.size() > 0) {
                    for (MulteityParam multeityParam : paramList) {
                        if (multeityParam.getUserIndex().equals(sysZhcxCol.getUserIndex())) {
                            exists = true;
                            if (i > 0) {
                                sqltem.append(" or ");
                            }
                            String keysOperator = multeityParam.getOperator();
                            String param = multeityParam.getValue().replace("*", "%").replace("?", "_");
                            String op = "=";
                            if ("eq".equals(keysOperator)) {
                                if (param.indexOf("%") == -1 && param.indexOf("_") == -1) {
                                    op = " = ";
                                } else {
                                    op = " like ";
                                }
                            } else if ("ge".equals(keysOperator)) {
                                op = " >= ";
                            } else if ("le".equals(keysOperator)) {
                                op = " <= ";
                            }
                            String paramName = sysZhcxCol.getColsName() + i + keysOperator;
                            sqltem.append(sysZhcxCol.getColsName() + op + " :" + paramName);
                            params.put(paramName, param);
                        }
                    }
                }
            }
        }
        if (exists) {
            sql.append(" and (  ").append(sqltem.toString()).append(" )");
        }
    }
}


public Map<String,Object> findAllSigleTableByMulteityParam(SysDbmsTabsColsInfoVo vo){
    Map<String, Object> map = new HashMap<>();
    // 默认单表查询
    StringBuffer sql = new StringBuffer();
    sql.append("Select * from " + vo.getTabsName());
    // 单表多条件查询时 拼接sql
    sql.append("  where 1=1  ");
    for (SysDbmsTabsColsInfo sysZhcxCol : vo.getList()) {
        String colsRange = sysZhcxCol.getTabsUuid();
        String coldType = sysZhcxCol.getColsType();
        String colName = sysZhcxCol.getColsName();
        String colValue = sysZhcxCol.getColsDesc();
        if (colValue != null) {
            colValue = colValue.replace("*", "%").replace("?", "_");
        }
        if ("CHAR".equals(coldType) && null != colValue && !"".equals(colValue)) {
            if (colValue.indexOf("%") == -1 && colValue.indexOf("_") == -1) {
                // 精确查询
                sql.append(" and " + colName + " = '" + colValue + "'   ");
            } else if (!colValue.startsWith("%") && colValue.endsWith("%")) {
                // 前like查询
                sql.append(" and " + colName + " like '" + colValue + "'   ");
            } else {
                // 前后like查询oracle慢，变成精确
                sql.append(" and " + colName + " = '" + colValue + "'   ");
            }
        } else if ("DATE".equals(coldType) && null != colValue && !"".equals(colValue)) {
            if ("left".equals(colsRange)) {
                sql.append(" and " + colName + " >= to_date('" + colValue + "', 'YYYY-MM-DD') ");
            } else if ("right".equals(colsRange)) {
                sql.append(" and " + colName + " <= to_date('" + colValue + "', 'YYYY-MM-DD') ");
            }
        } else if ("NUMBER".equals(coldType) && null != colValue && !"".equals(colValue)) {
            if ("left".equals(colsRange)) {
                sql.append(" and " + colName + " >= " + colValue + "    ");
            } else if ("right".equals(colsRange)) {
                sql.append(" and " + colName + " <= " + colValue + "   ");
            }
        }
    }
    resultMap(sql.toString(), null, vo, map);
    return map;
}


public Map<String,Object> findBySingleTableByGroupsAndMulteityParam(SysDbmsTabsColsInfoVo vo){
    Map<String, Object> map = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(" select ");
    for (int i = 0; i < vo.getList().size(); i++) {
        if (i > 0) {
            sql.append(",");
        }
        sql.append(vo.getList().get(i).getColsName());
    }
    sql.append("   from " + vo.getTabsName() + " where 1=1 ");
    // 排序分组
    List<List<SysDbmsTabsColsInfo>> groupListList = sortByUserIndex(vo.getList());
    // 拼接查询条件与参数
    Map<String, String> params = new HashMap<>();
    searchSqlByParams(sql, groupListList, vo.getParamList(), params);
    // 拼接排序字段
    boolean sortColumnFlag = false;
    for (SysDbmsTabsColsInfo sysDbmsTabsColsInfo : vo.getList()) {
        if (sysDbmsTabsColsInfo.getColsSort() != null && ("asc".equals(sysDbmsTabsColsInfo.getColsSort().toLowerCase()) || "desc".equals(sysDbmsTabsColsInfo.getColsSort().toLowerCase()))) {
            if (!sortColumnFlag) {
                sql.append(" order by " + sysDbmsTabsColsInfo.getColsName() + " " + sysDbmsTabsColsInfo.getColsSort());
                sortColumnFlag = true;
            } else {
                sql.append(" , " + sysDbmsTabsColsInfo.getColsName() + " " + sysDbmsTabsColsInfo.getColsSort());
            }
        }
    }
    // 求结果
    resultMap(sql.toString(), params, vo, map);
    return map;
}


}