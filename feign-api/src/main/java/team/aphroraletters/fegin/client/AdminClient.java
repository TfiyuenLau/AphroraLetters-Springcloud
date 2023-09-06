package team.aphroraletters.fegin.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team.aphroraletters.fegin.domain.response.ResultVO;

@FeignClient("article")
@RequestMapping("/article/admin")
public interface AdminClient {

    @ApiOperation("获取id对应的权限列表")
    @GetMapping("/getAuthorityById/{id}")
    public ResultVO getAuthorityById(@PathVariable("id") Long id);

}
