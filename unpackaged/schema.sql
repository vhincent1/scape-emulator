CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `type` enum('inventory','equipment','bank') NOT NULL,
  `slot` smallint(6) NOT NULL,
  `item` smallint(6) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_type_slot` (`player_id`,`type`,`slot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) NOT NULL,
  `password` char(60) NOT NULL,
  `rights` tinyint(4) NOT NULL,
  `x` smallint(6) NOT NULL,
  `y` smallint(6) NOT NULL,
  `height` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `setting` enum('attack_style','auto_retaliating','two_button_mouse','chat_fancy','private_chat_split','accepting_aid') NOT NULL,
  `value` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_setting` (`player_id`,`setting`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
