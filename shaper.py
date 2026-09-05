import os
import sqlite3
import xml.etree.ElementTree as ET
import glob
import argparse
import json

# ID Normalization Spec: Trim to ensure exact matching
def normalize_id(id_str):
    if id_str is None: return None
    return str(id_str).strip()

def create_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    # 1. Singularities Table (Core Elements & Hierarchy)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS singularities (
            id TEXT,
            gamesystem_id TEXT,
            faction_id TEXT,
            name TEXT,
            xml_tag TEXT,
            entry_type TEXT,
            value TEXT,
            parent_id TEXT,
            target_id TEXT,
            gamesystem_name TEXT,
            faction_name TEXT,
            is_awakened INTEGER DEFAULT 0,
            PRIMARY KEY (gamesystem_id, faction_id, id)
        )
    ''')

    # 2. Category Links Table (True Category Bindings)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS category_links (
            gamesystem_id TEXT,
            faction_id TEXT,
            singularity_id TEXT,
            target_id TEXT,
            category_name TEXT,
            is_primary INTEGER DEFAULT 0,
            PRIMARY KEY (gamesystem_id, faction_id, singularity_id, target_id)
        )
    ''')

    # 3. Costs Table (Points & Currency Limits)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS costs (
            gamesystem_id TEXT,
            faction_id TEXT,
            singularity_id TEXT,
            name TEXT,
            value REAL,
            PRIMARY KEY (gamesystem_id, faction_id, singularity_id, name)
        )
    ''')

    # 4. Characteristics Table (True Stat Profiles)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS characteristics (
            gamesystem_id TEXT,
            faction_id TEXT,
            singularity_id TEXT,
            profile_name TEXT,
            profile_type TEXT,
            stat_name TEXT,
            stat_value TEXT,
            PRIMARY KEY (gamesystem_id, faction_id, singularity_id, profile_name, stat_name)
        )
    ''')

    conn.commit()
    return conn

def parse_file(conn, file_path, repo_name, category_map):
    print(f"  Shaping Native XML: {os.path.basename(file_path)}")
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
    except Exception as e:
        print(f"  [ERROR] Failed to parse {file_path}: {e}")
        return

    # Extract Passport Metadata
    tag_name = root.tag.split('}')[-1]
    f_id = normalize_id(root.get('id'))
    f_name = root.get('name')
    gs_id = repo_name # Primary Repo Lockdown
    gs_name = root.get('gameSystemName') if tag_name == 'catalogue' else f_name

    cursor = conn.cursor()

    # First Pass: Register categoryEntries into category_map
    for elem in root.iter():
        elem_tag = elem.tag.split('}')[-1]
        if elem_tag == 'categoryEntry':
            c_id = normalize_id(elem.get('id'))
            c_name = elem.get('name')
            if c_id and c_name:
                category_map[c_id] = c_name

    # Recursive Native Parser Stack
    def process_node(node, parent_id, current_profile_name=None, current_profile_type=None):
        node_tag = node.tag.split('}')[-1]
        node_id = normalize_id(node.get('id'))
        node_name = node.get('name')
        entry_type = node.get('type')
        target_id = normalize_id(node.get('targetId'))
        node_value = node.get('value')

        if node_tag == 'profile':
            current_profile_name = node_name
            current_profile_type = node.get('typeName')

        if node_id:
            # Insert Core Singularity Element
            cursor.execute('''
                INSERT OR REPLACE INTO singularities
                (id, gamesystem_id, faction_id, name, xml_tag, entry_type, value, parent_id, target_id, gamesystem_name, faction_name)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (node_id, gs_id, f_id, node_name or node_tag, node_tag, entry_type, node_value, parent_id, target_id, gs_name, f_name))

        # Specialized Tag Processing (Category Links, Costs, Characteristics)
        if node_tag == 'categoryLink' and parent_id:
            cat_name = node_name or category_map.get(target_id) or target_id
            is_primary = 1 if node.get('primary') == 'true' else 0
            if cat_name and target_id:
                cursor.execute('''
                    INSERT OR REPLACE INTO category_links
                    (gamesystem_id, faction_id, singularity_id, target_id, category_name, is_primary)
                    VALUES (?, ?, ?, ?, ?, ?)
                ''', (gs_id, f_id, parent_id, target_id, cat_name, is_primary))

        elif node_tag == 'cost' and parent_id:
            cost_name = node_name or node.get('name') or 'pts'
            try:
                cost_val = float(node_value) if node_value else 0.0
                cursor.execute('''
                    INSERT OR REPLACE INTO costs
                    (gamesystem_id, faction_id, singularity_id, name, value)
                    VALUES (?, ?, ?, ?, ?)
                ''', (gs_id, f_id, parent_id, cost_name, cost_val))
            except (ValueError, TypeError):
                pass

        elif node_tag == 'characteristic' and parent_id:
            stat_name = node_name or node.get('name')
            if stat_name and current_profile_name:
                cursor.execute('''
                    INSERT OR REPLACE INTO characteristics
                    (gamesystem_id, faction_id, singularity_id, profile_name, profile_type, stat_name, stat_value)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                ''', (gs_id, f_id, parent_id, current_profile_name, current_profile_type or 'Unit', stat_name, node_value or ''))

        # Recurse children
        for child in node:
            process_node(child, node_id or parent_id, current_profile_name, current_profile_type)

    process_node(root, None)
    conn.commit()

def run_shaper(source_dir, output_db, repo_id):
    print(f"Processing Repository: {repo_id}")
    if os.path.exists(output_db): os.remove(output_db)

    conn = create_db(output_db)
    category_map = {}

    # Process GST first, then CATs in the source directory
    files = glob.glob(os.path.join(source_dir, "*.gst")) + glob.glob(os.path.join(source_dir, "*.cat"))
    for f in files:
        parse_file(conn, f, repo_id, category_map)

    conn.close()
    print(f"Shaping Complete: {output_db}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Battle Barge Native XML Shaper')
    parser.add_argument('--source', required=True, help='Directory containing .gst and .cat files')
    parser.add_argument('--output', required=True, help='Output database path (e.g. output/wh40k.db)')
    parser.add_argument('--id', required=True, help='Game system repo ID')

    args = parser.parse_args()
    run_shaper(args.source, args.output, args.id)
