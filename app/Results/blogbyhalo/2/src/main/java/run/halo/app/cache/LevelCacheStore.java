package run.halo.app.cache;
 import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.utils.JsonUtils;
@Slf4j
public class LevelCacheStore extends AbstractStringCacheStore{

 private  long PERIOD;

 private  DB LEVEL_DB;

 private  Timer timer;

public LevelCacheStore(HaloProperties haloProperties) {
    super.haloProperties = haloProperties;
}
@PostConstruct
public void init(){
    if (LEVEL_DB != null) {
        return;
    }
    try {
        // work path
        File folder = new File(haloProperties.getWorkDir() + ".leveldb");
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        options.createIfMissing(true);
        // open leveldb store folder
        LEVEL_DB = factory.open(folder, options);
        timer = new Timer();
        timer.scheduleAtFixedRate(new CacheExpiryCleaner(), 0, PERIOD);
    } catch (Exception ex) {
        log.error("init leveldb error ", ex);
    }
}


@Override
public void putInternal(String key,CacheWrapper<String> cacheWrapper){
    putInternalIfAbsent(key, cacheWrapper);
}


@Override
public Boolean putInternalIfAbsent(String key,CacheWrapper<String> cacheWrapper){
    Assert.hasText(key, "Cache key must not be blank");
    Assert.notNull(cacheWrapper, "Cache wrapper must not be null");
    try {
        LEVEL_DB.put(stringToBytes(key), stringToBytes(JsonUtils.objectToJson(cacheWrapper)));
        return true;
    } catch (JsonProcessingException e) {
        log.warn("Put cache fail json2object key: [{}] value:[{}]", key, cacheWrapper);
    }
    log.debug("Cache key: [{}], original cache wrapper: [{}]", key, cacheWrapper);
    return false;
}


@Override
@NonNull
public Optional<CacheWrapper<String>> getInternal(String key){
    Assert.hasText(key, "Cache key must not be blank");
    byte[] bytes = LEVEL_DB.get(stringToBytes(key));
    if (bytes != null) {
        String valueJson = bytesToString(bytes);
        return StringUtils.isEmpty(valueJson) ? Optional.empty() : jsonToCacheWrapper(valueJson);
    }
    return Optional.empty();
}


@PreDestroy
public void preDestroy(){
    try {
        LEVEL_DB.close();
        timer.cancel();
    } catch (IOException e) {
        log.error("close leveldb error ", e);
    }
}


@Override
public void run(){
    // batch
    WriteBatch writeBatch = LEVEL_DB.createWriteBatch();
    DBIterator iterator = LEVEL_DB.iterator();
    long currentTimeMillis = System.currentTimeMillis();
    while (iterator.hasNext()) {
        Map.Entry<byte[], byte[]> next = iterator.next();
        if (next.getKey() == null || next.getValue() == null) {
            continue;
        }
        String valueJson = bytesToString(next.getValue());
        Optional<CacheWrapper<String>> stringCacheWrapper = StringUtils.isEmpty(valueJson) ? Optional.empty() : jsonToCacheWrapper(valueJson);
        if (stringCacheWrapper.isPresent()) {
            // get expireat time
            long expireAtTime = stringCacheWrapper.map(CacheWrapper::getExpireAt).map(Date::getTime).orElse(0L);
            // if expire
            if (expireAtTime != 0 && currentTimeMillis > expireAtTime) {
                writeBatch.delete(next.getKey());
                log.debug("deleted the cache: [{}] for expiration", bytesToString(next.getKey()));
            }
        }
    }
    LEVEL_DB.write(writeBatch);
}


@Override
public void delete(String key){
    LEVEL_DB.delete(stringToBytes(key));
    log.debug("cache remove key: [{}]", key);
}


public byte[] stringToBytes(String str){
    return str.getBytes(Charset.defaultCharset());
}


public String bytesToString(byte[] bytes){
    return new String(bytes, Charset.defaultCharset());
}


}