package team.aphroraletters.feign.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2022-10-20
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Admin对象", description = "")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("管理员名称")
    private String realname;

    @ApiModelProperty("账户号")
    private String username;

    @ApiModelProperty("管理员密码")
    private String password;

    @ApiModelProperty("个人描述信息")
    private String description;

    @ApiModelProperty("权限（默认admin）")
    private String authority;

    @ApiModelProperty("权限列表")
    private List<String> roles;

    @ApiModelProperty("是否有效")
    private Byte isEffective;

}
