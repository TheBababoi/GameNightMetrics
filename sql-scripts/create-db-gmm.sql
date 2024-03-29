DROP SCHEMA IF EXISTS `GameNightMetrics`;

CREATE SCHEMA `GameNightMetrics`;

use `GameNightMetrics`;

SET FOREIGN_KEY_CHECKS = 0;


-- Create Roles table
CREATE TABLE Roles (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE  -- Role name (e.g., admin, user)
);

-- Add initial roles (admin and user) to Roles table 
INSERT INTO Roles (name) VALUES ('admin'), ('user');

-- Create Players table
CREATE TABLE Players (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
skill_level VARCHAR(50),
play_style VARCHAR(255),  -- Comma separated list of styles (e.g., Balanced, Aggressive)
preferred_game_type VARCHAR(255),  -- Comma separated list of game types (e.g., Strategy, RPG)
total_games_played INT DEFAULT 0,
role_id INT DEFAULT 2,  -- Default role to user
FOREIGN KEY (role_id) REFERENCES Roles(id)  -- Foreign key definition moved here
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



-- Create Board Games table
CREATE TABLE BoardGames (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  number_of_players INT,
  game_type VARCHAR(255),
  total_games_played INT DEFAULT 0
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- Create Hunction Table
CREATE TABLE PlayerGameStats (
  player_id INT NOT NULL,
  game_id INT NOT NULL,
  wins INT DEFAULT 0,
  loses INT DEFAULT 0,
  plays INT DEFAULT 0,
  win_loss_ratio DECIMAL(5,2) DEFAULT 0.00,
  FOREIGN KEY (player_id) REFERENCES Players(id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (game_id) REFERENCES BoardGames(id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (player_id, game_id),
  INDEX fk_player_id (player_id),
  INDEX fk_game_id (game_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

