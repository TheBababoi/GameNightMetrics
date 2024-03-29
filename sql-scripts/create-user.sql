-- Drop user first if they exist
DROP USER if exists 'Methodus'@'%' ;

-- Now create user with prop privileges
CREATE USER 'Methodus'@'%' IDENTIFIED BY 'Methodus';

GRANT ALL PRIVILEGES ON * . * TO 'Methodus'@'%';