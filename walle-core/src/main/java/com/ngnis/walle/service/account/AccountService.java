package com.ngnis.walle.service.account;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.core.SignatureUtil;
import com.ngnis.walle.datasource.db.user.UserDO;
import com.ngnis.walle.datasource.db.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author houyi
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

    private final UserMapper userMapper;

    public int getUserCnt(String account) {
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("account", account);
        return userMapper.selectCountByExample(example);
    }

    public UserDO getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("id", userId);
        return userMapper.selectOneByExample(example);
    }

    public UserDO getUserByAccount(String account) {
        if (StrUtil.isBlank(account)) {
            return null;
        }
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("account", account);
        return userMapper.selectOneByExample(example);
    }

    public UserDO getUserByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return null;
        }
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("accessKey", accessKey);
        return userMapper.selectOneByExample(example);
    }

    public void saveUser(UserDO userDO) {
        userMapper.insertSelective(userDO);
    }

    public void updateUser(UserDO userDO) {
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("id", userDO.getId());
        userMapper.updateByExampleSelective(userDO, example);
    }


    /**
     * 查询实际的SK
     */
    public String getSecretKey(Long userId) {
        UserDO userDO = getUserById(userId);
        return userDO == null ? null : userDO.getSecretKey();
    }

    /**
     * 对原始密码加salt之后进行加密
     */
    public String encryptPwd(String password) {
        return SecureUtil.md5(Constants.SALT_USE_TO_ENCRYPT_PWD + password);
    }

    /**
     * 创建AK
     */
    public String createAccessKey(String account) {
        return SignatureUtil.createKey(DateUtil.current(false), account, 16);
    }

    /**
     * 创建SK
     */
    public String createSecretKey(String account) {
        return SignatureUtil.createKey(DateUtil.current(false), account + "_$_" + IdUtil.objectId(), 24);
    }


}
