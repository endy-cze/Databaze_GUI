-- create user 'havlicek'@'%' IDENTIFIED by '123';
GRANT SELECT ON mysql.proc TO 'havlicek'@'%';
REVOKE ALL PRIVILEGES, GRANT OPTION
    FROM 'havlicek','chmiel';
GRANT SELECT ON mysql.proc TO 'havlicek'@'%';
Revoke execute ON pomdb.* from 'havlicek'@'%','chmiel'@'%';
