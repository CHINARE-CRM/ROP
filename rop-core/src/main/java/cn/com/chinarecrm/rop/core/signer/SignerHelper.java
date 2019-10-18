package cn.com.chinarecrm.rop.core.signer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Strings;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class SignerHelper {

    public static String mapAsUrlParams(Map<String, Object> map, String enc) {
        if (map == null || map.size() == 0) {
            return "";
        }
        String[] keys = Lang.collection2array(map.keySet());
        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        Lang.each(keys, new Each<String>() {

            @Override
            public void invoke(int index, String key, int length) throws ExitLoop, ContinueLoop, LoopException {
                Object val = map.get(key);
                if (val instanceof Collection || val.getClass().isArray()) {
                    // 数组情况的处理
                    Lang.each(val, new Each<Object>() {

                        @Override
                        public void invoke(int index, Object v, int length) throws ExitLoop, ContinueLoop, LoopException {
                            sb.append(Http.encode(key, enc))
                              .append('=')
                              .append(v)
                              .append('&');
                        }
                    });

                } else {
                    sb.append(Http.encode(key, enc))
                      .append('=')
                      .append(Http.encode(val, enc))
                      .append('&');
                }

            }
        });
        if (sb.length() > 0)
        { sb.setLength(sb.length() - 1);}
        return sb.toString();
    }

    public static String paramMapAsUrlString(Map<String, String[]> map, String enc) {
        if (map == null || map.size() == 0) {
            return "";
        }
        String[] keys = Lang.collection2array(map.keySet());
        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        Lang.each(keys, new Each<String>() {

            @Override
            public void invoke(int index, String key, int length) throws ExitLoop, ContinueLoop, LoopException {
                String[] val = map.get(key);
                if (val.length == 1) {
                    // 单值的情况
                    sb.append(Http.encode(key, enc))
                      .append('=')
                      .append(Http.encode(val[0], enc))
                      .append('&');
                } else {
                    Lang.each(val, new Each<Object>() {

                        @Override
                        public void invoke(int index, Object v, int length) throws ExitLoop, ContinueLoop, LoopException {
                            sb.append(Http.encode(key, enc))
                              .append('=')
                              .append(v)
                              .append('&');
                        }
                    });
                }

            }
        });
        if (sb.length() > 0)
        {sb.setLength(sb.length() - 1);}
        return sb.toString();
    }

    protected static String toStringInfo(Object val) {
        List<Object> info = Lang.list();
        Lang.each(val, new Each<Object>() {

            @Override
            public void invoke(int index, Object ele, int length) throws ExitLoop, ContinueLoop, LoopException {
                info.add(ele);
            }

        });
        return Strings.join(",", info);
    }

    private SignerHelper() {}
}
