package com.reggie.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author win
 * @create 2022/11/22 18:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    //编码：1成功，0和其它数字为失败
    private Integer code;
    //错误信息
    private String msg;
    //数据
    private T data;
    //动态数据
    private Map map = new HashMap();

    public static <T> CommonResult<T> success(T object) {
        CommonResult<T> r = new CommonResult<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> CommonResult<T> error(String msg) {
        CommonResult r = new CommonResult();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public CommonResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
