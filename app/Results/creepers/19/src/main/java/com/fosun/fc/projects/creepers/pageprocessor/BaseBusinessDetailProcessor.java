package com.fosun.fc.projects.creepers.pageprocessor;
 import java.util.List;
import java.util.Map;
import org.nlpcn.commons.lang.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fosun.fc.projects.creepers.constant.CreepersConstant;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.selector.Selectable;
public class BaseBusinessDetailProcessor {

 private  Logger logger;

 protected  String PAGE_URL;

 protected  String SPILT_COLON;

 protected  String SPILT_APOSTROPHE;

 protected  String SPILT_COMMA;

 protected  String SPILT_SEMICOLON;

 protected  String SPILT_QUESTION;

 protected  String CONTENT_EMPTY;

 protected  String CONTENT_MORE;

 protected  String REPLACE_FUNCTION_END;

 protected  String REPLACE_QUESTION;

 protected  String APPEND_URL_RANDOM;

 protected  String APPEND_URL_MAENT_XH;

 protected  String APPEND_URL_MAENT_ND;

 protected  String REPLACE_CONTENT_NBSP;

 protected  String XPATH_TABLE;

 protected  String XPATH_TD_ALLTEXT;

 protected  String XPATH_TBODY;

 protected  String XPATH_TR;

 protected  String XPATH_TD;

 protected  String XPATH_SPAN;

 protected  String XPATH_ALL_TEXT;

 protected  String XPATH_A_ONCLICK;

 protected  String XPATH_SPILT;

 private  String CONTENT_CREDIT_CODE;

 private  String CONTENT_NAME;

 private  String CONTENT_TYPE;

 private  String CONTENT_LEGAL_REPRESENTATIVE;

 private  String CONTENT_REGISTERED_CAPITAL;

 private  String CONTENT_CONSTRUCT_DATE;

 private  String CONTENT_ADDRESS;

 private  String CONTENT_OPERATING_PERIOD_START;

 private  String CONTENT_OPERATING_PERIOD_END;

 private  String CONTENT_BUSINESS_SCOPE;

 private  String CONTENT_REGISTRATION_AUTHORITY;

 private  String CONTENT_AUTHORIZE_DATE;

 private  String CONTENT_REGISTRATION_STATUS;

 private  String CONTENT_COMPANY_NAME;

 private  String CONTENT_COMPANY_PHONE;

 private  String CONTENT_COMPANY_POST_CODE;

 private  String CONTENT_COMPANY_ADDR;

 private  String CONTENT_COMPANY_EMAIL;

 private  String CONTENT_COMPANY_IS_TRANSFER;

 private  String CONTENT_COMPANY_OPERATING_STATUS;

 private  String CONTENT_COMPANY_IS_WEBSITE;

 private  String CONTENT_COMPANY_IS_INVEST;

 private  String CONTENT_COMPANY_CREW_NUMBER;

 private  String CONTENT_COMPANY_TOTAL_ASSET;

 private  String CONTENT_COMPANY_TOTAL_EQUITY;

 private  String CONTENT_COMPANY_TOTAL_AVENUE;

 private  String CONTENT_COMPANY_TOTAL_INCOME;

 private  String CONTENT_COMPANY_TOTAL_BUS_INCOME;

 private  String CONTENT_COMPANY_NET_PROFIT;

 private  String CONTENT_COMPANY_TOTAL_TAX;

 private  String CONTENT_COMPANY_TOTAL_LIABILITIES;


public void merBaseInfo(Selectable tableSelectable,ResultItems resultItems,Map<String,String> merNoMap){
    logger.info("======>>   entry   merBaseInfo:");
    // logger.info(selectable.toString());
    List<Selectable> tdThList = tableSelectable.xpath(XPATH_TBODY + XPATH_TR + XPATH_SPILT).nodes();
    boolean isFirstTr = true;
    Map<String, String> map = resultItems.get(CreepersConstant.TableNamesBusinessInfo.T_CREEPERS_MER_BASIC.getMapKey());
    // 工商注册号
    map.putAll(merNoMap);
    logger.info(CreepersConstant.TCreepersMerBasicColumn.MER_NO.getValue() + SPILT_COLON + map.get(CreepersConstant.TCreepersMerBasicColumn.MER_NO.getValue()));
    String lastContent = CONTENT_EMPTY;
    for (Selectable eachTdTh : tdThList) {
        if (isFirstTr) {
            isFirstTr = false;
            continue;
        }
        String currentContent = eachTdTh.xpath(XPATH_ALL_TEXT).toString();
        logger.info("currentContent:" + currentContent);
        if (CONTENT_CREDIT_CODE.equals(currentContent)) {
            lastContent = CONTENT_CREDIT_CODE;
            continue;
        } else if (CONTENT_CREDIT_CODE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 注册号/统一社会信用代码
            map.put(CreepersConstant.TCreepersMerBasicColumn.MEMO.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.MEMO.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_NAME.equals(currentContent)) {
            lastContent = CONTENT_NAME;
            continue;
        } else if (CONTENT_NAME.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 名称
            map.put(CreepersConstant.TCreepersMerBasicColumn.MER_NAME.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.MER_NAME.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_TYPE.equals(currentContent)) {
            lastContent = CONTENT_TYPE;
            continue;
        } else if (CONTENT_TYPE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 类型
            map.put(CreepersConstant.TCreepersMerBasicColumn.MER_TYPE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.MER_TYPE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_LEGAL_REPRESENTATIVE.equals(currentContent)) {
            lastContent = CONTENT_LEGAL_REPRESENTATIVE;
            continue;
        } else if (CONTENT_LEGAL_REPRESENTATIVE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 法定代表人
            map.put(CreepersConstant.TCreepersMerBasicColumn.LEGAL_REP.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.LEGAL_REP.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_REGISTERED_CAPITAL.equals(currentContent)) {
            lastContent = CONTENT_REGISTERED_CAPITAL;
            continue;
        } else if (CONTENT_REGISTERED_CAPITAL.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 注册资本
            map.put(CreepersConstant.TCreepersMerBasicColumn.REG_CAPITAL.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.REG_CAPITAL.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_CONSTRUCT_DATE.equals(currentContent)) {
            lastContent = CONTENT_CONSTRUCT_DATE;
            continue;
        } else if (CONTENT_CONSTRUCT_DATE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 成立日期
            if (StringUtil.isNotBlank(currentContent)) {
                map.put(CreepersConstant.TCreepersMerBasicColumn.FOUND_DT.getValue(), currentContent);
                logger.info(CreepersConstant.TCreepersMerBasicColumn.FOUND_DT.getValue() + SPILT_COLON + currentContent);
            }
        } else if (CONTENT_ADDRESS.equals(currentContent)) {
            lastContent = CONTENT_ADDRESS;
            continue;
        } else if (CONTENT_ADDRESS.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 住所
            map.put(CreepersConstant.TCreepersMerBasicColumn.ADDR.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.ADDR.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_OPERATING_PERIOD_START.equals(currentContent)) {
            lastContent = CONTENT_OPERATING_PERIOD_START;
            continue;
        } else if (CONTENT_OPERATING_PERIOD_START.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 营业期限自
            if (StringUtil.isNotBlank(currentContent)) {
                map.put(CreepersConstant.TCreepersMerBasicColumn.OPR_START_DT.getValue(), currentContent);
                logger.info(CreepersConstant.TCreepersMerBasicColumn.OPR_START_DT.getValue() + SPILT_COLON + currentContent);
            }
        } else if (CONTENT_OPERATING_PERIOD_END.equals(currentContent)) {
            lastContent = CONTENT_OPERATING_PERIOD_END;
            continue;
        } else if (CONTENT_OPERATING_PERIOD_END.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 营业期限至
            if (StringUtil.isNotBlank(currentContent)) {
                map.put(CreepersConstant.TCreepersMerBasicColumn.OPR_END_DT.getValue(), currentContent);
                logger.info(CreepersConstant.TCreepersMerBasicColumn.OPR_END_DT.getValue() + SPILT_COLON + currentContent);
            }
        } else if (CONTENT_BUSINESS_SCOPE.equals(currentContent)) {
            lastContent = CONTENT_BUSINESS_SCOPE;
            continue;
        } else if (CONTENT_BUSINESS_SCOPE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 经营范围
            map.put(CreepersConstant.TCreepersMerBasicColumn.SCOPE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.SCOPE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_REGISTRATION_AUTHORITY.equals(currentContent)) {
            lastContent = CONTENT_REGISTRATION_AUTHORITY;
            continue;
        } else if (CONTENT_REGISTRATION_AUTHORITY.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 登记机关
            map.put(CreepersConstant.TCreepersMerBasicColumn.REG_AUTHORITY.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.REG_AUTHORITY.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_AUTHORIZE_DATE.equals(currentContent)) {
            lastContent = CONTENT_AUTHORIZE_DATE;
            continue;
        } else if (CONTENT_AUTHORIZE_DATE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 核准日期
            if (StringUtil.isNotBlank(currentContent)) {
                map.put(CreepersConstant.TCreepersMerBasicColumn.MER_TYPE.getValue(), currentContent);
                logger.info(CreepersConstant.TCreepersMerBasicColumn.MER_TYPE.getValue() + SPILT_COLON + currentContent);
            }
        } else if (CONTENT_REGISTRATION_STATUS.equals(currentContent)) {
            lastContent = CONTENT_REGISTRATION_STATUS;
            continue;
        } else if (CONTENT_REGISTRATION_STATUS.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 登记状态
            map.put(CreepersConstant.TCreepersMerBasicColumn.REG_STATUS.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersMerBasicColumn.REG_STATUS.getValue() + SPILT_COLON + currentContent);
        }
    }
    logger.info("======>>   end  merBaseInfo:");
}


public void entNdbgAsset(Selectable tableSelectable,ResultItems resultItems,Map<String,String> merNoMap){
    logger.info("======>>   entry   entNdbgAsset:");
    List<Selectable> tdThList = tableSelectable.xpath(XPATH_TBODY + XPATH_TR + XPATH_SPILT).nodes();
    Map<String, String> map = resultItems.get(CreepersConstant.TableNamesBusinessInfo.T_CREEPERS_ENT_ASSET.getMapKey());
    // 工商注册号
    map.putAll(merNoMap);
    logger.info(CreepersConstant.TCreepersEntAssetColumn.MER_NO.getValue() + SPILT_COLON + map.get(CreepersConstant.TCreepersEntAssetColumn.MER_NO.getValue()));
    int tdThNum = 1;
    String lastContent = CONTENT_EMPTY;
    for (Selectable eachTdTh : tdThList) {
        if (tdThNum < 2) {
            tdThNum++;
            continue;
        }
        String currentContent = eachTdTh.xpath(XPATH_ALL_TEXT).toString();
        logger.info("currentContent:" + currentContent);
        if (CONTENT_COMPANY_TOTAL_ASSET.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_ASSET;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_ASSET.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 资产总额
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_ASSET.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_ASSET.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_EQUITY.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_EQUITY;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_EQUITY.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 所有者权益合计
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_EQUITY.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_EQUITY.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_AVENUE.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_AVENUE;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_AVENUE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 利润总额
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_AVENUE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_AVENUE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_INCOME.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_INCOME;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_INCOME.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 营业总收入
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_INCOME.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_INCOME.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_BUS_INCOME.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_BUS_INCOME;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_BUS_INCOME.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 营业总收入中主营业务收入
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_BUS_INCOME.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_BUS_INCOME.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_NET_PROFIT.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_NET_PROFIT;
            continue;
        } else if (CONTENT_COMPANY_NET_PROFIT.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 净利润
            map.put(CreepersConstant.TCreepersEntAssetColumn.NET_PROFIT.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.NET_PROFIT.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_TAX.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_TAX;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_TAX.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 纳税总额
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_TAX.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_TAX.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_TOTAL_LIABILITIES.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_TOTAL_LIABILITIES;
            continue;
        } else if (CONTENT_COMPANY_TOTAL_LIABILITIES.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 负债总额
            map.put(CreepersConstant.TCreepersEntAssetColumn.TOTAL_LIABILITIES.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntAssetColumn.TOTAL_LIABILITIES.getValue() + SPILT_COLON + currentContent);
        }
    }
    logger.info("======>>   finish   entNdbgAsset:");
}


public void entNdbgBasic(Selectable tableSelectable,ResultItems resultItems,Map<String,String> merNoMap){
    logger.info("======>>   entry   methodNdbgBasic:");
    List<Selectable> tdThList = tableSelectable.xpath(XPATH_TBODY + XPATH_TR + XPATH_SPILT).nodes();
    Map<String, String> map = resultItems.get(CreepersConstant.TableNamesBusinessInfo.T_CREEPERS_ENT_BASIC.getMapKey());
    // 工商注册号
    map.putAll(merNoMap);
    logger.info(CreepersConstant.TCreepersEntBasicColumn.MER_NO.getValue() + SPILT_COLON + map.get(CreepersConstant.TCreepersEntBasicColumn.MER_NO.getValue()));
    int tdThNum = 1;
    String lastContent = CONTENT_EMPTY;
    for (Selectable eachTdTh : tdThList) {
        if (tdThNum < 3) {
            tdThNum++;
            continue;
        }
        String currentContent = eachTdTh.xpath(XPATH_ALL_TEXT).toString();
        logger.info("currentContent:" + currentContent);
        if (CONTENT_CREDIT_CODE.equals(currentContent)) {
            lastContent = CONTENT_CREDIT_CODE;
            continue;
        } else if (CONTENT_CREDIT_CODE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 注册号/统一社会信用代码
            map.put(CreepersConstant.TCreepersEntBasicColumn.MEMO.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.MEMO.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_NAME.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_NAME;
            continue;
        } else if (CONTENT_COMPANY_NAME.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 企业名称
            map.put(CreepersConstant.TCreepersEntBasicColumn.MER_NAME.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.MER_NAME.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_PHONE.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_PHONE;
            continue;
        } else if (CONTENT_COMPANY_PHONE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 企业联系电话
            map.put(CreepersConstant.TCreepersEntBasicColumn.PHONE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.PHONE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_POST_CODE.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_POST_CODE;
            continue;
        } else if (CONTENT_COMPANY_POST_CODE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 邮政编码
            map.put(CreepersConstant.TCreepersEntBasicColumn.POST_CODE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.POST_CODE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_ADDR.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_ADDR;
            continue;
        } else if (CONTENT_COMPANY_ADDR.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 企业通信地址
            map.put(CreepersConstant.TCreepersEntBasicColumn.ADDR.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.ADDR.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_EMAIL.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_EMAIL;
            continue;
        } else if (CONTENT_COMPANY_EMAIL.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 企业电子邮箱
            map.put(CreepersConstant.TCreepersEntBasicColumn.EMAIL.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.EMAIL.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_IS_TRANSFER.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_IS_TRANSFER;
            continue;
        } else if (CONTENT_COMPANY_IS_TRANSFER.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 有限责任公司本年度是否发生股东股权转让
            map.put(CreepersConstant.TCreepersEntBasicColumn.IS_TRANSFER.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.IS_TRANSFER.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_OPERATING_STATUS.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_OPERATING_STATUS;
            continue;
        } else if (CONTENT_COMPANY_OPERATING_STATUS.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 企业经营状态
            map.put(CreepersConstant.TCreepersEntBasicColumn.STATUS.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.STATUS.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_IS_WEBSITE.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_IS_WEBSITE;
            continue;
        } else if (CONTENT_COMPANY_IS_WEBSITE.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 是否有网站或网店
            map.put(CreepersConstant.TCreepersEntBasicColumn.IS_WEBSITE.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.IS_WEBSITE.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_IS_INVEST.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_IS_INVEST;
            continue;
        } else if (CONTENT_COMPANY_IS_INVEST.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 是否有投资信息或购买其他公司股权
            map.put(CreepersConstant.TCreepersEntBasicColumn.IS_INVEST.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.IS_INVEST.getValue() + SPILT_COLON + currentContent);
        } else if (CONTENT_COMPANY_CREW_NUMBER.equals(currentContent)) {
            lastContent = CONTENT_COMPANY_CREW_NUMBER;
            continue;
        } else if (CONTENT_COMPANY_CREW_NUMBER.equals(lastContent)) {
            lastContent = CONTENT_EMPTY;
            // 从业人数
            map.put(CreepersConstant.TCreepersEntBasicColumn.CREW_NUMBER.getValue(), currentContent);
            logger.info(CreepersConstant.TCreepersEntBasicColumn.CREW_NUMBER.getValue() + SPILT_COLON + currentContent);
        }
    }
    logger.info("======>>   finish   methodNdbgBasic:");
}


}