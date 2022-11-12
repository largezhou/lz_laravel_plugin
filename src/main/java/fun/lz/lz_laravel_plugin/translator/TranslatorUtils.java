package fun.lz.lz_laravel_plugin.translator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorUtils {
    private final static String transAPIHost = "https://api.fanyi.baidu.com/api/trans/vip/translate";
    public static String appID = TranslatorSetting.getInstance().appID;
    public static String securityKey = TranslatorSetting.getInstance().securityKey;

    public static String getTransResult(String query, String from, String to) {
        Map<String, Object> params = buildParams(query, from, to);
        String resp = HttpUtil.get(transAPIHost, params);
        var mapper = new ObjectMapper();
        TransResp transResp;
        try {
            transResp = mapper.readValue(resp, TransResp.class);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
        if (StrUtil.isNotBlank(transResp.getErrorMsg())) {
            return String.format("翻译出错：%s，错误码：%s", transResp.getErrorMsg(), transResp.getErrorCode());
        }
        if (transResp.getTransResult() == null || transResp.getTransResult().size() == 0) {
            return "翻译出错";
        }
        return transResp.getTransResult().get(0).getDst();
    }

    private static Map<String, Object> buildParams(String query, String from, String to) {
        var params = new HashMap<String, Object>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appID);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        String src = appID + query + salt + securityKey; // 加密前的原文
        params.put("sign", SecureUtil.md5(src));
        return params;
    }

    @Data
    public static class TransResp {
        private String from;
        private String to;

        @JsonProperty("error_code")
        private String errorCode;
        @JsonProperty("error_msg")
        private String errorMsg;
        @JsonProperty("trans_result")
        private List<TransResult> transResult;
    }

    @Data
    public static class TransResult {
        private String src;
        private String dst;
    }
}
