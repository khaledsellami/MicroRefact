package org.danyuan.application.dbms.echarts.service;
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.danyuan.application.dbms.echarts.po.SysDbmsChartDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class SysPlantPieStatisticsChartService {

@Autowired
 private JdbcTemplate jdbcTemplate;


public void buildPie(Map<String,Object> map,SysDbmsChartDimension info,StringBuilder sbWhere,String type1,String tableName){
    List<String> legend_data = new ArrayList<>();
    List<Map<String, Object>> series_data = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    Map<String, Object> param = new HashMap<>();
    // 默认表结构
    sql.append(" select  " + type1 + " as aks,count(1) as num");
    sql.append(" from " + tableName + "  ");
    sql.append(" where 1=1 ");
    sql.append(" and  " + type1 + " is not null ");
    sql.append(" and  " + type1 + " <> '' ");
    sql.append(sbWhere.toString());
    sql.append(" group by  " + type1);
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
    List<Map<String, Object>> listMap = template.queryForList(sql.toString(), param);
    for (Map<String, Object> map2 : listMap) {
        legend_data.add(map2.get("aks").toString());
        // {value:92503371, name:'男'}
        Map<String, Object> data = new HashMap<>();
        data.put("value", Integer.valueOf(map2.get("num").toString()));
        data.put("name", map2.get("aks").toString());
        series_data.add(data);
    }
    map.put("legend_data", legend_data);
    map.put("series_data", series_data);
    map.put("chartType", info.getChartType());
}


public void buildPieSum(Map<String,Object> map,SysDbmsChartDimension info,StringBuilder sbWhere,String type1,String tableName){
    List<String> legend_data = new ArrayList<>();
    List<Map<String, Object>> series_data = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    Map<String, Object> param = new HashMap<>();
    // 默认表结构
    sql.append(" SELECT  " + type1 + " AS AKS,SUM(`总中标金额`) AS NUM");
    sql.append(" FROM " + tableName + "  ");
    sql.append(" WHERE  DELETE_FLAG = 0  ");
    sql.append(" AND  " + type1 + " IS NOT NULL ");
    sql.append(" AND  " + type1 + " <> '' ");
    sql.append(" AND  公告类型 IN('中标公告','成交公告') ");
    sql.append(sbWhere.toString());
    sql.append(" group by  " + type1);
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
    List<Map<String, Object>> listMap = template.queryForList(sql.toString(), param);
    for (Map<String, Object> map2 : listMap) {
        legend_data.add(map2.get("AKS").toString());
        // {value:92503371, name:'男'}
        Map<String, Object> data = new HashMap<>();
        data.put("value", Double.valueOf(map2.get("NUM") == null ? "0" : map2.get("NUM").toString()).longValue());
        data.put("name", map2.get("AKS").toString());
        series_data.add(data);
    }
    map.put("legend_data", legend_data);
    map.put("series_data", series_data);
    map.put("chartType", info.getChartType());
}


}