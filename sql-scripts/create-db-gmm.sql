-- Drop schema if it already exists (prevents errors on re-creation)
DROP SCHEMA IF EXISTS `GameNightMetrics`;

-- Create the schema
CREATE SCHEMA `GameNightMetrics`;

-- Use the newly created schema
USE `GameNightMetrics`;

-- Disable foreign key checks temporarily (for bulk data insertion if needed)
SET FOREIGN_KEY_CHECKS = 0;

-- Create Roles table
CREATE TABLE Role (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE  -- Role name (e.g., admin, user)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Insert initial roles
INSERT INTO `role` (name)
VALUES
  ('ROLE_USER'),
  ('ROLE_ADMIN');

-- Create Players table
CREATE TABLE Player (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  skill_level VARCHAR(50),
  play_style VARCHAR(255),  -- Comma separated list of styles (e.g., Balanced,Aggressive)
  preferred_game_type VARCHAR(255),  -- Comma separated list of game types (e.g., Strategy, RPG)
  total_games_played INT DEFAULT 0
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Insert a sample player
INSERT INTO Player (username, password, skill_level, play_style, preferred_game_type)
VALUES ('d', '$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K', 'Beginner', 'Balanced,Strategic', 'RPG,BoardGame');

-- Create Board Games table
CREATE TABLE BoardGame (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  number_of_max_players INT,
  number_of_min_players INT,
  game_type VARCHAR(255),
  total_games_played INT DEFAULT 0
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Create Junction Table (PlayerGameStats) for player statistics
CREATE TABLE PlayerGameStats (
  player_id INT NOT NULL,
  game_id INT NOT NULL,
  wins INT DEFAULT 0,
  loses INT DEFAULT 0,
  plays INT DEFAULT 0,
  win_loss_ratio DECIMAL(5,2) DEFAULT 0.00,
  FOREIGN KEY (player_id) REFERENCES Player(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,  -- Prevent deleting player with referenced stats
  FOREIGN KEY (game_id) REFERENCES BoardGame(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,  -- Prevent deleting game with referenced stats
  PRIMARY KEY (player_id, game_id),
  INDEX fk_player_id (player_id),
  INDEX fk_game_id (game_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create table for Player Roles (many-to-many relationship)
CREATE TABLE players_roles (
  user_id INT NOT NULL,  -- Foreign key referencing Player.id
  role_id INT NOT NULL,  -- Foreign key referencing Role.id
  PRIMARY KEY (user_id, role_id),  -- Composite primary key for uniqueness
  CONSTRAINT fk_players_roles_player_id FOREIGN KEY (user_id) REFERENCES Player(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,  -- Prevent deleting player with roles
  CONSTRAINT fk_players_roles_role_id FOREIGN KEY (role_id) REFERENCES Role(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION   -- Prevent deleting role with assigned players
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Enable foreign key checks for data integrity
SET FOREIGN_KEY_CHECKS = 1;
