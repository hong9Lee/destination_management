insert into user (`user_id`, `user_name`) values('1', 'test_user_1');
insert into user (`user_id`, `user_name`) values('2', 'test_user_2');

insert into city (`city_name`, `explanation`) values('산본', '수리산이 있는 도시');
insert into city (`city_name`, `explanation`) values('삼척', '바다가 있는 도시');


insert into travel (`title`, `start_date`, `end_date`, `city_id`, `user_id`)
values('동해로의 여행', '2022-09-14', '2022-12-14', '1', '1');
