package com.swking.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swking.entity.TspTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface TspTaskService extends IService<TspTask> {
    public List<TspTask> findTspTasks(int userId);
    public int updateTspTasksSolution(int id, String solutionFilePath, int progress);
    public int updateTspTasksProgress(int id, int progress);
}
