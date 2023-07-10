package cn.getech.wms.api.service.impl;

import cn.getech.wms.api.dto.PlmStdDocument;
import cn.getech.wms.api.dto.ResultVO;
import cn.getech.wms.api.enums.ResultEnum;
import cn.getech.wms.api.exception.Asserts;
import cn.getech.wms.api.util.ResultVOUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.getech.wms.api.dto.K3Entity;
import cn.getech.wms.api.enums.FormIDEnum;
import cn.getech.wms.api.service.BaseService;
import cn.getech.wms.api.service.KingdeeService;
import com.kingdee.bos.webapi.entity.QueryParam;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * k3基本数据查询 通用服务接口实现
 * @author bocong.zheng
 */
@Service
@Slf4j
public class BaseServiceImpl implements BaseService {

    @Autowired
    KingdeeService kingdeeService;
    @Autowired
    private K3CloudApi k3CloudApi;

    @Override
    public <T> List<T> query(String name,Class<T> clz) {
        K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
        if(!Optional.of(k3Entity).isPresent()){
            Asserts.fail(ResultEnum.PARAM_ERROR,"k3基本数据字段未配置");
        }
        ResultVO<List<T>> resultVO = kingdeeService.query(equalsSql(k3Entity, name), clz, k3Entity);
        if (resultVO.getCode() == 0) {
            return resultVO.getData();
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Optional<T> get(String name,Class<T> clz) {
        K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
        if(!Optional.of(k3Entity).isPresent()){
            Asserts.fail(ResultEnum.PARAM_ERROR,"k3基本数据字段未配置");
        }
        T data = null;
        ResultVO<List<T>> resultVO = kingdeeService.query(equalsSql(k3Entity,name),clz,k3Entity );
        if (resultVO.getCode() == 0) {
            if (CollectionUtil.isNotEmpty(resultVO.getData())) {
                data = resultVO.getData().get(0);
            }
        }
        return Optional.ofNullable(data);
    }

    @Override
    public <T> List<T> queryIn(List<String> conditions, Class<T> clz) {
        K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
        if(!Optional.of(k3Entity).isPresent()){
            Asserts.fail(ResultEnum.PARAM_ERROR,"k3基本数据字段未配置");
        }
        ResultVO<List<T>> resultVO = kingdeeService.query(inSql(k3Entity, conditions), clz, k3Entity);
        if (resultVO.getCode() == 0) {
            return resultVO.getData();
        }
        return Collections.emptyList();
    }


    /**
     * 拼接查询条件语句，支持精确查询
     * @param k3Entity
     * @param name 查询用的key
     * @return
     */
    private String equalsSql(K3Entity k3Entity,String name){
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" = '");
        sb.append(name);
        sb.append("'");
        if(StringUtils.isNotBlank(k3Entity.defaultCondition())){
            //默认查询启用状态数据
            sb.append(k3Entity.defaultCondition());
        }
        return sb.toString();
    }

    /**
     * 拼接查询条件语句，支持精确查询
     * @param k3Entity
     * @param conditions 查询用的集合
     * @return
     */
    private String inSql(K3Entity k3Entity,List<String> conditions){
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" in (");
        for (int i = 0,len = conditions.size(); i < len; i++) {
            if(i > 0){
                sb.append(",");
            }
            sb.append("'");
            sb.append(conditions.get(i));
            sb.append("'");
        }
        sb.append(")");
        if(StringUtils.isNotBlank(k3Entity.defaultCondition())){
            //默认查询启用状态数据
            sb.append(k3Entity.defaultCondition());
        }
        return sb.toString();
    }

    /**
     * 拼接查询条件语句,支持模糊查询
     * @param k3Entity
     * @param name
     * @return
     */
    private String likeSql(K3Entity k3Entity,String name){
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" like '%");
        sb.append(name);
        //默认查询启用状态数据
        sb.append("%' and FForbidStatus='A'");
        return sb.toString();
    }


    /**
     * 应收应付单带禁用状态的拼接查询条件语句,支持模糊查询
     *
     * @param k3Entity
     * @param name
     * @return
     */
    private String likeSqlAll(K3Entity k3Entity, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" like '%");
        sb.append(name);
        //默认查询启用状态数据
        sb.append("%' and FCancelStatus='A'");
        return sb.toString();
    }
}
