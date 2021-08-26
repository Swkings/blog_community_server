package com.swking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swking.entity.TspTask;
import com.swking.service.TspTaskService;
import com.swking.dao.TspTaskMapper;
import com.swking.util.BlogCommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 *
 */
@Service
public class TspTaskServiceImpl extends ServiceImpl<TspTaskMapper, TspTask>
    implements TspTaskService{
    @Autowired
    private TspTaskMapper tspTaskMapper;

    @Override
    public List<TspTask> findTspTasks(int userId) {
        QueryWrapper<TspTask> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return tspTaskMapper.selectList(wrapper);
    }

    public int updateTspTasksSolution(int id, String solutionFilePath, int progress){
        UpdateWrapper<TspTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("solution_file_path", solutionFilePath).set("progress", progress);
        return tspTaskMapper.update(null, updateWrapper);
    }

    public int updateTspTasksProgress(int id, int progress){
        UpdateWrapper<TspTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("progress", progress);
        return tspTaskMapper.update(null, updateWrapper);
    }

    public int deleteTask(int id){
        TspTask tspTask = tspTaskMapper.selectById(id);
        String filePath = tspTask.getFilePath();
        String solutionFilePath = tspTask.getSolutionFilePath();
        int deleteSuccess = tspTaskMapper.deleteById(id);
        if(deleteSuccess==1){
            File file;
            if(!StringUtils.isBlank(filePath)){
                file = new File(filePath);
                if(!file.isDirectory()){
                    file.delete();
                }
            }
            if(!StringUtils.isBlank(solutionFilePath)){
                file = new File(solutionFilePath);
                if(!file.isDirectory()){
                    file.delete();
                }
            }
        }
        return deleteSuccess;
    }
}




