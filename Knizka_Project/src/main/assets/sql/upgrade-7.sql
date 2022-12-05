
-- insert tags
INSERT OR IGNORE INTO user_flags (uf_handle_id_ref) VALUES (11000293); UPDATE user_flags SET uf_tag_list = IFNULL(uf_tag_list, '') || ', ' || '#životné ťažkosti, #pobožnosť' WHERE uf_handle_id_ref = 11000293;

