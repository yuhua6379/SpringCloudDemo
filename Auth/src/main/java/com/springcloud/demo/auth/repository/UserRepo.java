package com.springcloud.demo.auth.repository;

import com.springcloud.demo.auth.entity.User;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Repository
// RepositoryRestResource 自动生成rest风格的curd接口
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepo extends RepoBase<User, Long> {

    @ApiIgnore
    Optional<User> findByUserName(String userName);

    @ApiIgnore
    Optional<User> findById(Long id);

    @ApiIgnore
    Optional<User> findByUserNameAndPassWord(String userName, String passWord);

    @ApiIgnore
    Boolean existsByUserName(String username);
}
