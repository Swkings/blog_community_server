create table tsp_task(
    id int auto_increment primary key comment 'id',
    name varchar(50) null,
    comment varchar(200) null,
    type varchar(50) null comment '任务类型：TSP, ATSP..',
    dimension int null comment '问题规模',
    edge_weight_type varchar(50) null comment '权重类型：EUC_2D，ATT..',
    file_path varchar(200) null comment '存储路径',
    solution_file_path varchar(200) null comment '解决方案存储路径',
    user_id int null comment '用户id',
    status int null comment '状态：0：私有，1：公开',
    progress int null comment '任务完成进度：0-100',
    create_time timestamp null comment '创建时间'
);