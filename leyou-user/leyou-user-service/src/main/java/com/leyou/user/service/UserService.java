package com.leyou.user.service;

import com.leyou.common.util.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.util.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:";

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if (type == 1) {
            user.setUsername(data);
        } else if (type == 2) {
            user.setPhone(data);
        } else {
            return null;
        }
        return userMapper.selectCount(user) == 0;
    }

    public void sendVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);

        //发送消息到rabbitMQ
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "vertify.sms", msg);

        //将验证码保存到redis
        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        String redisCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //1.校验验证码
        if (!StringUtils.equals(code, redisCode)) {
            return;
        }
        //2.生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //3.加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        //4.新增用户
        user.setId(null);
        user.setCreated(new Date());
        userMapper.insert(user);
        //5.删除redis中的验证码
        redisTemplate.delete(KEY_PREFIX + user.getPhone());
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        password = CodecUtils.md5Hex(password, user.getSalt());
        if (StringUtils.equals(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
