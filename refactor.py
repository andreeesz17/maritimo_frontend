import os
import shutil

src_root = r"E:\proyect_idiomas_danny\app\src"
old_package = "com.ute.guamanidiomas"
new_package = "com.maritimo.control"

def replace_in_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        if old_package in content:
            new_content = content.replace(old_package, new_package)
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Updated package references in: {file_path}")
    except Exception as e:
        print(f"Error processing {file_path}: {e}")

# Step 1: Replace in all files recursively
print("Starting search and replace of package name...")
for root, dirs, files in os.walk(src_root):
    for file in files:
        if file.endswith(('.kt', '.xml', '.kts')):
            replace_in_file(os.path.join(root, file))

# Also replace in the root settings.gradle.kts and build.gradle.kts just in case
replace_in_file(r"E:\proyect_idiomas_danny\settings.gradle.kts")
replace_in_file(r"E:\proyect_idiomas_danny\build.gradle.kts")

# Step 2: Relocate folders
def move_package_dirs(source_set):
    # e.g., app/src/main/java/com/ute/guamanidiomas -> app/src/main/java/com/maritimo/control
    old_dir = os.path.join(src_root, source_set, "java", "com", "ute", "guamanidiomas")
    new_parent = os.path.join(src_root, source_set, "java", "com", "maritimo")
    new_dir = os.path.join(new_parent, "control")

    if os.path.exists(old_dir):
        print(f"Moving {old_dir} to {new_dir}...")
        os.makedirs(new_parent, exist_ok=True)
        # Move all contents from old_dir to new_dir
        if os.path.exists(new_dir):
            for item in os.listdir(old_dir):
                s = os.path.join(old_dir, item)
                d = os.path.join(new_dir, item)
                if os.path.isdir(s):
                    shutil.copytree(s, d, dirs_exist_ok=True)
                else:
                    shutil.copy2(s, d)
            shutil.rmtree(old_dir)
        else:
            shutil.move(old_dir, new_dir)
        
        # Clean up empty old parent directories
        parent = os.path.join(src_root, source_set, "java", "com", "ute")
        if os.path.exists(parent) and not os.listdir(parent):
            os.rmdir(parent)
            print(f"Removed empty directory: {parent}")
    else:
        print(f"Directory {old_dir} does not exist, skipping.")

for source_set in ["main", "androidTest", "test"]:
    move_package_dirs(source_set)

print("Refactoring completed successfully!")
