docker run --detach --name=container-mysql --env="MYSQL_ROOT_PASSWORD=12345" --publish 3306:3306 mysql:5.6

criar database => create database eventos_test;
                  create database eventos_dev;

CREATE USER 'user_test_event'@'localhost' IDENTIFIED BY '12345';
CREATE USER 'user_test_event'@'%' IDENTIFIED BY '12345';

GRANT ALL ON eventos_test.* TO 'user_test_event'@'localhost' WITH GRANT OPTION;
GRANT ALL ON eventos_test.* TO 'user_test_event'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;


