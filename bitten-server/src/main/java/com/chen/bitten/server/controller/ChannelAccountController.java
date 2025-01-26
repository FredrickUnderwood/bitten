package com.chen.bitten.server.controller;

import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.persistence.ChannelAccount;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.server.service.ChannelAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class ChannelAccountController {

    @Autowired
    private ChannelAccountService channelAccountService;

    @PostMapping("/save")
    public BasicResultVO<ChannelAccount> saveOrUpdate(ChannelAccount channelAccount) {
        channelAccount.setCreator(channelAccount.getCreator().isBlank() ? BittenConstant.DEFAULT_CREATOR : channelAccount.getCreator());
        return BasicResultVO.success(channelAccountService.saveOrUpdate(channelAccount));
    }

    @GetMapping("/queryByChannelType")
    public BasicResultVO<List<ChannelAccount>> queryByChannelType(Integer channelType, String creator) {
        creator = creator.isBlank() ? BittenConstant.DEFAULT_CREATOR : creator;
        return BasicResultVO.success(channelAccountService.queryByChannelType(channelType, creator));
    }

    @GetMapping("/list")
    public BasicResultVO<List<ChannelAccount>> list(String creator) {
        creator = creator.isBlank() ? BittenConstant.DEFAULT_CREATOR : creator;
        return BasicResultVO.success(channelAccountService.list(creator));
    }

    @DeleteMapping("/delete/{ids}")
    public BasicResultVO deleteById(@PathVariable("ids") String ids) {
        if (Objects.nonNull(ids) && !ids.isBlank()) {
            List<Long> idList = Arrays.stream(ids.split(CommonConstant.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            channelAccountService.deleteByIds(idList);
        }
        return BasicResultVO.success();
    }


}
