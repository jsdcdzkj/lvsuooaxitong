package com.jsdc.lsoa.service;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.lsoa.exception.CustomException;
import com.jsdc.lsoa.mapper.ProcessApproveRecordMapper;
import com.jsdc.lsoa.mapper.ProcessNodeMapper;
import com.jsdc.lsoa.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessNodeService {

    @Autowired
    private ProcessNodeMapper processNodeMapper;

    @Autowired
    private ProcessApproveRecordMapper approveRecordMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    public void save(ProcessNode entity) {
        SysUser loginUser = userService.getUser();
        Date now = new Date();

        // 查询该流程是否有未审批的案件
        List<ProcessApproveRecord> recordList = approveRecordMapper.selectList(new LambdaQueryWrapper<ProcessApproveRecord>()
                .eq(ProcessApproveRecord::getProcessId, entity.getProcessId())
                .eq(ProcessApproveRecord::getStatus, 0)
        );
        if (CollUtil.isNotEmpty(recordList)) {
            throw new CustomException("该流程存在未审批完成的案件，不允许修改");
        }

        entity.setUpdateUser(loginUser.getId());
        entity.setUpdateTime(now);

        if (Objects.isNull(entity.getId())) {
            // 新增节点时重新排序
            List<ProcessNode> list = processNodeMapper.selectList(new LambdaQueryWrapper<ProcessNode>()
                    .eq(ProcessNode::getProcessId, entity.getProcessId())
                    .ge(ProcessNode::getSort, entity.getSort())
                    .orderByAsc(ProcessNode::getSort)
            );
            if (CollUtil.isNotEmpty(list)) {
                for (ProcessNode node : list) {
                    node.setSort(node.getSort() + 1);
                    processNodeMapper.updateById(node);
                }
            }

            entity.setCreateTime(now);
            entity.setCreateUser(loginUser.getId());
            processNodeMapper.insert(entity);
        }else {
            processNodeMapper.updateById(entity);
        }

    }


    public void deleteById(Integer id) {
        ProcessNode entity = processNodeMapper.selectById(id);

        // 查询该流程是否有未审批的案件
        List<ProcessApproveRecord> recordList = approveRecordMapper.selectList(new LambdaQueryWrapper<ProcessApproveRecord>()
                .eq(ProcessApproveRecord::getProcessId, entity.getProcessId())
                .eq(ProcessApproveRecord::getStatus, 0)
        );
        if (CollUtil.isNotEmpty(recordList)) {
            throw new CustomException("该流程存在未审批完成的案件，不允许修改");
        }

        // 删除节点时重新排序
        List<ProcessNode> list = processNodeMapper.selectList(new LambdaQueryWrapper<ProcessNode>()
                .eq(ProcessNode::getProcessId, entity.getProcessId())
                .gt(ProcessNode::getSort, entity.getSort())
                .orderByAsc(ProcessNode::getSort)
        );
        if (CollUtil.isNotEmpty(list)) {
            for (ProcessNode node : list) {
                node.setSort(node.getSort() - 1);
                processNodeMapper.updateById(node);
            }
        }

        processNodeMapper.deleteById(id);
    }

    public List<ProcessNode> getByProcess(Integer processId) {
        List<ProcessNode> list = processNodeMapper.selectList(new LambdaQueryWrapper<ProcessNode>()
                .eq(ProcessNode::getProcessId, processId)
                .orderByAsc(ProcessNode::getSort)
        );
        List<SysUser> userList = userService.list();
        Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getRealName));
        List<SysRole> roleList = roleService.list();
        Map<Integer, String> roleMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, SysRole::getRoleName));
        for (ProcessNode node : list) {
            String approverValue = node.getApproverValue();
            String[] idArray = approverValue.split(",");
            List<String> nameList = new ArrayList<>();
            if (Objects.equals(node.getApproverType(), 1)) {
                for (String id : idArray) {
                    nameList.add(userMap.get(Integer.parseInt(id)));
                }
            }else {
                for (String id : idArray) {
                    nameList.add(roleMap.get(Integer.parseInt(id)));
                }
            }
            node.setApproverName(String.join("，", nameList));
        }
        return list;
    }
}
