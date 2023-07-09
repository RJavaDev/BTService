CREATE OR REPLACE FUNCTION get_region_address(regionId INTEGER)
    RETURNS TEXT
    LANGUAGE SQL AS
$$
SELECT STRING_AGG(name, ', ')
FROM (
         WITH RECURSIVE region_name AS (
             SELECT bts_child.name AS name,
                    bts_child.id,
                    bts_child.parent_id,
                    0 AS level
             FROM bts_region bts_child
             WHERE bts_child.id = regionId
             UNION ALL
             SELECT btsr_parent.name AS name,
                    btsr_parent.id,
                    btsr_parent.parent_id,
                    level + 1
             FROM bts_region btsr_parent
                      INNER JOIN region_name rn ON rn.parent_id = btsr_parent.id
         )
         SELECT region_name.name
         FROM region_name
         ORDER BY region_name.level DESC
     ) AS user_address;
$$;
