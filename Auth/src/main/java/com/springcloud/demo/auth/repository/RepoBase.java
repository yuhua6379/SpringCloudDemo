package com.springcloud.demo.auth.repository;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RestResource;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@NoRepositoryBean
public interface RepoBase<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {


    <S extends T> List<S> saveAll(Iterable<S> entities);

    void flush();

    <S extends T> S saveAndFlush(S entity);

    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);

    /**
     * @deprecated
     */
    @Deprecated
    T getOne(ID id);

    T getById(ID id);

    // 这里为什么要抄一个JpaRepository的实现，是因为要统一去禁止一些危险接口导出
    // 这些接口虽然不会被spring-boot-starter-data-rest自动导出，但是其repo还是可以直接使用
    // 一般写入类的和findAll之类的都应该被禁止掉
    @Override
    @ApiIgnore
//    @RestResource(exported = false)
    void deleteById(ID id);

    @Override
    @ApiIgnore
//    @RestResource(exported = false)
    List<T> findAll();

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    List<T> findAll(Sort sort);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    List<T> findAllById(Iterable<ID> ids);


    @Override
    @ApiIgnore
    @RestResource(exported = false)
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    Page<T> findAll(Pageable pageable);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    <S extends T> S save(S entity);

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    void delete(T entity);

    @ApiIgnore
    @RestResource(exported = false)
    void deleteAllById(Iterable<? extends ID> ids);

    /**
     * @deprecated
     */
    @Deprecated
    @ApiIgnore
    @RestResource(exported = false)
    default void deleteInBatch(Iterable<T> entities) {
        this.deleteAllInBatch(entities);
    }

    @ApiIgnore
    @RestResource(exported = false)
    void deleteAllInBatch(Iterable<T> entities);

    @ApiIgnore
    @RestResource(exported = false)
    void deleteAllByIdInBatch(Iterable<ID> ids);

    @ApiIgnore
    @RestResource(exported = false)
    void deleteAllInBatch();

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    void deleteAll();

    @Override
    @ApiIgnore
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends T> entities);
}