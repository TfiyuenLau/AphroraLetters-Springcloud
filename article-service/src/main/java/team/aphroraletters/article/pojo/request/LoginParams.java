package team.aphroraletters.article.pojo.request;

import lombok.Data;

@Data
public class LoginParams {

    private String username;

    private String password;

    private String code;

}
