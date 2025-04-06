import re
import sys

def replace_annotations(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    
    # Replace @NotNull with @Nonnull
    content = re.sub(r'@NotNull', '@Nonnull', content)
    
    # Replace @org.jetbrains.annotations.Nullable with @javax.annotation.Nullable
    content = re.sub(r'@org\.jetbrains\.annotations\.Nullable', '@javax.annotation.Nullable', content)
    
    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)
    
    print(f"Replaced annotations in {file_path}")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        replace_annotations(sys.argv[1])
    else:
        print("Please provide a file path")
