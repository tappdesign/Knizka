
-- insert tags
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000303); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#spoveď' WHERE uf_handle_id_ref = 11000303;


