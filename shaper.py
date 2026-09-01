import os
import sqlite3
import xml.etree.ElementTree as ET
import glob
import argparse

# ID Normalization Spec: Trim to ensure matching across sources
def normalize_id(id_str):
    if id_str is None: return None
    return str(id_str).strip()

def create_db(db_path):
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # Create Singularities Table (Matches Room Version 7)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS singularities (
            id TEXT,
            gamesystem_id TEXT,
            faction_id TEXT,
            name TEXT,
            xml_tag TEXT,
            value TEXT,
            parent_id TEXT,
            target_id TEXT,
            link_type TEXT,
            gamesystem_name TEXT,
            faction_name TEXT,
            is_awakened INTEGER DEFAULT 0,
            PRIMARY KEY (gamesystem_id, faction_id, id)
        )
    ''')
    
    # Create Tags Table
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS singularity_tags (
            gamesystem_id TEXT,
            faction_id TEXT,
            singularity_id TEXT,
            tag TEXT,
            PRIMARY KEY (gamesystem_id, faction_id, singularity_id, tag)
        )
    ''')
    
    conn.commit()
    return conn

def parse_file(conn, file_path, repo_name):
    print(f"  Flattening: {os.path.basename(file_path)}")
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
    except Exception as e:
        print(f"  [ERROR] Failed to parse {file_path}: {e}")
        return

    # Extract Passport Info
    tag_name = root.tag.split('}')[-1]
    f_id = normalize_id(root.get('id'))
    f_name = root.get('name')
    gs_id = repo_name # Primary ID Lockdown
    gs_name = root.get('gameSystemName') if tag_name == 'catalogue' else f_name
    
    cursor = conn.cursor()
    
    # Recursive parsing stack
    def process_node(node, parent_id):
        node_tag = node.tag.split('}')[-1]
        node_id = normalize_id(node.get('id'))
        node_name = node.get('name')
        
        if node_id:
            target_id = normalize_id(node.get('targetId'))
            link_type = node.get('type')
            node_value = node.get('value')
            
            # Insert Singularity
            cursor.execute('''
                INSERT OR REPLACE INTO singularities 
                (id, gamesystem_id, faction_id, name, xml_tag, value, parent_id, target_id, link_type, gamesystem_name, faction_name)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (node_id, gs_id, f_id, node_name or node_tag, node_tag, node_value, parent_id, target_id, link_type, gs_name, f_name))
            
            # Insert Tag
            cursor.execute('''
                INSERT OR REPLACE INTO singularity_tags 
                (gamesystem_id, faction_id, singularity_id, tag)
                VALUES (?, ?, ?, ?)
            ''', (gs_id, f_id, node_id, node_tag))
            
            # Recurse children
            for child in node:
                process_node(child, node_id)
                
    process_node(root, None)
    conn.commit()

def run_shaper(source_dir, output_db, repo_id):
    print(f"Processing Repository: {repo_id}")
    if os.path.exists(output_db): os.remove(output_db)
    
    conn = create_db(output_db)
    
    # Process GST then CATs in the source directory
    files = glob.glob(os.path.join(source_dir, "*.gst")) + glob.glob(os.path.join(source_dir, "*.cat"))
    for f in files:
        parse_file(conn, f, repo_id)
        
    conn.close()
    print(f"Snapshot Complete: {output_db}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Battle Barge Rulebook Shaper')
    parser.add_argument('--source', required=True, help='Directory containing .gst and .cat files')
    parser.add_argument('--output', required=True, help='Output filename (e.g. wh40k.db)')
    parser.add_argument('--id', required=True, help='Unique ID for the game system (Repo Name)')
    
    args = parser.parse_args()
    run_shaper(args.source, args.output, args.id)
