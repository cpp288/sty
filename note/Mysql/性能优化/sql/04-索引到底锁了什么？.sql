-- phoneNum 没有加索引

###### 例子1 ######
set session autocommit = OFF;
update users set lastUpdate=NOW() where phoneNum = '13666666666';
rollback;

-- 其他查询执行
update users set lastUpdate=NOW() where id =2;	-- 阻塞
update users set lastUpdate=NOW() where id =1;	-- 阻塞
-- 当不是索引，则锁全表，成为表锁

###### 例子2 ######
set session autocommit = OFF;
update users set lastUpdate=NOW() where id = 1;
rollback;

-- 其他查询执行
update users set lastUpdate=NOW() where id =2;	-- 可执行
update users set lastUpdate=NOW() where id =1;	-- 阻塞
-- 当是索引时，则为行锁

###### 例子3 ######
set session autocommit = OFF;
update users set lastUpdate=NOW() where `name` = 'seven';
rollback;

-- 其他查询执行
update users set lastUpdate=NOW() where `name` = 'seven';
update users set lastUpdate=NOW() where id =1;
update users set lastUpdate=NOW() where `name` = 'qingshan';
update users set lastUpdate=NOW() where id =2;