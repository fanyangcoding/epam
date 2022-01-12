package com.epam.weather.response;

//import com.siemens.advanta.fsm.tools.util.StringUtil;
//import com.siemens.advanta.fsm.tools.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultModel<T> implements Serializable {
    private static final long serialVersionUID = -1802122468331526708L;

    /**
     * 返回码
     */
    private String code;

    /**
     * 描述
     */
    private String msg;

    /**
     * 返回内容
     */
    private T data;

    public ResultModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultModel(ResultStatus status) {
        this.code = String.valueOf(status.getCode());
        this.msg = status.getMsg();
    }

    public ResultModel(ResultStatus status, T data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }

    public static ResultModel<String> defaultSuccess(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            return new ResultModel<>(ResultStatus.SUCCESS.getCode(), msg);
        } else {
            return new ResultModel<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMsg());
        }
    }

    public static ResultModel<String> defaultError(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            return new ResultModel<>(ResultStatus.FAIL.getCode(), msg);
        } else {
            return new ResultModel<>(ResultStatus.FAIL.getCode(), ResultStatus.FAIL.getMsg());
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message=" + msg +
                ", data=" + data +
                "}";
    }
}
