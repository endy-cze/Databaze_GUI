-- create user 'chmiel'@'%' IDENTIFIED by '123';
-- GRANT SELECT ON mysql.proc TO 'chmiel'@'%';
grant execute on procedure pomdb.vyberZakazky2 to 'chmiel'@'%';
grant execute on procedure pomdb.vyberFyzKusy to 'chmiel'@'%';
grant execute on procedure pomdb.planovaniRozvrh to 'chmiel'@'%';