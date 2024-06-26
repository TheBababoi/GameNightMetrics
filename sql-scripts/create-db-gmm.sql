DROP SCHEMA IF EXISTS `GameNightMetrics`;

CREATE SCHEMA `GameNightMetrics`;

USE `GameNightMetrics`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Role (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE  -- Role name (admin, user)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `role` (name)
VALUES
  ('ROLE_USER'),
  ('ROLE_ADMIN');

CREATE TABLE Player (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  skill_level VARCHAR(50),
  play_style VARCHAR(255),  -- Comma separated list of styles
  preferred_game_type VARCHAR(255),  -- Comma separated list of game types
  total_games_played INT DEFAULT 0
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO Player (username, password, skill_level, play_style, preferred_game_type)
VALUES ('Methodus', '$2a$12$HRqFg5UzUpg4E3Th.a07geTjhm6xfKu0rOc.EG5j.qnAq8lUMEJ/W', 'skill_level_here', 'play_style_here', 'preferred_game_type_here');
INSERT INTO Player (username, password, skill_level, play_style, preferred_game_type)
VALUES ('Tzaxis', '$2a$12$HRqFg5UzUpg4E3Th.a07geTjhm6xfKu0rOc.EG5j.qnAq8lUMEJ/W', 'skill_level_here', 'play_style_here', 'preferred_game_type_here');
INSERT INTO Player (username, password, skill_level, play_style, preferred_game_type)
VALUES ('Simos', '$2a$12$HRqFg5UzUpg4E3Th.a07geTjhm6xfKu0rOc.EG5j.qnAq8lUMEJ/W', 'skill_level_here', 'play_style_here', 'preferred_game_type_here');
INSERT INTO Player (username, password, skill_level, play_style, preferred_game_type)
VALUES ('Xaristos', '$2a$12$HRqFg5UzUpg4E3Th.a07geTjhm6xfKu0rOc.EG5j.qnAq8lUMEJ/W', 'skill_level_here', 'play_style_here', 'preferred_game_type_here');

CREATE TABLE BoardGame (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  number_of_max_players INT,
  number_of_min_players INT,
  game_type VARCHAR(255),
  total_games_played INT DEFAULT 0,
  average_total_rating DECIMAL(5,2) DEFAULT 0.00,
  average_difficulty_rating DECIMAL(5,2) DEFAULT 0.00,
  number_of_ratings INT DEFAULT 0  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE player_game_stats (
  player_id INT NOT NULL,
  game_id INT NOT NULL,
  wins INT DEFAULT 0,
  loses INT DEFAULT 0,
  plays INT DEFAULT 0,
  win_loss_ratio DECIMAL(5,2) DEFAULT 0.00,
  FOREIGN KEY (player_id) REFERENCES Player(id)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (game_id) REFERENCES BoardGame(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  PRIMARY KEY (player_id, game_id),
  INDEX fk_player_id (player_id),
  INDEX fk_game_id (game_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE players_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_players_roles_player_id FOREIGN KEY (user_id) REFERENCES Player(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_players_roles_role_id FOREIGN KEY (role_id) REFERENCES Role(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE game_ratings (
  player_id INT NOT NULL,
  game_id INT NOT NULL,
  total_rating INT,
  gameplay_rating INT,
  theme_rating INT,
  visual_rating INT,
  difficulty_rating INT,
  comment VARCHAR(255),
  PRIMARY KEY (player_id, game_id),
  FOREIGN KEY (player_id) REFERENCES Player(id)
   ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (game_id) REFERENCES BoardGame(id)
   ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE game_session (
  session_id INT PRIMARY KEY AUTO_INCREMENT,
  game_id INT NOT NULL,
  session_date DATE NOT NULL,
  session_duration varchar(255),
  total_players INT NOT NULL,
  FOREIGN KEY (game_id) REFERENCES BoardGame(id)
     ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE participating_players (
  session_id INT NOT NULL,
  player_id INT NOT NULL,
  PRIMARY KEY (session_id, player_id),
  FOREIGN KEY (session_id) REFERENCES game_session(session_id)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (player_id) REFERENCES Player(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE session_winners (
  session_id INT NOT NULL,
  player_id INT NOT NULL,
  PRIMARY KEY (session_id, player_id),
  FOREIGN KEY (session_id) REFERENCES game_session(session_id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (player_id) REFERENCES Player(id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELIMITER //
CREATE TRIGGER update_boardgame_ratings_after_insert
AFTER INSERT ON game_ratings
FOR EACH ROW
BEGIN
  UPDATE BoardGame b
  SET b.average_total_rating = (
    SELECT AVG(gr.total_rating)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  ),
  b.average_difficulty_rating = (
    SELECT AVG(gr.difficulty_rating)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  ),
  b.number_of_ratings = (
    SELECT COUNT(*)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  )
  WHERE b.id = NEW.game_id;
END //

CREATE TRIGGER update_boardgame_ratings_after_delete
AFTER DELETE ON game_ratings
FOR EACH ROW
BEGIN
  UPDATE BoardGame b
  SET b.average_total_rating = (
    SELECT AVG(gr.total_rating)
    FROM game_ratings gr
    WHERE gr.game_id = OLD.game_id
  ),
  b.average_difficulty_rating = (
    SELECT AVG(gr.difficulty_rating)
    FROM game_ratings gr
    WHERE gr.game_id = OLD.game_id
  ),
  b.number_of_ratings = (
    SELECT COUNT(*)
    FROM game_ratings gr
    WHERE gr.game_id = OLD.game_id
  )
  WHERE b.id = OLD.game_id;
END //

CREATE TRIGGER update_boardgame_ratings_after_update
AFTER UPDATE ON game_ratings
FOR EACH ROW
BEGIN
  UPDATE BoardGame b
  SET b.average_total_rating = (
    SELECT AVG(gr.total_rating)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  ),
  b.average_difficulty_rating = (
    SELECT AVG(gr.difficulty_rating)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  ),
  b.number_of_ratings = (
    SELECT COUNT(*)
    FROM game_ratings gr
    WHERE gr.game_id = NEW.game_id
  )
  WHERE b.id = NEW.game_id;
END //
DELIMITER ;





SELECT id INTO @playerId FROM Player WHERE username = 'Methodus';


INSERT INTO players_roles (user_id, role_id)
VALUES (@playerId, (SELECT id FROM Role WHERE name = 'ROLE_USER'));

INSERT INTO players_roles (user_id, role_id)
VALUES (@playerId, (SELECT id FROM Role WHERE name = 'ROLE_ADMIN'));


SET FOREIGN_KEY_CHECKS = 1;
