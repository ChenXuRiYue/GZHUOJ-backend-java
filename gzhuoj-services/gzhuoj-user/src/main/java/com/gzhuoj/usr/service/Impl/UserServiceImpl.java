package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dto.req.UserBatchImportReqDTO;
import com.gzhuoj.usr.dto.req.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.service.UserService;
import com.gzhuoj.usr.utils.ExcelWriter;
import com.gzhuoj.usr.utils.generateRandStrUtil;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String test() {
        return "fuck";
    }

    /**
     * @param requestParam
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUserAccount())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }

        // 用redis存储用户信息 ->  返回一个token来证明用户已经登录
        String KEY = "Login_" + requestParam.getUserAccount();
        Map<Object, Object> keyMap = stringRedisTemplate.opsForHash().entries(KEY);
        if (CollUtil.isNotEmpty(keyMap)) {
            String token = keyMap.keySet()
                    .stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录失败"));
            return new UserLoginRespDTO(token);
        }
        String uuid = UUID.randomUUID().toString();

        stringRedisTemplate.opsForHash().put(KEY, uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(KEY, 30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }


    /**
     * 批量导入账号信息
     *
     * @param requestParam 账号CSV文件
     * @return 账号信息的 List集合
     */
    @Override
    public ResponseEntity<byte[]> batchImport(UserBatchImportReqDTO requestParam) throws IOException {
        MultipartFile userExcelFile = requestParam.getUserExcelFile();

        List<UserDO> userDOList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(userExcelFile.getInputStream(), "GBK"))) {
            String line;
            boolean flag = false;
            while ((line = reader.readLine()) != null) {
                if (!flag) {
                    flag = true;
                    continue;
                }
                String[] fields = line.split("\t");
                // 账号组成 -> 学校名英文缩写_学生学号
                String userAccount = requestParam.getSchoolEngName() + "_" + fields[0].trim();
                String username = fields[1].trim();
                // 生成随机化的密码 -> 8位数字 + 大小写字母
                String password = generateRandStrUtil.getRandStr(8);

                UserDO userDO = UserDO.builder()
                        .userAccount(userAccount)
                        .username(username)
                        .password(password)
                        .email("")
                        .role(1)  // 1 -> 普通用户
                        .organization(requestParam.getSchoolEngName())
                        .build();
                userDOList.add(userDO);
            }
        } catch (IOException e) {
            log.error("[CSV文件读取失败]");
            throw new RuntimeException(e);
        }

        // 账号信息需存入数据库
        userDOList.forEach(each -> {
            baseMapper.insert(each);
        });
        byte[] excelContent = ExcelWriter.writeUsersToExcel(userDOList);
        String fileName = "导出名单.xlsx";
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @Override
    public UserInfoUpdateRespDTO updateInfo(UserInfoUpdateReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, requestParam.getUserAccount())
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }
        if (StrUtil.isEmpty(requestParam.getPassword())) {
            throw new ClientException("密码不能为空");
        }
        if (!Objects.equals(requestParam.getPassword(), userDO.getPassword())) {
            throw new ClientException("密码错误");
        }

        userDO.setEmail(!StrUtil.isEmpty(requestParam.getEmail()) ? requestParam.getEmail() : userDO.getEmail());
        userDO.setOrganization(!StrUtil.isEmpty(requestParam.getOrganization()) ? requestParam.getOrganization() : userDO.getOrganization());
        userDO.setUsername(!StrUtil.isEmpty(requestParam.getUsername()) ? requestParam.getUsername() : userDO.getUsername());
        if (!StrUtil.isEmpty(requestParam.getNewPassword())) {
            if (!Objects.equals(requestParam.getNewPassword(), requestParam.getVerifyNewPassword())) {
                throw new ClientException("两次密码不一致");
            }
            userDO.setPassword(requestParam.getNewPassword());
        }
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserAccount, requestParam.getUserAccount())
                .eq(UserDO::getDeleteFlag, 0);
        baseMapper.update(userDO, updateWrapper);
        return new UserInfoUpdateRespDTO(requestParam.getUserAccount());
    }

    @Override
    public UserInfoRespDTO userInfo(String userAccount) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, userAccount)
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null){
            throw new ClientException("用户不存在");
        }
        return BeanUtil.toBean(userDO, UserInfoRespDTO.class);
    }

    public static void main(String[] args) {

    }
}
