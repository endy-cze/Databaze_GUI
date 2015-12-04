LOAD DATA LOCAL INFILE 'F:\\databaze_strasice\\zaloha\\2015_12_04\\2015_12_04_technologicka_karta.csv'
INTO TABLE technologicka_karta_db.technologicka_karta
CHARACTER SET 'cp1250'
FIELDS TERMINATED BY ',' ENCLOSED BY '\"'
LINES TERMINATED BY '\\n';