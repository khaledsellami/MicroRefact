package cn.maxcj.core.util;
 import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.maxcj.core.common.constant.dictmap.base.AbstractDictMap;
import cn.maxcj.core.common.constant.dictmap.factory.DictFieldWarpperFactory;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import cn.maxcj.DTO.AbstractDictMap;
public class Contrast {

 public  String separator;


public String contrastObjByName(Class dictClass,String key,Object pojo1,Map<String,String> pojo2){
    AbstractDictMap dictMap = (AbstractDictMap) dictClass.newInstance();
    String str = parseMutiKey(dictMap, key, pojo2) + separator;
    try {
        Class clazz = pojo1.getClass();
        Field[] fields = pojo1.getClass().getDeclaredFields();
        int i = 1;
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            String prefix = "get";
            int prefixLength = 3;
            if (field.getType().getName().equals("java.lang.Boolean")) {
                prefix = "is";
                prefixLength = 2;
            }
            Method getMethod = null;
            try {
                getMethod = clazz.getDeclaredMethod(prefix + StrUtil.upperFirst(field.getName()));
            } catch (NoSuchMethodException e) {
                System.err.println("this className:" + clazz.getName() + " is not methodName: " + e.getMessage());
                continue;
            }
            Object o1 = getMethod.invoke(pojo1);
            Object o2 = pojo2.get(StrUtil.lowerFirst(getMethod.getName().substring(prefixLength)));
            if (o1 == null || o2 == null) {
                continue;
            }
            if (o1 instanceof Date) {
                o1 = DateUtil.formatDate((Date) o1);
            } else if (o1 instanceof Integer) {
                o2 = Integer.parseInt(o2.toString());
            }
            if (!o1.toString().equals(o2.toString())) {
                if (i != 1) {
                    str += separator;
                }
                String fieldName = dictMap.get(field.getName());
                String fieldWarpperMethodName = dictMap.getFieldWarpperMethodName(field.getName());
                if (fieldWarpperMethodName != null) {
                    Object o1Warpper = DictFieldWarpperFactory.createFieldWarpper(o1, fieldWarpperMethodName);
                    Object o2Warpper = DictFieldWarpperFactory.createFieldWarpper(o2, fieldWarpperMethodName);
                    str += "字段名称:" + fieldName + ",旧值:" + o1Warpper + ",新值:" + o2Warpper;
                } else {
                    str += "字段名称:" + fieldName + ",旧值:" + o1 + ",新值:" + o2;
                }
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return str;
}


public String contrastObj(Class dictClass,String key,Object pojo1,Map<String,String> pojo2){
    AbstractDictMap dictMap = (AbstractDictMap) dictClass.newInstance();
    String str = parseMutiKey(dictMap, key, pojo2) + separator;
    try {
        Class clazz = pojo1.getClass();
        Field[] fields = pojo1.getClass().getDeclaredFields();
        int i = 1;
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method getMethod = pd.getReadMethod();
            Object o1 = getMethod.invoke(pojo1);
            Object o2 = pojo2.get(StrUtil.lowerFirst(getMethod.getName().substring(3)));
            if (o1 == null || o2 == null) {
                continue;
            }
            if (o1 instanceof Date) {
                o1 = DateUtil.formatDate((Date) o1);
            } else if (o1 instanceof Integer) {
                o2 = Integer.parseInt(o2.toString());
            }
            if (!o1.toString().equals(o2.toString())) {
                if (i != 1) {
                    str += separator;
                }
                String fieldName = dictMap.get(field.getName());
                String fieldWarpperMethodName = dictMap.getFieldWarpperMethodName(field.getName());
                if (fieldWarpperMethodName != null) {
                    Object o1Warpper = DictFieldWarpperFactory.createFieldWarpper(o1, fieldWarpperMethodName);
                    Object o2Warpper = DictFieldWarpperFactory.createFieldWarpper(o2, fieldWarpperMethodName);
                    str += "字段名称:" + fieldName + ",旧值:" + o1Warpper + ",新值:" + o2Warpper;
                } else {
                    str += "字段名称:" + fieldName + ",旧值:" + o1 + ",新值:" + o2;
                }
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return str;
}


public String parseMutiKey(AbstractDictMap dictMap,String key,Map<String,String> requests){
    StringBuilder sb = new StringBuilder();
    if (key.contains(",")) {
        String[] keys = key.split(",");
        for (String item : keys) {
            String fieldWarpperMethodName = dictMap.getFieldWarpperMethodName(item);
            String value = requests.get(item);
            if (fieldWarpperMethodName != null) {
                Object valueWarpper = DictFieldWarpperFactory.createFieldWarpper(value, fieldWarpperMethodName);
                sb.append(dictMap.get(item) + "=" + valueWarpper + ",");
            } else {
                sb.append(dictMap.get(item) + "=" + value + ",");
            }
        }
        return StrUtil.removeSuffix(sb.toString(), ",");
    } else {
        String fieldWarpperMethodName = dictMap.getFieldWarpperMethodName(key);
        String value = requests.get(key);
        if (fieldWarpperMethodName != null) {
            Object valueWarpper = DictFieldWarpperFactory.createFieldWarpper(value, fieldWarpperMethodName);
            sb.append(dictMap.get(key) + "=" + valueWarpper);
        } else {
            sb.append(dictMap.get(key) + "=" + value);
        }
        return sb.toString();
    }
}


}