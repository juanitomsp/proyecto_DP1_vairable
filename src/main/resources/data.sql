-- One admin user, named admin1, with password '4dm1n' and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,email,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS','admin1@gmail.com',1);

-- Ten players users, named player1 with password 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,password,email,authority) VALUES (4,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player1@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (5,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player2@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (6,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player3@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (7,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player4@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (8,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player5@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (9,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player6@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (10,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player7@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (11,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player8@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (12,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player9@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (13,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player10@gmail.com',2);

INSERT INTO appusers(id,username,password,email,authority) VALUES (15,'xgc1564','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (17,'DKT3825','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (14,'NHV4546','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (16,'BJN1732','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (18,'RPG8849','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);
INSERT INTO appusers(id,username,password,email,authority) VALUES (19,'araapafal','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e','player@gmail.com',2);

INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (1, 4, 5, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (2, 4, 6, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (3, 5, 7, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (4, 6, 8, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (5, 7, 9, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (6, 8, 10, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (7, 15, 14, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (8, 15, 17, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (9, 17, 16, false);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (10, 14, 18, false);

INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (11, 9, 11, true);
INSERT INTO friendship(id, user_id, friend_id, pending) VALUES (12, 10, 12, true);

INSERT INTO game_sessions (id, num_players, status, invite_code, is_public, owner_id) VALUES (1, 4, 'FINISHED', 'AGD57F', true, 1);
INSERT INTO game_sessions (id, num_players, status, invite_code, is_public, owner_id) VALUES (2, 4, 'WAITING', 'APD57F', true, 1);
INSERT INTO game_sessions (id, num_players, status, invite_code, is_public, owner_id) VALUES (3, 3, 'PLAYING', 'AGI57F', false, 1);
INSERT INTO game_sessions (id, num_players, status, invite_code, is_public, owner_id) VALUES (4, 5, 'WAITING', 'ASJ57F', true, 1);
INSERT INTO game_sessions (id, num_players, status, invite_code, is_public, owner_id) VALUES (5, 5, 'PLAYING', 'AWS57F', true, 1);


INSERT INTO players(id, is_turn, points, color, user_id,game_id) VALUES (1, false, 5, 'RED',4, 1);
INSERT INTO players(id, is_turn, points, color, user_id,game_id) VALUES (3, false, 10, 'BLUE',7, 1);
INSERT INTO players(id, is_turn, points, color, user_id,game_id) VALUES (2, false, 3, 'GREEN',5, 1);

INSERT INTO achievement(id, threshold, name, badge_image, description, metric) VALUES (1, 5, 'Pescador Principiante', 'https://cdn-icons-png.flaticon.com/512/610/610333.png', 'You have to win 5 games in a row to win this award', 'VICTORIES');
INSERT INTO achievement(id, threshold, name, badge_image, description, metric) VALUES (2, 10, 'Pescador Avanzado', 'https://cdn-icons-png.flaticon.com/512/610/610333.png', 'You have to win 10 games in a row to win this award', 'VICTORIES');
INSERT INTO achievement(id, threshold, name, badge_image, description, metric) VALUES (3, 20, 'Pescador Experto', 'https://cdn-icons-png.flaticon.com/512/610/610333.png', 'You have to win 20 games in a row to win this award', 'VICTORIES');
INSERT INTO achievement(id, threshold, name, badge_image, description, metric) VALUES (4, 10, 'En racha', 'https://cdn-icons-png.flaticon.com/512/610/610333.png', 'You have to play 10 games in a row to win this award, it doesn´t matter if you win or you loose', 'GAMES_PLAYED');

INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (1, 4, 1, 5, false, 920, '2025-10-28 10:00:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (2, 7, 1, 10, true, 920, '2025-10-28 10:00:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (3, 5, 1, 3, false, 920, '2025-10-28 10:00:00');

INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (4, 4, 20, 15, true, 610, '2025-10-29 11:15:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (5, 6, 20, 8, false, 610, '2025-10-29 11:15:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (6, 8, 20, 2, false, 610, '2025-10-29 11:15:00');

INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (7, 5, 21, 7, false, 750, '2025-10-29 12:30:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (8, 6, 21, 12, true, 750, '2025-10-29 12:30:00');

INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (9, 7, 22, 20, true, 1200, '2025-10-29 14:00:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (10, 8, 22, 18, false, 1200, '2025-10-29 14:00:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (11, 9, 22, 11, false, 1200, '2025-10-29 14:00:00');
INSERT INTO player_game_stats(id, user_id, game_id, points, is_winner, duration_seconds, finished_at) VALUES (12, 10, 22, 5, false, 1200, '2025-10-29 14:00:00');
