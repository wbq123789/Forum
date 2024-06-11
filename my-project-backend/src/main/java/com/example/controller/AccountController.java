package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.ChangePasswordVO;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.request.ModifyEmailVO;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.entity.vo.response.AccountPrivacyVO;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import com.example.service.AccountService;
import com.example.utils.Const;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: forum
 * @description: 用户信息相关接口
 * @author: 王贝强
 * @create: 2024-06-07 20:55
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户信息、隐私设置相关", description = "包括用户信息、详细信息、隐私设置等操作。")
public class AccountController {
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsService detailsService;
    @Resource
    AccountPrivacyService privacyService;

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public RestBean<AccountVO> getAccountInfo(@RequestAttribute(Const.ATTR_USER_ID) Integer userId) {
        Account account = accountService.findAccountById(userId);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }
    /**
     * 获取用户详细信息
     * @param userId 用户ID
     * @return 用户详细信息
     */
    @GetMapping("/details")
    @Operation(summary = "获取用户详细信息")
    public RestBean<AccountDetailsVO> getAccountDetails(@RequestAttribute(Const.ATTR_USER_ID) Integer userId) {
        AccountDetails accountDetails = Optional
                .ofNullable(detailsService.findAccountDetailsByAccountId(userId))
                .orElseGet(AccountDetails::new);
        return RestBean.success(accountDetails.asViewObject(AccountDetailsVO.class));
    }
    /**
     * 保存用户详细信息
     * @param userId 用户ID
     * @param detailsSaveVO 详细信息
     * @return 是否保存成功
     */
    @PostMapping("/save-details")
    @Operation(summary = "保存用户详细信息")
    public RestBean<Void> saveAccountDetails(@RequestAttribute(Const.ATTR_USER_ID) Integer userId, @RequestBody @Valid DetailsSaveVO detailsSaveVO) {
        boolean success =detailsService.saveAccountDetails(userId,detailsSaveVO);
        return success ? RestBean.success() : RestBean.failure(400,"此用户名已存在，请重新输入！");
    }
    /**
     * 修改邮箱地址
     * @param userId 用户ID
     * @param modifyEmailVO 修改邮箱信息
     * @return 是否修改成功
     */
    @PostMapping("/modify-email")
    @Operation(summary = "修改邮箱地址")
    public RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) Integer userId, @RequestBody @Valid ModifyEmailVO modifyEmailVO) {
        return this.messageHandle(() ->
                accountService.modifyEmail(userId, modifyEmailVO));
    }
    /**
     * 修改密码
     * @param userId 用户ID
     * @param changePasswordVO 修改密码信息
     * @return 是否修改成功
     */
    @PostMapping("/change-password")
    @Operation(summary = "修改密码")
    public RestBean<Void> changePassword(@RequestAttribute(Const.ATTR_USER_ID) Integer userId, @RequestBody @Valid ChangePasswordVO changePasswordVO) {
        return this.messageHandle(() ->
                accountService.changePassword(userId, changePasswordVO));
    }
    /**
     * 保存用户隐私信息公开选项设置
    * @param userId 用户ID
    * @param privacySaveVO 隐私信息
    * @return 是否保存成功
    */
    @PostMapping("save-privacy")
    @Operation(summary = "保存用户隐私信息公开选项")
    public RestBean<Void> savePrivacy(@RequestAttribute(Const.ATTR_USER_ID) Integer userId, @RequestBody @Valid PrivacySaveVO privacySaveVO) {
        privacyService.savePrivacy(userId, privacySaveVO);
        return RestBean.success();
    }
    /**
     * 获取用户隐私信息公开选项
     * @param userId 用户ID
     * @return 用户隐私信息
     */
    @GetMapping("/privacy")
    @Operation(summary = "获取用户隐私信息公开选项")
    public RestBean<AccountPrivacyVO> getPrivacy(@RequestAttribute(Const.ATTR_USER_ID) Integer userId) {
        return RestBean.success(privacyService.getAccountPrivacy(userId).asViewObject(AccountPrivacyVO.class));
    }
    /**
     * 针对于返回值为String作为错误信息的方法进行统一处理
     * @param action 具体操作
     * @return 响应结果
     * @param <T> 响应结果类型
     */
    private <T> RestBean<T> messageHandle(Supplier<String> action){
        String message = action.get();
        if(message == null)
            return RestBean.success();
        else
            return RestBean.failure(400, message);
    }
}
