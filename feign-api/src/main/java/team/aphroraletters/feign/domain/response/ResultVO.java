package team.aphroraletters.feign.domain.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 * 501：bean验证错误，不管多少个错误都以map形式返回
 * 502：拦截器拦截到用户token出错
 * 555：异常抛出信息
 */
@ApiModel(value = "JSONResult传输对象", description = "json数据传输对象,封装了status、message、data属性")
public class ResultVO {

    @ApiModelProperty("响应状态码")
    private Integer status;

    @ApiModelProperty("响应体消息")
    private String msg;

    @ApiModelProperty("响应体数据")
    private Object data;

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public ResultVO(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public ResultVO(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVO build(Integer status, String msg, Object data) {
        return new ResultVO(status, msg, data);
    }

    public static ResultVO ok(Object data) {
        return new ResultVO(data);
    }

    public static ResultVO ok() {
        return new ResultVO(null);
    }

    public static ResultVO errorMsg(String msg) {
        return new ResultVO(500, msg, null);
    }

    public static ResultVO errorMap(Object data) {
        return new ResultVO(501, "error", data);
    }

    public static ResultVO errorTokenMsg(String msg) {
        return new ResultVO(502, msg, null);
    }

    public static ResultVO errorException(String msg) {
        return new ResultVO(555, msg, null);
    }

    public ResultVO() {

    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为LeeJSONResult对象
     *
     * @param jsonData
     * @param clazz
     * @return
     * @Description: 需要转换的对象是一个类
     * @author leechenxiang
     * @date 2016年4月22日 下午8:34:58
     */
    public static ResultVO formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ResultVO.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param json
     * @return
     * @Description: 没有object对象的转化
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:21
     */
    public static ResultVO format(String json) {
        try {
            return MAPPER.readValue(json, ResultVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param jsonData
     * @param clazz
     * @return
     * @Description: Object是集合转化
     * 需要转换的对象是一个list
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:31
     */
    public static ResultVO formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
