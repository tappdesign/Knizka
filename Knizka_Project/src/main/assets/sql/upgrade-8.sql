
-- insert tags
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000003); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#chorí umierajúci' WHERE uf_handle_id_ref = 11000003;

INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000294); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#spoveď' WHERE uf_handle_id_ref = 11000294;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000295); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske, #pobožnosť' WHERE uf_handle_id_ref = 11000295;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000296); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#k svätému mužovi, #pobožnosť, #misie' WHERE uf_handle_id_ref = 11000296;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000297); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske, #pobožnosť' WHERE uf_handle_id_ref = 11000297;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000298); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske, #pobožnosť' WHERE uf_handle_id_ref = 11000298;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000299); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske, #pobožnosť' WHERE uf_handle_id_ref = 11000299;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000300); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske' WHERE uf_handle_id_ref = 11000300;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000301); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske' WHERE uf_handle_id_ref = 11000301;
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000302); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#mariánske' WHERE uf_handle_id_ref = 11000302;



-- ===============================================================================================
-- Zostava: Ku sv. spovedi
-- ===============================================================================================

-- Insert data into main prayers - Ku sv. spovedi
INSERT INTO main.prayers
       (handle_id, creation, last_modification, title, content, insight, prayer_set_flag, package_id)
       VALUES
       (11100006, 11100006, 11100006, 'Ku sv. spovedi', '<div class="NoteWrapper">Ku sv. spovedi</div>', 'Pred sv. spoveďou (kapucíni), Spovedné zrkadlo, Zvláštny úkon ľútosti', 2, 0);

-- Insert data to prayer set
INSERT INTO prayer_linked_set
       (prayer_handle_id_ref, prayer_text_id_ref, text_order, text_type, text_category)
   	   VALUES
   	   (11100006, 11000121, 1, 0, 0);

INSERT INTO prayer_linked_set
       (prayer_handle_id_ref, prayer_text_id_ref, text_order, text_type, text_category)
   	   VALUES
   	   (11100006, 11000294, 2, 0, 0);

INSERT INTO prayer_linked_set
       (prayer_handle_id_ref, prayer_text_id_ref, text_order, text_type, text_category)
   	   VALUES
   	   (11100006, 11000172, 3, 0, 0);
