insert into user (`user_id`, `user_name`) values('1', 'test_user_1');
insert into user (`user_id`, `user_name`) values('2', 'test_user_2');
insert into user (`user_id`, `user_name`) values('3', 'test_user_3');
insert into user (`user_id`, `user_name`) values('4', 'test_user_4');


insert into city (`addr_1`, `addr_2`, `city_name`, `full_addr`, `explanation`)
values('경기도', '군포시', '산본', '경기도 군포시 산본', '수리산이 아름다운 도시');

insert into city (`addr_1`, `addr_2`, `city_name`, `full_addr`, `explanation`)
values('강원도', '동해시', '묵호항', '강원도 동해시 묵호항', '등대가 아름다운 곳');

insert into city (`addr_1`, `addr_2`, `city_name`, `full_addr`, `explanation`)
values('서울특별시', '종로구', '광화문', '서울특별시 종로구 광화문', '경복궁의 남쪽에 있는 정문');



-- 1번유저
insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('1박2일 수리산 등산', '2022-10-20', '2022-10-21', '1', '1');

insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('묵호항으로 여행', '2022-11-01', '2022-11-05', '2', '1');

insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('광화문 탐방', '2022-11-20', '2022-11-21', '3', '1');


-- 2번유저
insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('수리산 여행', '2022-12-20', '2022-12-22', '1', '2');

insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('크리스마스에 묵호항으로 여행', '2022-12-24', '2022-12-25', '2', '2');





